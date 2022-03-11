/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Where;
import org.kie.api.runtime.process.ProcessInstance;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_INSTANCE",
    indexes = {
        @Index(columnList = "rootInstance_id"),
        @Index(columnList = "parentInstance_id"),
        @Index(columnList = "configuration_id, status"),
        @Index(columnList = "processUnitId, status"),
        @Index(columnList = "archiveTs, status"),
        @Index(columnList = "tenant, organization, processId, root, active"),
        @Index(columnList = "tenant, organization, startTs")
    }
)
@Sortables(
    defaultSort = "startTs:ASC",
    value = {
        @Sortable(property = "startTs", pathExpression = "id"),
        @Sortable(property = "lastActivityTs"),    
        @Sortable(property = "businessName"),    
        @Sortable(property = "status"),    
    }
)
@Fetchables({
    @Fetchable(property = "rootInstance", type = Instance.class),
    @Fetchable(property = "parentInstance", type = Instance.class),
    @Fetchable(property = "treeInstances", type = Instance.class),
    @Fetchable(property = "input"),
    @Fetchable(property = "output"),
    @Fetchable(property = "variables", type = InstanceVariable.class),
    @Fetchable(property = "variables?publicOnly", type = InstanceVariable.class),
    @Fetchable(property = "variableValues"),
    @Fetchable(property = "variableValues?publicOnly"),
    @Fetchable(property = "nodes", type = InstanceNode.class),
    @Fetchable(property = "nodes?activeOnly", type = InstanceNode.class),
    @Fetchable(property = "tasks", type = Task.class),
    @Fetchable(property = "tasks?activeOnly", type = Task.class),
    @Fetchable(property = "treeTasks", type = Task.class),
    @Fetchable(property = "treeTasks?activeOnly", type = Task.class),
    @Fetchable(property = "configuration", type = Configuration.class),
    @Fetchable(property = "notifications", type = Notification.class),
    @Fetchable(property = "notifications?activeOnly", type = Notification.class),
    @Fetchable(property = "commands", type = InstanceCommand.class),
    @Fetchable(property = "commands?failedOnly", type = InstanceCommand.class),
    @Fetchable(property = "history", type = History.class),
    @Fetchable(property = "authorizations"),
    @Fetchable(property = "chat", type = ChatMember.class),
    @Fetchable(property = "nextTasks", type = Task.class)    
})
public class Instance extends Auditable {

    @Id
    private Long id;
    
    @Basic
    @Column(nullable = false)
    private String tenant;

    @Basic
    @Column(nullable = false)
    private String organization;

    @Basic
    @Column(nullable = false)    
    private String name;
    
    @Basic
    @Column(nullable = false)    
    private String version;

    @Basic
    @Column(nullable = false)    
    private Integer versionNumber;

    @Basic
    @Column(nullable = false)    
    private String unitId;

    @Basic
    @Column(nullable = false)    
    private String processId;

    @Basic
    @Column(nullable = false)    
    private String processUnitId;

    @Basic
    @Column(nullable = false)    
    private String businessName;

    @Basic
    private String businessState;

    @Basic
    @Column(nullable = false)   
    private String creatorUserId;
  
    @Basic
    @Column(nullable = false)   
    private String initiatorUserId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)    
    private Date startTs;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false) 
    private Date lastActivityTs;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date archiveTs;

    @Basic
    @Column(nullable = false)    
    private int status;

    @Basic
    @Column(nullable = false)    
    Boolean active;
    
    @Basic
    @Column(nullable = false)    
    private Boolean root;

    // we need to set optional true for root instances because in some scenario 
    // (independent call activity) a child instance can survive to root
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) 
    private Instance rootInstance;

    // we need to set optional true for root instances because in some scenario 
    // (independent call activity) a child instance can survive to root    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) 
    private Instance parentInstance;

    @OneToMany(mappedBy = "instance", fetch = FetchType.LAZY)
    private Set<InstanceVariable> variables;

    @Where(clause = "public_ = true")
    @OneToMany(mappedBy = "instance", fetch = FetchType.LAZY)
    private Set<InstanceVariable> publicVariables;

    @OneToOne(mappedBy = "instance", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private InstanceIO io;
    
    @OrderBy("nodeInstanceId ASC")
    @OneToMany(mappedBy = "instance", fetch = FetchType.LAZY)
    private List<InstanceNode> nodes;

    @OrderBy("nodeInstanceId ASC")
    @Where(clause = "active = true")
    @OneToMany(mappedBy = "instance", fetch = FetchType.LAZY)
    private List<InstanceNode> activeNodes;

    @OrderBy("id ASC")    
    @OneToMany(mappedBy = "instance", fetch = FetchType.LAZY)
    private List<Task> tasks;

    @OrderBy("id ASC")    
    @Where(clause = "active = true")
    @OneToMany(mappedBy = "instance", fetch = FetchType.LAZY)
    private List<Task> activeTasks;
    
    // due to a bug in hibernate jpa implementation we must 
    // use deprecated annotation for bidirectional one-to-many 
    // relationships in order to avoid foreign key generation
    @OrderBy("id ASC") 
    @org.hibernate.annotations.ForeignKey(name = "none")
    @OneToMany(mappedBy = "rootInstance", fetch = FetchType.LAZY)
    private List<Instance> treeInstances;

    @OrderBy("id ASC") 
    @org.hibernate.annotations.ForeignKey(name = "none")
    @OneToMany(mappedBy = "rootInstance", fetch = FetchType.LAZY)
    private List<Task> treeTasks;

    @OrderBy("id ASC") 
    @Where(clause = "active = true")
    @org.hibernate.annotations.ForeignKey(name = "none")
    @OneToMany(mappedBy = "rootInstance", fetch = FetchType.LAZY)
    private List<Task> treeActiveTasks;

    @OneToMany(mappedBy = "instance", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InstanceAccess> accesses;

    @OrderBy("id ASC")      
    @OneToMany(mappedBy = "instance", fetch = FetchType.LAZY)
    private List<InstanceCommand> commands;

    @OrderBy("id ASC") 
    @Where(clause = "status = 'ERROR'")
    @OneToMany(mappedBy = "instance", fetch = FetchType.LAZY)
    private List<InstanceCommand> failedCommands;

    @OneToMany(mappedBy = "instance", fetch = FetchType.LAZY)
    private Set<InstanceCorrelation> correlations;

    @OrderBy("id ASC") 
    @OneToMany(mappedBy = "instance", fetch = FetchType.LAZY)
    private List<Notification> notifications;
    
    @OrderBy("id ASC")
    @OneToMany(mappedBy = "instance", fetch = FetchType.LAZY)
    private List<History> history;
    
    // we need to set optional true for configuration because in some scenario 
    // configuration could be deleted when there's no active instances
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Configuration configuration;
    
    @OneToOne(mappedBy = "instance", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
    private ChatMember chatMembership;

    public Instance() {
    }

    public Instance(
        Long id,
        DeploymentUnit unit,
        Deployment deployment,
        Instance parentInstance,
        Configuration configuration,
        String creatorUserId,
        String initiatorUserId,
        Map<String, Object> input,
        Chat chat) {
        this.id = id;
        this.tenant = deployment.getTenant();
        this.organization = configuration.getOrganization();
        this.name = deployment.getName();
        this.version = deployment.getVersion();
        this.versionNumber = deployment.getVersionNumber();
        this.processId = deployment.getProcessId();
        this.processUnitId = deployment.getUnitId();
        this.unitId = unit.getIdentifier();
        this.creatorUserId = creatorUserId;
        this.initiatorUserId = initiatorUserId;
        this.startTs = new Date();
        this.lastActivityTs = new Date();
        this.status = ProcessInstance.STATE_ACTIVE;
        this.active = true;
        this.businessName = name;
        this.root = parentInstance == null;
        this.parentInstance = parentInstance;
        this.rootInstance = parentInstance != null ? parentInstance._rootInstance() : this;
        this.treeInstances = new ArrayList<>();
        this.treeTasks = new ArrayList<>();
        this.treeActiveTasks = new ArrayList<>();
        this.variables = new HashSet<>();
        this.publicVariables = new HashSet<>();
        this.nodes = new ArrayList<>();
        this.activeNodes = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.activeTasks = new ArrayList<>();
        this.commands = new ArrayList<>();
        this.failedCommands = new ArrayList<>();
        this.correlations = new HashSet<>();
        this.accesses = new HashSet<>();
        this.notifications = new ArrayList<>();
        this.configuration = configuration;
        this.io = new InstanceIO(this, input);
        if (chat != null) {
            this.chatMembership = new ChatMember(this, chat);
        }
        addAccess(initiatorUserId, InstanceAccessType.initiator, null);
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

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public String getProcessId() {
        return processId;
    }
        
    public String getProcessUnitId() {
        return processUnitId;
    }
    
    public String getUnitId() {
        return unitId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = StringUtils.abbreviate(businessName, 255);
    }

    public String getBusinessState() {
        return businessState;
    }

    public void setBusinessState(String businessState) {
        this.businessState = StringUtils.abbreviate(businessState, 255);
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public String getInitiatorUserId() {
        return initiatorUserId;
    }

    public Date getStartTs() {
        return startTs;
    }

    public Date getEndTs() {
        return endTs;
    }

    public Date getLastActivityTs() {
        return lastActivityTs;
    }

    public void setLastActivityTs() {
        this.lastActivityTs = new Date();
    }

    public Date getArchiveTs() {
        return archiveTs;
    }

    public int getStatus() {
        return status;
    }

    public boolean isActive() {
        return active;
    }
    
    public boolean isCompleted() {
        return !active;
    }
    
    public Optional<Configuration> getConfiguration() {
        return optionalProxy(configuration);
    }

    public void complete(int status, Map<String, Object> output) {
        
        if (!active) {
            return;
        }
        
        this.status = status;

        active = false;
        endTs = new Date();
        io.setOutput(output);
    }
    
    public void setArchiveTs(Date archiveTs) {

        if (active) {
            throw new IllegalStateException();
        }
        this.archiveTs = archiveTs;   
    }
    
    public boolean isRoot() {
        return root;
    }

    public Long getRootId() {
        return rootInstance.getId();
    }
    
    public Optional<Instance> getRootInstance() {
        return optionalProxy(rootInstance);
    }

    public Long getParentId() {
        return parentInstance != null ? 
            parentInstance.getId() : 
            null;
    }
    
    public Optional<Instance> getParentInstance() {
        return optionalProxy(parentInstance);
    }

    public Set<InstanceVariable> getVariables(boolean publicOnly) {
        return Collections.unmodifiableSet(publicOnly ? publicVariables : variables);
    }

    public Set<InstanceVariable> getVariables(InstanceContext context, boolean publicOnly) {
        return getVariables(publicOnly)
            .stream()
            .filter(v -> v.getContext().equals(context))
            .collect(Collectors.toUnmodifiableSet());
    }

    public boolean hasVariable(InstanceContext context, String name) {
        return variables
            .stream()
            .anyMatch(v -> 
                v.getContext().equals(context) &&
                v.getName().equals(name));
    }

    public InstanceVariable getVariable(InstanceContext context, String name) {
        return variables
            .stream()
            .filter(v -> 
                v.getContext().equals(context) &&
                v.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    public InstanceIO getIO() {
        return io;
    }

    public List<InstanceNode> getNodes(boolean activeOnly) {
        return Collections.unmodifiableList(activeOnly ? 
            activeNodes : 
            nodes);
    }

    public boolean hasNode(Long nodeInstanceId) {
        return nodes
            .stream()
            .anyMatch(n -> n.getNodeInstanceId().equals(nodeInstanceId));
    }

    public InstanceNode getNode(Long nodeInstanceId) {
        return nodes
            .stream()
            .filter(n -> n.getNodeInstanceId().equals(nodeInstanceId))
            .findFirst()
            .orElse(null);
    }

    public List<Task> getTasks(boolean activeOnly) {
        return Collections.unmodifiableList(activeOnly ? 
            activeTasks : 
            tasks);
    }

    public List<Task> getTreeTasks(boolean activeOnly) {
        
        if (getRootInstance().isPresent()) {
            return Collections.unmodifiableList(
                activeOnly ? 
                    getRootInstance().get()._treeActiveTasks() : 
                    getRootInstance().get()._treeTasks());
        } else {
            return Collections.EMPTY_LIST;
        }
    }
    
    public List<Task> getNextTasks(AuthenticationContext context) {
        return activeTasks
            .stream()
            .filter(t -> context.getUserId().equals(t.getSwimlaneActorId()))
            .collect(Collectors.toList());
    }
    
    public List<Instance> getTreeInstances(boolean includeSelf) {

        if (getRootInstance().isPresent()) {
            return getRootInstance().get()._treeInstances()
                .stream()
                .filter(i -> includeSelf || !i.equals(this))
                .collect(Collectors.toUnmodifiableList());
        } else {
            return includeSelf ? 
                List.of(this) : 
                Collections.EMPTY_LIST;
        }
    }
    
    public Set<InstanceAccess> getAccesses() {
        return Collections.unmodifiableSet(accesses);
    }

    public boolean hasAccess(String identity, InstanceAccessType type, Task task) {
        return 
            AuthenticationContext.isAdmin(identity) || 
            accesses
                .stream()
                .anyMatch(a -> 
                    a.getIdentity().equals(identity) && 
                    a.getType().equals(type) &&
                    Objects.equals(a.getTask(), task));
    }
    
    public boolean addAccess(String identity, InstanceAccessType type, Task task) {
        if (hasAccess(identity, type, task)) {
            return false;
        }
        accesses.add(new InstanceAccess(this, identity, type, task));
        return true;
    }

    public Set<InstanceAuthorization> getAuthorizations(AuthenticationContext context) {
        
        Set<InstanceAuthorization> authorizations;
        
        if (getConfiguration().isEmpty()) {
            return context.isAdmin() ? 
                Set.of(InstanceAuthorization.values()) : 
                Collections.EMPTY_SET;
        }
        
        authorizations = new HashSet<>();
        for (InstanceAuthorization authorization : InstanceAuthorization.values()) {
            try {
                if (getConfiguration().get().hasPermission(PermissionType.valueOf(authorization.name()), 
                        context)) {
                    authorizations.add(authorization);
                }
            } catch (IllegalArgumentException e) {
                throw new IncompatibleClassChangeError();
            }
        }
        if (!isActive()) {
            authorizations.remove(InstanceAuthorization.Abort);
        }
        if (!isActive() || getCommands(false).isEmpty()) {
            authorizations.remove(InstanceAuthorization.Retry);
        }
        return Collections.unmodifiableSet(authorizations);
    }
    
    public boolean hasAuthorization(AuthenticationContext context, InstanceAuthorization authorization) {
        return getAuthorizations(context).contains(authorization);
    }
    
    protected List<InstanceCommand> getAllCommands(boolean failedOnly) {
        return Collections.unmodifiableList(failedOnly ? failedCommands : commands);
    }

    public List<InstanceCommand> getCommands(boolean failedOnly) {
        return (failedOnly ? failedCommands : commands)
            .stream()
            .filter(c -> c.getBusinessKey() == null)
            .collect(Collectors.toUnmodifiableList());
    }
    
    public Optional<InstanceCommand> getCommand(Long commandId) {
        return getCommands(false)
            .stream()
            .filter(c -> c.getId().equals(commandId))
            .findFirst();
    }

    public Set<InstanceCorrelation> getCorrelations() {
        return Collections.unmodifiableSet(correlations);
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

    public Optional<ChatMember> getChatMembership() {
        if (getRootInstance().isPresent()) {
            return Optional.ofNullable(getRootInstance().get()._chatMembership());
        } else {
            return Optional.empty();
        }
    }
    
    /**
     * Proxy internal access getters
     */

    protected Instance _rootInstance() {
        return rootInstance;
    }
    
    protected List<Instance> _treeInstances() {
        return treeInstances;
    }

    protected List<Task> _treeTasks() {
        return treeTasks;
    }

    protected List<Task> _treeActiveTasks() {
        return treeActiveTasks;
    }
    
    protected ChatMember _chatMembership() {
        return chatMembership;
    }
}
