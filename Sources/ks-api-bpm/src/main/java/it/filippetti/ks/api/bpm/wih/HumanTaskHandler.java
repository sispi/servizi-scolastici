/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.wih;

import com.google.common.base.Strings;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import it.filippetti.ks.api.bpm.configuration.ApplicationProperties;
import it.filippetti.ks.api.bpm.model.Instance;
import it.filippetti.ks.api.bpm.model.Settings;
import it.filippetti.ks.api.bpm.repository.InstanceRepository;
import it.filippetti.ks.api.bpm.service.IdentityService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import keysuite.cache.ClientCache;
import keysuite.docer.client.Actor;
import keysuite.docer.client.Group;
import keysuite.docer.client.User;
import org.apache.commons.lang3.StringUtils;
import org.drools.core.process.instance.impl.WorkItemImpl;
import org.jbpm.services.task.exception.PermissionDeniedException;
import org.jbpm.services.task.impl.model.AttachmentImpl;
import org.jbpm.services.task.impl.model.TaskDataImpl;
import org.jbpm.services.task.impl.util.HumanTaskHandlerHelper;
import org.jbpm.services.task.utils.ContentMarshallerHelper;
import org.jbpm.services.task.utils.OnErrorAction;
import org.jbpm.services.task.wih.AbstractHTWorkItemHandler;
import org.jbpm.services.task.wih.util.PeopleAssignmentHelper;
import org.kie.api.runtime.Environment;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.runtime.process.WorkflowProcessInstance;
import org.kie.api.task.model.Attachment;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;
import org.kie.internal.runtime.manager.RuntimeManagerRegistry;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.kie.internal.task.api.InternalTaskService;
import org.kie.internal.task.api.TaskModelProvider;
import org.kie.internal.task.api.model.*;
import org.mvel2.templates.TemplateRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
 * !!! MINIMAL PORT FROM OLD KEYSUITE TO REFACTOR !!!
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 * @author ?
 */
@Component
@WorkItemHandlerName("Human Task")
public class HumanTaskHandler extends AbstractHTWorkItemHandler {

    private static final Logger log = LoggerFactory.getLogger(HumanTaskHandler.class);

    public static final String DOC_CORRELATO = "DOC_CORRELATO";
    
    private static final Pattern p = Pattern.compile("^(\\w+):\\s*(.+)$", Pattern.MULTILINE);
    private static final Pattern OldHTPattern = Pattern.compile("(?:Old\\sHT(?:\\sEvents)?|HT-\\w+)_block\\d+(?:_block\\d+)?");

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private IdentityService identityService;
    
    @Autowired
    private InstanceRepository instanceRepository;

    private List<String> startDeadlines;
    private List<String> endDeadlines;

    private JwtParser jwtParser;
    
    public HumanTaskHandler() {
        startDeadlines = new ArrayList<>();
        endDeadlines = new ArrayList<>();
    }

    /**
     * 
     * @param workItem
     * @param manager 
     */
    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {

        log.info(String.format(">>> executeWorkItem 'Human Task' (%d)", workItem.getId()));
        
        // FIX
        if (workItem.getParameter(AssignmentHelper.ACTOR_ID) == null && 
            workItem.getParameter(AssignmentHelper.GROUP_ID) == null) {
            if (workItem.getParameter("SwimlaneActorId") != null) {
                workItem.getParameters().put(
                    AssignmentHelper.ACTOR_ID, 
                    workItem.getParameter("SwimlaneActorId"));
            }
        }
        
        // FIX
        String bas = checkActor(workItem.getParameters(), AssignmentHelper.BUSINESSADMINISTRATOR_GROUP_ID);
        if (StringUtils.isBlank(bas)) {
            bas = ApplicationProperties.get().adminRole();
        } else {
            Set<String> set = Arrays
                .stream(bas.split(",", -1))
                .filter(i -> !StringUtils.isBlank(i))
                .collect(Collectors.toSet());
            set.add(ApplicationProperties.get().adminRole());
            bas = set.stream().collect(Collectors.joining(","));
        }
        workItem.getParameters().put(AssignmentHelper.BUSINESSADMINISTRATOR_GROUP_ID, bas);
        
        // FIX
        workItem.getParameters().put(AssignmentHelper.BUSINESSADMINISTRATOR_ID, ApplicationProperties.get().adminUser());
        
        String deploymentId = ((org.drools.core.process.instance.WorkItem) workItem).getDeploymentId();
        RuntimeManager runtimeManager = RuntimeManagerRegistry.get().getManager(deploymentId);
        RuntimeEngine runtime = runtimeManager.getRuntimeEngine(ProcessInstanceIdContext.get(workItem.getProcessInstanceId()));
        KieSession ksessionById = runtime.getKieSession();
        //String locale = (String)workItem.getParameter("Locale");
        Instance instance;
        String tenant;

        /**
         * *****************GESTIONE DEALINE DI DEFAULT*****************
         */
   
        WorkflowProcessInstance pi = (WorkflowProcessInstance) ksessionById.getProcessInstance(workItem.getProcessInstanceId());

        String defaultCodEnte = (String) pi.getVariable("INSTANCE_ENTE");
        String defaultCodAoo = (String) pi.getVariable("INSTANCE_AOO");

        workItem.getParameters().put(AssignmentHelper.BUSINESSADMINISTRATOR_GROUP_ID, pi.getVariable("Administrators"));

        //FileSystemRepository fileSystemRepository = new FileSystemRepository();
        //String settings = fileSystemRepository.getFileSettings(pi.getProcessId(), defaultCodEnte, defaultCodAoo,em);
        //Map<String, Object> mapConfiguration = it.kdm.orchestratore.util.Helper.jsonToHashMapSingleNode(settings, "configuration");
        //Map<String, Object> mapConfiguration = (Map<String,Object>) new ConfigurationHelper(em).getConfiguration(pi.getProcessId(), defaultCodEnte, defaultCodAoo).get("configuration");
        // get instance
        instance = instanceRepository
            .findById(workItem.getProcessInstanceId())
            .orElseThrow(() -> {
            throw new IllegalStateException(String.format(
                    "Instance %d not found",
                    workItem.getProcessInstanceId()));
            });

        // get tenant
        tenant = instance.getTenant();

        // get configuration
        Map<String, Object> mapConfiguration;
        try {
            mapConfiguration = instance
                .getConfiguration()
                .orElseThrow(IllegalStateException::new)
                .getSettings()
                .getMap(Settings.KEY_CONFIGURATION);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        assert (mapConfiguration != null);
        for (String key : mapConfiguration.keySet()) {
            if (key.startsWith("Deadline")) {
                workItem.getParameters().put("conf_" + key, mapConfiguration.get(key));
            }
        }

        /**
         * ************************************************************
         */

        boolean autocomplete = Boolean.TRUE.equals((Boolean) workItem.getParameter("autocomplete"));

        Object corrDoc = workItem.getParameters().get("relatedDocument");

        if (corrDoc != null) {
            String DOCNUM = null;
            if (corrDoc instanceof Map) {
                DOCNUM = (String) ((Map) corrDoc).get("DOCNUM");
            } else {
                DOCNUM = corrDoc.toString();
            }
            ((WorkItemImpl) workItem).setParameter(DOC_CORRELATO, DOCNUM);
        }

        checkPINs((WorkItemImpl) workItem, tenant);

        InternalTask task = (InternalTask) createTaskBasedOnWorkItemParams(ksessionById, workItem);
        
        DeadlineHelper helper = new DeadlineHelper();

        if (startDeadlines.size() > 0) {
            List<Deadline> startDeadlinesTask = task.getDeadlines().getStartDeadlines();
            List<Deadline> newDeadlines = new ArrayList<>(startDeadlinesTask);

            for (String deadLine : startDeadlines) {
                newDeadlines.addAll(helper.parseDeadlineString(deadLine));
            }

            task.getDeadlines().setStartDeadlines(newDeadlines);
        }

        if (endDeadlines.size() > 0) {
            List<Deadline> endDeadlinesTask = task.getDeadlines().getEndDeadlines();
            List<Deadline> newDeadlines = new ArrayList<>(endDeadlinesTask);

            for (String deadLine : endDeadlines) {
                newDeadlines.addAll(helper.parseDeadlineString(deadLine));
            }

            task.getDeadlines().setEndDeadlines(newDeadlines);
        }

        if (workItem.getParameter(AssignmentHelper.RECIPIENT_GROUP_ID) != null
                || workItem.getParameter(AssignmentHelper.TASKSTAKEHOLDER_GROUP_ID) != null
                || workItem.getParameter(AssignmentHelper.EXCLUDED_GROUP_ID) != null) {
            AssignmentHelper peopleAssignmentHelper = new AssignmentHelper();

            InternalPeopleAssignments peopleAssignments = peopleAssignmentHelper.getNullSafePeopleAssignments(task);

            peopleAssignmentHelper.assignTaskStakeholdersGroups(workItem, peopleAssignments);
            peopleAssignmentHelper.assignRecipientsGroups(workItem, peopleAssignments);
            peopleAssignmentHelper.assignExcludedGroups(workItem, peopleAssignments);

            ((InternalTask) task).setPeopleAssignments(peopleAssignments);
        }

        //ContentData content = createTaskContentBasedOnWorkItemParams(ksessionById, workItem);
        Map<String, Object> content = createTaskDataBasedOnWorkItemParams(ksessionById, workItem);

        try {

            String doceroperation = (String) workItem.getParameters().get("EVENTO_OPERAZIONE");

            if (Strings.isNullOrEmpty(doceroperation)) {
                doceroperation = (String) workItem.getParameters().get("taskType");
            }

            if (Strings.isNullOrEmpty(doceroperation)) {
                if ("Firma Remota UD".equals(task.getName())) {
                    doceroperation = "FIRMA";
                } else {
                    doceroperation = "GENERICO";
                }
            }

            task.setSubject(doceroperation);

            String DOCNUM = (String) workItem.getParameter(DOC_CORRELATO);

            if (!Strings.isNullOrEmpty(DOCNUM)) {
                Attachment attachment = TaskModelProvider.getFactory().newAttachment();
                ((AttachmentImpl) attachment).setAccessType(AccessType.Inline);
                ((AttachmentImpl) attachment).setAttachedAt(new Date());
                ((AttachmentImpl) attachment).setName(DOCNUM);

                ((AttachmentImpl) attachment).setContentType(doceroperation);

                List<Attachment> attachments = new ArrayList<Attachment>();
                ((TaskDataImpl) task.getTaskData()).setAttachments(attachments);
                attachments.add(attachment);
            }

            long taskId = ((InternalTaskService) runtime.getTaskService()).addTask(task, content);

            if (task.getTaskData().getStatus() == Status.Ready || 
                task.getTaskData().getStatus() == Status.Reserved) {
                String swimlaneUser = (String) workItem.getParameter("SwimlaneActorId");
                if (!Strings.isNullOrEmpty(swimlaneUser)) {
                    // FIX
                    try {
                        runtime.getTaskService().start(taskId, swimlaneUser);
                    } catch (Throwable t) {
                        log.warn(String.format(
                            "Cannot start task %d for swimlane user '%s': %s", 
                            taskId, swimlaneUser, t.getMessage()));
                    }
                }
            }

            if (autocomplete) {
                manager.completeWorkItem(
                    workItem.getId(), 
                    Collections.singletonMap("autocomplete", (Object) true));
            }
        } catch (Exception e) {
            if (action.equals(OnErrorAction.ABORT)) {
                manager.abortWorkItem(workItem.getId());
            } else if (action.equals(OnErrorAction.RETHROW)) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                } else {
                    throw new RuntimeException(e);
                }
            } else if (action.equals(OnErrorAction.LOG)) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 
     * @param workItem
     * @param manager 
     */
    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

        log.info(String.format(">>> abortWorkItem 'Human Task' (%d)", workItem.getId()));
        
        String deploymentId = ((org.drools.core.process.instance.WorkItem) workItem).getDeploymentId();
        RuntimeManager runtimeManager = RuntimeManagerRegistry.get().getManager(deploymentId);
        RuntimeEngine runtime = runtimeManager.getRuntimeEngine(ProcessInstanceIdContext.get(workItem.getProcessInstanceId()));
        Task task = runtime.getTaskService().getTaskByWorkItemId(workItem.getId());
        if (task != null) {
            try {
                runtime.getTaskService().exit(task.getId(), ApplicationProperties.get().adminUser());
            } catch (PermissionDeniedException e) {
                log.info(e.getMessage());
            }
        }

    }

    /**
     * 
     * @param workItem
     * @param obj
     * @return 
     */
    private String createDeadlineString(WorkItemImpl workItem, Object obj, String tenant) {

        String tmpl;

        if (obj instanceof Map) {
            tmpl = "";

            for (Object key : ((Map) obj).keySet()) {
                tmpl += String.format("%s:%s\n", key, ((Map) obj).get(key));
            }
        } else {
            tmpl = obj.toString();

            if (obj instanceof String && tmpl.startsWith("[")) {
                return tmpl;
            }
        }

        /*
        from: admin
        deadlines: 1d
        groups: ${doc['TaskStakeholderId']}
        togroups: ${doc['TaskStakeholderId']},${doc['GroupId']}
        subject: Notifica relativa all'attivitÃ  #${taskId}
        <a href='${baseUrl}${taskId}' >Link dettagli dell'attivitÃ </a>
         */
        //"users", "groups", "from", "tousers", "togroups", "replyto", "subject","body"
        tmpl += "\n<!-- taskId:${taskId} -->";

        String DOCNUM = (String) workItem.getParameter(DOC_CORRELATO);

        if (!Strings.isNullOrEmpty(DOCNUM)) {
            tmpl += "\n<!-- DOCNUM_CORRELATO:" + DOCNUM + " -->";
        }

        //List<OrganizationalEntity> owners = new ArrayList<>();
        AssignmentHelper helper = new AssignmentHelper();

        String owners = ClientCache.POT_OWNERS;
        String actualOwner = ClientCache.ACTUAL_OWNER;

        if (!Strings.isNullOrEmpty((String) workItem.getParameter(AssignmentHelper.ACTOR_ID))) {
            owners += "," + workItem.getParameter(AssignmentHelper.ACTOR_ID);
        }

        if (!Strings.isNullOrEmpty((String) workItem.getParameter(AssignmentHelper.GROUP_ID))) {
            owners += "," + workItem.getParameter(AssignmentHelper.GROUP_ID);
        }

        if (!Strings.isNullOrEmpty((String) workItem.getParameter("SwimlaneActorId"))) {
            actualOwner += "," + workItem.getParameter("SwimlaneActorId");
        }

        Map<String, Object> vars = new HashMap<String, Object>(workItem.getParameters());

        for (String key : System.getProperties().stringPropertyNames()) {
            vars.put(key, System.getProperty(key));
        }

        vars.put("doc", workItem.getParameters());
        vars.put("processInstanceId", workItem.getProcessInstanceId());
        vars.put("processSessionId", "${processSessionId}");
        vars.put("workItemId", workItem.getId());
        vars.put("expirationTime", "${expirationTime}");
        vars.put("taskId", "${taskId}");
        vars.put("owners", owners);
        vars.put("actualOwner", actualOwner);

        //vars.put("baseUrl",System.getProperty("taskDetailUrl",""));
        String[] deadlines = null;
        String attributes = "";

        String backofficeBaseUrl = applicationProperties.backofficeBaseUrl();

        tmpl = tmpl.replaceAll("\\$TaskStakeholderGroupId", "\\${doc['TaskStakeholderGroupId']}");
        tmpl = tmpl.replaceAll("\\$RecipientGroupId", "\\${doc['RecipientGroupId']}");
        tmpl = tmpl.replaceAll("\\$GroupId", "\\${doc['GroupId']}");
        tmpl = tmpl.replaceAll("\\$ActorId", "\\${doc['ActorId']}");
        tmpl = tmpl.replaceAll("\\$SwimlaneActorId", "\\${doc['SwimlaneActorId']}");
        tmpl = tmpl.replaceAll("\\$taskName", "\\${doc['name']}");

        tmpl = tmpl.replaceAll("\\$taskId", "\\${taskId}");
        tmpl = tmpl.replaceAll("\\$owners", "\\${owners}");
        tmpl = tmpl.replaceAll("\\$taskUrl", backofficeBaseUrl + "/bpm/instances/task?id=\\${taskId}");
        tmpl = tmpl.replaceAll("\\$baseUrl", backofficeBaseUrl);

        //tmpl = tmpl.replaceAll("\\$\\{([^\\}]+)\\}","\\$\\{doc['$1']\\}");
        tmpl = (String) TemplateRuntime.eval(tmpl, vars);

        // rimuovo i caratteri riservati |^@ , i null e le virgole inutili
        tmpl = tmpl.replace("^", "&#94;").replace("@", "&#64").replace("|", "&#124;");
        tmpl = tmpl.replaceAll("(?<=[:,])null(?=[,$\\n\\r])", "");
        tmpl = tmpl.replaceAll(",+", ",");
        tmpl = tmpl.replaceAll("(?<=[:,]),|,(?=[\\r\\n])|,$", "");

        BufferedReader bufReader = new BufferedReader(new StringReader(tmpl));

        String line = null;
        Boolean onlyMail = tmpl.contains("type:OnlyMail\n");

        String htAdmins = applicationProperties.adminRole();;
        String htAdmin = applicationProperties.adminUser();
        /*
        try {
            htAdmins = ServerProperties.getParams("human.task.group.administrators");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Strings.isNullOrEmpty(htAdmins)) {
            htAdmins = "SYS_ADMINS";
        }
        */
        try {
            while ((line = bufReader.readLine()) != null) {
                if (line.startsWith("from:")
                        || line.startsWith("groups:")
                        || line.startsWith("togroups:")
                        || line.startsWith("tousers:")) {
                    line = line.replaceAll("Administrators", htAdmins);
                    line = line.replaceAll("Administrator", htAdmin);
                }

                if (line.startsWith("deadlines:")) {
                    if (line.trim().length() == "deadlines:".length()) {
                        deadlines = "0s".split(",");
                    } else {
                        deadlines = line.substring("deadlines:".length()).split(",");
                    }
                } else {
                    if (line.matches("^\\w+:\\s*$")) {
                        continue;
                    }

                    boolean isBody = line.startsWith("body:");
                    if (line.matches("^\\w+:.*$")) {

                        if (line.startsWith("toactors:") || line.startsWith("togroups:") && tmpl.indexOf("tousers:") == -1) {

                            String[] actors = line.substring("togroups:".length()).split(",");

                            List<String> groups = new ArrayList<>();
                            List<String> users = new ArrayList<>();

                            for (int i = 0; i < actors.length; i++) {

                                Actor actor = identityService.getActor(tenant, actors[i]).orElse(null);

                                if (Strings.isNullOrEmpty(actors[i])) {
                                    continue;
                                }
                                if (actor instanceof User) {
                                    users.add(actors[i]);
                                } else if (actor instanceof Group) {
                                    groups.add(actors[i]);
                                } else {
                                    log.warn("actor not found:" + actors[i]);
                                }
                            }

                            if (users.size() > 0) {
                                attributes += "|tousers:" + StringUtils.join(users, ",");
                            }
                            if (groups.size() > 0) {
                                attributes += "|togroups:" + StringUtils.join(groups, ",");
                            }

                        } else {
                            attributes += "|" + line;
                        }
                    } else {
                        isBody = true;
                        attributes += "|body:" + line;
                    }

                    if (isBody) {
                        while ((line = bufReader.readLine()) != null) {
                            attributes += "\n" + line;
                        }

                        if (onlyMail) {
                            attributes += "\n<!-- NO_DESKTOP_NOTIFY -->";
                        }

                        break;
                    }
                }
            }
        } catch (IOException e) {
            //do nothing
        }

        if (deadlines == null || deadlines.length == 0) {
            return null;
        }

        //[groups:<...>|tousers:<actor1>|subject:<subject1>|body:<body1>]@[delay1]^[togroups:<actor2>|subject:<subject2>|body:<body2>]@[delay2]
        String components = "";

        for (int i = 0; i < deadlines.length; i++) {
            String deadline = deadlines[i].trim();

            if (!Strings.isNullOrEmpty(deadline)) {
                components += String.format("[%s]@[%s]^", attributes, deadline);
            }
        }

        return components;
    }

    /**
     * 
     * @param workItem
     * @param field
     * @return 
     */
    private String getDeadline(WorkItemImpl workItem, String field, String tenant) {
        if (workItem.getParameter(field) != null) {
            Object value = workItem.getParameter(field);
            String components = "";

            if (value instanceof List) {
                for (Object comp : (List) value) {
                    components += createDeadlineString(workItem, comp, tenant);
                }
            } else {
                components = createDeadlineString(workItem, value, tenant);
            }

            return components;
        }
        return null;
    }

    /**
     * 
     * @param workItem
     * @param field 
     */
    private void checkDeadline(WorkItemImpl workItem, String field, String tenant) {
        if (workItem.getParameter(field) != null) {
            workItem.setParameter(field, getDeadline(workItem, field, tenant));
        }
    }

    /**
     * 
     * @param workItem
     * @param override
     * @param field 
     */
    private void overrideField(WorkItemImpl workItem, String override, String field) {
        Object value = workItem.getParameter(override);

        if (value != null && !Strings.isNullOrEmpty(value.toString())) {
            workItem.setParameter(field, value);
        }
    }

    /**
     * 
     * @param workItem 
     */
    private void checkPINs(WorkItemImpl workItem, String tenant) {

        //NodeName diventa la descrizione web (Name dell'HT) e per default Ã¨ il nome bpmn2 del nome
        //TaskName ,se specificato, diventa la FTL (FormName del HT) e per i vecchi HT Ã¨ quello digitato
        //su webapp se non trova [TaskName].ftl del HT cerca il [NodeName].ftl
        //il vecchio designer esporta la ftl come TaskName digitato dall'utente
        //il nuovo designer esporta la ftl come [label]_[id blocco]
        //l'handler imposta TaskName in FormName e NodeName in Name
        //AppBPM testa entrambi getFormName() e getName()

        //name dinamico (variabile di processo)
        String name = (String) workItem.getParameter("name");

        //name statico (xml)
        String NodeName = (String) workItem.getParameter("NodeName");

        if (OldHTPattern.matcher(NodeName).find()) {
            workItem.setParameter("TaskName", NodeName);
        }

        if (!Strings.isNullOrEmpty(name)) {
            workItem.setParameter("NodeName", name);
        }

        /*if (Strings.isNullOrEmpty(name)) {
            //e' sicuramente un vecchio HT (da vecchio o nuovo designer)
            //la form potrebbe essere NodeName oppure TaskName

            workItem.setParameter("NodeName", workItem.getParameter("TaskName"));
            workItem.setParameter("TaskName", NodeName);
        } else {
            //puÃ² essere vecchio o nuovo HT, dipende dal NodeName
            //in questo caso supportiamo solo il nuovo designer e
            //quindi la formname Ã¨ sempre [label]_[idblocco]
            //nei nuovi HT tale valore Ã¨ in TaskName
            //nei vecchi HT tale valore Ã¨ in NodeName

            workItem.setParameter("NodeName", name);

            if (OldHTPattern.matcher(NodeName).find()) {
                workItem.setParameter("TaskName", NodeName);
            }
        }

        workItem.setParameter("taskName", workItem.getParameter("NodeName"));*/

        //overrideField(workItem, "DisplayName" , "NodeName");
        overrideField(workItem, "priority", "Priority");
        overrideField(workItem, "skippable", "Skippable");
        overrideField(workItem, "comment", "Comment");
        overrideField(workItem, "description", "Description");
        overrideField(workItem, "SwimlaneActorId", AssignmentHelper.ACTOR_ID);

        /**
         * ************************* TIPIZZAZIONE ATTORI
         * *******************************
         */
        String p1 = checkActor(workItem.getParameters(), AssignmentHelper.GROUP_ID);
        String p2 = checkActor(workItem.getParameters(), AssignmentHelper.ACTOR_ID);
        String p3 = checkActor(workItem.getParameters(), "SwimlaneActorId");

        Set<String> users0 = new HashSet<>();
        Set<String> users = new HashSet<>();

        Set<String> groups0 = new HashSet<>();
        Set<String> groups = new HashSet<>();

        if (!Strings.isNullOrEmpty(p1)) {
            groups0.addAll(Arrays.asList(p1.split(",")));
        }

        if (!Strings.isNullOrEmpty(p2)) {
            users0.addAll(Arrays.asList(p2.split(",")));
        }

        if (!Strings.isNullOrEmpty(p3)) {
            users0.addAll(Arrays.asList(p3.split(",")));
        }

        for (String actor : groups0) {
            if (identityService.getActor(tenant, actor).orElse(null) instanceof User) {
                users.add(actor);
            } else {
                groups.add(actor);
            }
        }

        for (String actor : users0) {
            if (identityService.getActor(tenant, actor).orElse(null) instanceof Group) {
                groups.add(actor);
            } else {
                users.add(actor);
            }
        }

        workItem.setParameter(AssignmentHelper.GROUP_ID, StringUtils.join(groups, ','));
        workItem.setParameter(AssignmentHelper.ACTOR_ID, StringUtils.join(users, ','));

        /**
         * ***************************************************************************
         */
        checkActor(workItem.getParameters(), AssignmentHelper.GROUP_ID);
        checkActor(workItem.getParameters(), AssignmentHelper.ACTOR_ID);
        checkActor(workItem.getParameters(), AssignmentHelper.RECIPIENT_ID);
        checkActor(workItem.getParameters(), AssignmentHelper.TASKSTAKEHOLDER_ID);
        checkActor(workItem.getParameters(), AssignmentHelper.REFUSE_GROUP_ID);
        checkActor(workItem.getParameters(), AssignmentHelper.BUSINESSADMINISTRATOR_GROUP_ID);
        checkActor(workItem.getParameters(), AssignmentHelper.TASKSTAKEHOLDER_GROUP_ID);
        checkActor(workItem.getParameters(), AssignmentHelper.RECIPIENT_GROUP_ID);
        checkActor(workItem.getParameters(), AssignmentHelper.EXCLUDED_GROUP_ID);

        checkDeadline(workItem, "NotStartedReassign", tenant);
        checkDeadline(workItem, "NotStartedNotify", tenant);
        checkDeadline(workItem, "NotCompletedReassign", tenant);
        checkDeadline(workItem, "NotCompletedNotify", tenant);

        String[] params = workItem.getParameters().keySet().toArray(new String[0]);

        for (String field : params) {
            Object p = workItem.getParameter(field);
            if (p == null) {
                continue;
            }
            String dl = p.toString();

            if (dl.contains("type:NotStarted\n") || dl.contains("type:OnlyMail\n")) {
                String deadLine = getDeadline(workItem, field, tenant);
                if (!Strings.isNullOrEmpty(deadLine)) {
                    startDeadlines.add(deadLine);
                    workItem.getParameters().remove(field);
                }
            }
            if (dl.contains("type:NotCompleted\n")) {
                String deadLine = getDeadline(workItem, field, tenant);
                if (!Strings.isNullOrEmpty(deadLine)) {
                    endDeadlines.add(deadLine);
                    workItem.getParameters().remove(field);
                }
            }
        }
    }
    
    /**
     * 
     * @param session
     * @param param
     * @return 
     */
    private ContentData createAttachementContentBasedOnWorkItemParams(KieSession session, Map<String, String> param) {

        ContentData content = null;

        if (param != null) {
            Environment env = null;
            if (session != null) {
                env = session.getEnvironment();
            }

            content = ContentMarshallerHelper.marshal(param, env);
        }

        return content;
    }

    /**
     *
     */
    private class DeadlineHelper extends HumanTaskHandlerHelper {

        private List<OrganizationalEntity> businessAdministrators;

        public DeadlineHelper(String... businessAdministratorGroupIds) {

            if (businessAdministratorGroupIds.length == 0) {
                businessAdministratorGroupIds = new String[]{"Administrators"};
            }

            businessAdministrators = new ArrayList<>();
            for (String groupId : businessAdministratorGroupIds) {
                businessAdministrators.add(
                        TaskModelProvider.getFactory().newGroup(groupId));
            }
        }

        public List<Deadline> parseDeadlineString(String deadlineInfo) {
            return parseDeadlineString(deadlineInfo, businessAdministrators, null, false);
        }
    }

    /**
     *
     */
    private class AssignmentHelper extends PeopleAssignmentHelper {

        public static final String TASKSTAKEHOLDER_GROUP_ID = "TaskStakeholderGroupId";
        public static final String REFUSE_GROUP_ID = "RefuseGroupId";
        public static final String RECIPIENT_GROUP_ID = "RecipientGroupId";
        public static final String EXCLUDED_GROUP_ID = "ExcludedGroupId";

        public void processAssignments(String peopleAssignmentIds, List<OrganizationalEntity> organizationalEntities, boolean user) {
            processPeopleAssignments(peopleAssignmentIds, organizationalEntities, user);
        }

        @Override
        public InternalPeopleAssignments getNullSafePeopleAssignments(Task task) {
            return super.getNullSafePeopleAssignments(task);
        }

        public void assignTaskStakeholdersGroups(WorkItem workItem, InternalPeopleAssignments peopleAssignments) {

            String taskStakehodlerIds = (String) workItem.getParameter(TASKSTAKEHOLDER_GROUP_ID);
            List<OrganizationalEntity> taskStakeholders = peopleAssignments.getTaskStakeholders();

            processPeopleAssignments(taskStakehodlerIds, taskStakeholders, false);

        }

        public void assignRecipientsGroups(WorkItem workItem, InternalPeopleAssignments peopleAssignments) {

            String recipientIds = (String) workItem.getParameter(RECIPIENT_GROUP_ID);
            List<OrganizationalEntity> recipients = peopleAssignments.getRecipients();

            processPeopleAssignments(recipientIds, recipients, false);
        }

        public void assignExcludedGroups(WorkItem workItem, InternalPeopleAssignments peopleAssignments) {

            String excludedOwnerIds = (String) workItem.getParameter(EXCLUDED_GROUP_ID);
            List<OrganizationalEntity> excludedOwners = peopleAssignments.getExcludedOwners();

            processPeopleAssignments(excludedOwnerIds, excludedOwners, false);

        }
    }
    
    private String checkActor(Map<String,Object> workItem, String field) {

        Object value = workItem.get(field);

        if (value == null || "".equals(value))
            return null;

        List actors = new ArrayList();

        if (value instanceof Collection) {
            actors.addAll( (Collection) value );
        } else {
            actors.add(value);
        }

        for (int i=0; i<actors.size(); i++){
            value = actors.get(i);
            if (value instanceof Map) {
                String id = (String) ((Map)value).get("identity");
                if (id == null)
                    id = (String) ((Map)value).get("USER_ID");
                else if (id == null)
                    id = (String) ((Map)value).get("GROUP_ID");
                else if (id == null)
                    id = (String) ((Map)value).get("ACTOR_ID");
                if (id != null)
                    actors.set(i,id );
                else
                    actors.set(i,"" );
            }
            else if (value instanceof String) {
                // try to convert and replace value to username in case of jwt token
                try {
                    actors.set(i, identityService.parseJwtToken((String) value).getSubject());
                } catch (JwtException e) {
                    // not a jwt token
                }
                /*
                String token = (String) value;
                int idx = token.indexOf("|uid:") + 5;
                if (idx > 5)
                    actors.set(i,token.substring(idx, token.indexOf("|",idx)));
                */
            }
        }
        value = StringUtils.join(actors, System.getProperty("org.jbpm.ht.user.separator", ","));
        workItem.put(field,value);
        return (String) value;
    }
}
