/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import com.google.common.base.Objects;
import it.filippetti.ks.api.bpm.wih.WorkItemSupport;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.*;
import org.drools.persistence.info.WorkItemInfo;
import org.kie.api.runtime.process.WorkItem;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_INSTANCE_NODE",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"instance_id", "nodeInstanceId"})
    },
    indexes = {
        @Index(columnList = "instance_id, active"),
        @Index(columnList = "eventCorrelable, eventId, active")
    }
)
@Sortables(
    defaultSort = "id:ASC",
    value = {
        @Sortable(property = "id", pathExpression = "nodeInstanceId")
    }
)
@Fetchables({
    @Fetchable(property = "input"),
    @Fetchable(property = "output"),
    @Fetchable(property = "context", type = InstanceContext.class),
    @Fetchable(property = "event", type = InstanceEvent.class),
    @Fetchable(property = "variables", type = InstanceVariable.class),
    @Fetchable(property = "variables?publicOnly", type = InstanceVariable.class),
    @Fetchable(property = "variableValues", type = InstanceVariable.class),
    @Fetchable(property = "variableValues?publicOnly", type = InstanceVariable.class),
    @Fetchable(property = "commands", type = InstanceCommand.class),
    @Fetchable(property = "commands?failedOnly", type = InstanceCommand.class),
    @Fetchable(property = "authorizations"),
    @Fetchable(property = "task", type = Task.class),
})
public class InstanceNode extends Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Instance instance;

    @Basic
    @Column(nullable = false)
    private Long nodeInstanceId;
    
    @Embedded
    private InstanceContext context;

    @Basic
    @Column(nullable = false)
    private String nodeId;

    @Basic
    @Column(nullable = false)
    private String nodeName;

    @Basic
    @Column(nullable = false)
    private String nodeType;

    @Embedded
    private InstanceEvent event;
    
    @Basic
    private String connection;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)    
    private Date enterTs;

    @Temporal(TemporalType.TIMESTAMP)
    private Date exitTs;

    @Basic
    @Column(nullable = false)
    private Boolean active;

    @OneToOne(mappedBy = "node", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false) 
    private InstanceNodeIO io;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(
            name = "value", 
            column = @Column(name = "metadata", length = 4000))
    })    
    private JsonString metadata;

    @Basic
    private Long workItemId;

    @Basic
    private String businessKey;
    
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
        name = "workItemId", 
        insertable = false,
        updatable = false,
        referencedColumnName = "workItemId",
        foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private WorkItemInfo workItem;

    @Basic    
    @Column(name = "subprocessInstance_id")
    private Long subprocessInstanceId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
        name = "subprocessInstance_id", 
        insertable = false,
        updatable = false,
        referencedColumnName = "id", 
        foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Instance subprocessInstance;

    @OneToOne(mappedBy = "node", fetch = FetchType.LAZY, optional = true)
    private Task task;

    public InstanceNode() {
    }

    public InstanceNode(
        Instance instance, 
        Long nodeInstanceId, 
        InstanceContext context, 
        String nodeId, 
        String nodeName, 
        String nodeType, 
        String connection,
        InstanceEvent event,
        Object input, 
        Map<String, Object> metadata,
        WorkItem workItem,
        Long subprocessInstanceId){
        this.instance = instance;
        this.nodeInstanceId = nodeInstanceId;
        this.context = context;
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.nodeType = nodeType;
        this.connection = connection;
        this.event = event;
        this.enterTs = new Date();
        this.active = true;
        this.io = new InstanceNodeIO(this, input);
        this.metadata = new JsonString(metadata);
        this.workItemId = workItem != null ? workItem.getId() : null;
        this.businessKey = workItem != null ? WorkItemSupport.getBusinessKey(workItem) : null;
        this.subprocessInstanceId = subprocessInstanceId;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public Instance getInstance() {
        return instance;
    }

    public InstanceContext getContext() {
        return context;
    }

    public Long getNodeInstanceId() {
        return nodeInstanceId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public boolean isEvent() {
        return event != null;
    }
    
    public InstanceEvent getEvent() {
        return event;
    }

    public String getConnection() {
        return connection;
    }

    public Date getEnterTs() {
        return enterTs;
    }

    public Date getExitTs() {
        return exitTs;
    }

    public boolean isActive() {
        return active;
    }
    
    public boolean isCompleted() {
        return !active;
    }
    
    public void complete(Object output) {
        
        if (!active) {
            return;
        }
        
        io.setOutput(output);
        active = false;
        exitTs = new Date();
    }

    public InstanceNodeIO getIO() {
        return io;
    }

    public JsonValue getMetadata() {
        return metadata;
    }

    public boolean isWorkItem() {
        return workItemId != null;
    }

    public Long getWorkItemId() {
        return workItemId;
    }

    public WorkItemInfo getWorkItem() {
        return workItem;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public List<InstanceCommand> getCommands(boolean failedOnly) {
        if (businessKey != null) {
            return instance
                .getAllCommands(failedOnly)
                .stream()
                .filter(e -> Objects.equal(e.getBusinessKey(), businessKey))
                .collect(Collectors.toUnmodifiableList());
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Optional<InstanceCommand> getCommand() {
        return getCommands(false).stream().findFirst();
    }

    public boolean isExecuting() {
        if (businessKey != null) {
            return instance
                .getAllCommands(false)
                .stream()
                .anyMatch(c -> c.isExecuting());
        } else {
            return false;
        }
    }
    
    public Set<InstanceNodeAuthorization> getAuthorizations(AuthenticationContext context) {
        
        Set<InstanceNodeAuthorization> authorizations;
        
        authorizations = new HashSet<>();
        if (context.isAdmin() && isWorkItem() && isActive()) {
            authorizations.add(InstanceNodeAuthorization.Abort);
            authorizations.add(InstanceNodeAuthorization.Complete);
            if (!getCommands(false).isEmpty()) {
                authorizations.add(InstanceNodeAuthorization.Retry);
            }
        }
        return Collections.unmodifiableSet(authorizations);
    }

    public boolean hasAuthorization(AuthenticationContext context, InstanceNodeAuthorization authorization) {
        return getAuthorizations(context).contains(authorization);
    }
    
    public Long getSubprocessInstanceId() {
        return subprocessInstanceId;
    }

    public Instance getSubprocessInstance() {
        return subprocessInstance;
    }

    public boolean isTask() {
        return optionalProxy(task).isPresent();
    }
    
    public Task getTask() {
        return task;
    }
    
    public Set<InstanceVariable> getVariables(boolean publicOnly) {
        return instance.getVariables(context, publicOnly);
    }

    public boolean hasVariable(String name) {
        return instance.hasVariable(context, name);
    }

    public InstanceVariable getVariable(String name) {
        return instance.getVariable(context, name);
    }
}
