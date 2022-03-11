/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.collections.CollectionUtils;
import org.jbpm.services.task.impl.model.TaskImpl;
import org.jbpm.services.task.internals.lifecycle.Allowed;
import org.jbpm.services.task.internals.lifecycle.MVELLifeCycleManager;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.task.model.I18NText;
import org.kie.api.task.model.PeopleAssignments;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.TaskData;
import org.kie.internal.task.api.model.InternalTask;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_TASK",
    indexes = {
        @Index(columnList = "tenant, organization, businessKey"),
        @Index(columnList = "tenant, organization, active, businessKey"),
        @Index(columnList = "instance_id, active"),
        @Index(columnList = "rootInstance_id, active")
    }        
)
@Sortables(
    defaultSort = "startTs:ASC",
    value = {
        @Sortable(property = "startTs", pathExpression = "id"),
        @Sortable(property = "expireTs", pathExpression = "internalTask.taskData.expirationTime"),
        @Sortable(property = "instanceId", pathExpression = "instance"),
        @Sortable(property = "name", pathExpression = "internalTask.name"),   
        @Sortable(property = "status", pathExpression = "internalTask.taskData.status")    
    }
)
@Fetchables({
    @Fetchable(property = "instance", type = Instance.class),
    @Fetchable(property = "rootInstance", type = Instance.class),
    @Fetchable(property = "node", type = InstanceNode.class),
    @Fetchable(property = "input"),
    @Fetchable(property = "output"),
    @Fetchable(property = "authorizations"),
    @Fetchable(property = "attachments"),
    @Fetchable(property = "comments"),
    @Fetchable(property = "assignments", type = TaskAssignments.class),
    @Fetchable(property = "notifications", type = Notification.class),
    @Fetchable(property = "notifications?activeOnly", type = Notification.class),
    @Fetchable(property = "history", type = History.class),    
    @Fetchable(property = "nextTasks", type = Task.class)
})
public class Task extends Auditable implements org.kie.api.task.model.Task {

    public static final String ASSIGNMENT_SEPARATOR = System.getProperty("org.jbpm.ht.user.separator", ",");

    public static final String BUSINESS_KEY_PARAMETER = "BusinessKey";
    
    public enum Assignment {
        ActorId,
        GroupId,
        SwimlaneActorId,
        RecipientId,
        RecipientGroupId,
        TaskStakeholderId,
        TaskStakeholderGroupId,
        BusinessAdministratorId,
        BusinessAdministratorGroupId,
        ExcludedOwnerId,
        ExcludedGroupId,
        RefuseGroupId
    }
    
    /**
     * 
     */
    private static final Map<TaskAuthorization, Map<Status, Set<Allowed>>> authorizations = new HashMap<>();
    static {
        MVELLifeCycleManager.initMVELOperations().entrySet().forEach(operation -> {
            try {
                operation.getValue().forEach((command) -> {
                    command.getStatus().forEach((status) -> {
                        authorizations
                            .computeIfAbsent(TaskAuthorization.valueOf(operation.getKey().name()), k -> new HashMap<>())
                            .computeIfAbsent(status, k -> new HashSet<>())
                            .addAll(command.getAllowed());
                    });
                });
            } catch (IllegalArgumentException e) {
                // unsupported action
            }
        });
    }
    
    @Id
    private Long id;    

    @Basic
    @Column(nullable = false)
    private String tenant;

    @Basic
    @Column(nullable = false)
    private String organization;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)        
    private Instance instance;

    // we need to set optional true for root instances because in some scenario 
    // (independent call activity) a child instance can survive to root    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))        
    private Instance rootInstance;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, unique = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) 
    private InstanceNode node;
    
    @Basic
    @Column(nullable = false, unique = true)
    private Long workItemId;

    @Basic
    @Column(nullable = false)    
    private String businessKey;
    
    @Basic
    private String swimlaneActorId;    
    
    @Basic
    private String refuseGroupId;    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)    
    private Date startTs;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTs;    

    @Basic
    @Column(nullable = false)    
    private Boolean active;
    
    @OneToOne(targetEntity = TaskImpl.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @MapsId
    @JoinColumn(name = "id")
    private InternalTask internalTask;

    @OneToOne(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false) 
    private TaskIO io;
    
    @OrderBy("id ASC")
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    private List<Notification> notifications;

    @OrderBy("id ASC")
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    private List<History> history;
    
    public Task() {
    }

    public Task(
        InstanceNode node,
        InternalTask internalTask,
        WorkItem workItem,
        String businessKey,
        String swimlaneActorId, 
        String refuseGroupId) {

        if (node.getInstance().getId() != internalTask.getTaskData().getProcessInstanceId() || 
            node.getWorkItemId() != internalTask.getTaskData().getWorkItemId() || 
            node.getWorkItemId() != workItem.getId()) {
            throw new IllegalArgumentException();
        }
        this.id = internalTask.getId();
        this.tenant = node.getInstance().getTenant();
        this.organization = node.getInstance().getOrganization();
        this.instance = node.getInstance();
        this.rootInstance = node.getInstance().getRootInstance().orElse(null);
        this.node = node;
        this.workItemId = internalTask.getTaskData().getWorkItemId();
        this.businessKey = businessKey;
        this.internalTask = internalTask;
        this.swimlaneActorId = swimlaneActorId;
        this.refuseGroupId = refuseGroupId;
        this.active = true;
        this.startTs = new Date();
        this.io = new TaskIO(this, workItem.getParameters());
        this.notifications = new ArrayList<>();
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public String getTenant() {
        return tenant;
    }

    public String getOrganization() {
        return organization;
    }

    public Instance getInstance() {
        return instance;
    }

    public Optional<Instance> getRootInstance() {
        return optionalProxy(rootInstance);
    }

    public InstanceNode getNode() {
        return node;
    }

    public Long getWorkItemId() {
        return workItemId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    protected String getSwimlaneActorId() {
        return swimlaneActorId;
    }

    protected String getRefuseGroupId() {
        return refuseGroupId;
    }

    public Date getStartTs() {
        return startTs;
    }

    public Date getEndTs() {
        return endTs;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isCompleted() {
        return !active;
    }
    
    public TaskIO getIO() {
        return io;
    }

    public List<Notification> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }

    public List<Notification> getActiveNotifications(AuthenticationContext context) {
        return notifications
            .stream()
            .filter(n -> n.getRecipients()
                .stream()
                .anyMatch(r -> r.isActive(context)))
             .collect(Collectors.toUnmodifiableList());
    }

    public List<History> getHistory() {
        return Collections.unmodifiableList(history);
    }
    
    public TaskAssignments getAssignments() {
        return new TaskAssignments(this);
    }    

    public Set<TaskAuthorization> getAuthorizations(AuthenticationContext context) {

        Status status;
        TaskAssignments assignments;
        Set<TaskAuthorization> authorizations;
        boolean isExcludedOwner, authorized;
        
        status = getTaskData().getStatus();
        assignments = getAssignments();
        authorizations = new HashSet<>();
        isExcludedOwner = assignments.getExcludedOwners().contains(context.getUserId());
        for (TaskAuthorization authorization : TaskAuthorization.values()) {
            if (authorization == TaskAuthorization.Save || 
                authorization == TaskAuthorization.Refuse) {
                continue;
            }
            for (Allowed allowed : Task.authorizations
                .get(authorization)
                .getOrDefault(status, Collections.<Allowed>emptySet())) {
                if (context.isAdmin()) {
                    authorized = true;
                } else {
                    switch (allowed) {
                        case Anyone:
                            authorized = !isExcludedOwner;
                            break;
                        case BusinessAdministrator:
                            authorized = CollectionUtils.containsAny(
                                assignments.getBusinessAdministrators(), 
                                context.getIdentities());
                            break;
                        case Initiator:
                            authorized = !isExcludedOwner && 
                                context.getUserId().equals(assignments.getTaskInitiator());
                            break;
                        case Owner:
                            authorized = !isExcludedOwner && 
                                context.getUserId().equals(assignments.getActualOwner());
                            break;
                        case PotentialOwner:
                            authorized = !isExcludedOwner && 
                                CollectionUtils.containsAny(
                                    assignments.getPotentialOwners(), 
                                    context.getIdentities());
                            break;
                        case TaskStakeholders:
                            authorized = !isExcludedOwner && 
                                CollectionUtils.containsAny(
                                    assignments.getTaskStakeholders(), 
                                    context.getIdentities());
                            break;
                        default:
                            throw new IncompatibleClassChangeError();
                    }
                }
                if (authorized) {
                    authorizations.add(authorization);
                }                
            }
        }
        if (authorizations.contains(TaskAuthorization.Claim) || authorizations.contains(TaskAuthorization.Complete)) {
            authorizations.add(TaskAuthorization.Save);
        }
        if (authorizations.contains(TaskAuthorization.Forward) && refuseGroupId != null) {
            authorizations.add(TaskAuthorization.Refuse);
        }
        return Collections.unmodifiableSet(authorizations);
    }
    
    public boolean hasAuthorization(AuthenticationContext context, TaskAuthorization authorization) {
        return getAuthorizations(context).contains(authorization);
    }

    public boolean hasAnyAuthorization(AuthenticationContext context, TaskAuthorization... authorizations) {
        
        Set<TaskAuthorization> _authorizations;
        
        _authorizations = getAuthorizations(context);
        for (TaskAuthorization authorization : authorizations) {
            if (_authorizations.contains(authorization)) {
                return true;
            }
        }
        return false;
    }
    
    public List<Task> getNextTasks(AuthenticationContext context) {
        return instance
            .getNodes(true)
            .stream()
            .filter(n -> n.isTask())
            .map(n -> n.getTask())
            .filter(t -> 
                startTs.before(t.getStartTs()) && 
                context.getUserId().equals(t.getSwimlaneActorId()))
            .collect(Collectors.toList());
    }
    
    public void setInput(Map<String, Object> input) {
        if (!active) {
            return;
        }
        io.setInput(input);
    }
    
    public void complete(Map<String, Object> output) {
        
        if (!active) {
            throw new IllegalStateException();
        }
        
        active = false;
        endTs = new Date();
        io.setOutput(output);
    }

    public boolean hasFormAsset() {
        if (instance.getConfiguration().isPresent()) {
            return instance.getConfiguration().get()
                .hasAsset(String.format("%s.ftl", getFormName()));
        } else {
            return false;
        }
    }
    
    public Asset getFormAsset() {
        if (instance.getConfiguration().isPresent()) {
            return instance.getConfiguration().get()
                .getAsset(String.format("%s.ftl", getFormName()));
        } else {
            return null;
        }
    }
    
    
    @Override
    public Integer getPriority() {
        return internalTask.getPriority();
    }

    @Override
    public List<I18NText> getNames() {
        return internalTask.getNames();
    }

    @Override
    public List<I18NText> getSubjects() {
        return internalTask.getSubjects();
    }

    @Override
    public List<I18NText> getDescriptions() {
        return internalTask.getDescriptions();
    }

    @Override
    public String getName() {
        return internalTask.getName();
    }

    @Override
    public String getSubject() {
        return internalTask.getSubject();
    }

    @Override
    public String getDescription() {
        return internalTask.getDescription();
    }
    
    @Override
    public PeopleAssignments getPeopleAssignments() {
        return internalTask.getPeopleAssignments();
    }

    @Override
    public TaskData getTaskData() {
        return internalTask.getTaskData();
    }

    @Override
    public String getTaskType() {
        return internalTask.getTaskType();
    }

    @Override
    public Boolean isArchived() {
        return internalTask.isArchived();
    }

    @Override
    public Integer getVersion() {
        return internalTask.getVersion();
    }

    @Override
    public String getFormName() {
        return internalTask.getFormName();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException();
    }    
}
