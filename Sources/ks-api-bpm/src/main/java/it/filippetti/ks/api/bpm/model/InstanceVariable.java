/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_INSTANCE_VARIABLE",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"instance_id", "contextId", "contextInstanceId", "name"})
    },
    indexes = {
        @Index(columnList = "instance_id, public_"),
    }
)
@Fetchables({
    @Fetchable(property = "value")
})
public class InstanceVariable extends Auditable {
    
    public static interface Name {
        public static final String TENANT                   = "INSTANCE_ENTE";
        public static final String ORGANIZATION             = "INSTANCE_AOO";
        public static final String INSTANCE_ID              = "instanceId";
        public static final String ROOT_INSTANCE_ID         = "primaryInstanceId";
        public static final String CORRELATION_SUBSCRIPTION =  "correlationSubscription";
        public static final String CORRELATION_KEY          = "__ckValue";
        public static final String CONTEXT_ID               = "contextId";
        public static final String CONTEXT_INSTANCE_ID      = "contextInstanceId";
        public static final String CONTEXT_KEY              = "contextKey";
        public static final String ACTOR_ID                 = "ActorId";
        public static final String CREATOR_USER_ID          = "creatorUserId";
        public static final String BUSINESS_NAME            = "descrizione";
        public static final String BUSINESS_STATE           = "businessState";
        public static final String SWIMLANES                = "lanes";
        public static final String HUMAN_TOKEN              = "humanToken";        
        public static final String SYSTEM_TOKEN             = "systemToken";
        public static final String LAST_WORKITEM_RESULTS    = "$";        
        public static final String LAST_WORKITEM_STATE      = "$state";        
        public static final String CHAT_ID                  = "chatId";        
        public static final String PROFILE                  = "profile";                
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Instance instance;

    @Embedded
    private InstanceContext context;
    
    @Basic
    @Column(nullable = false)
    private String name;

    @Basic
    @Column(name = "public_", nullable = false)    
    private Boolean public_;
    
    @Basic
    @Column(nullable = false)    
    private Boolean business;

    @Basic
    @Column(name="in_", nullable = false)    
    private Boolean in;

    @Basic
    @Column(name="out_", nullable = false)     
    private Boolean out;
    
    @Basic
    @Column(nullable = false)    
    private Boolean config;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false) 
    private Date lastModifiedTs;
    
    @OneToOne(mappedBy = "variable", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private InstanceVariableValue value;

    @Basic
    @Column(name = "value", length = 512)
    private String simpleValue;
    
    public InstanceVariable() {
    }

    public InstanceVariable(
        Instance instance, 
        InstanceContext context,
        String name,
        boolean business, 
        boolean in, 
        boolean out, 
        boolean config) {
        this.instance = instance;
        this.context = context;
        this.name = name;
        this.business = business;
        this.in = in;
        this.out = out;   
        this.config = config;
        this.public_ = business | in | out | config;
        this.lastModifiedTs = new Date();
        this.value = new InstanceVariableValue(this);
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

    public String getName() {
        return name;
    }

    public boolean isBusiness() {
        return business;
    }

    public boolean isPublic() {
        return public_;
    }

    public boolean isIn() {
        return in;
    }

    public boolean isOut() {
        return out;
    }

    public boolean isConfig() {
        return config;
    }

    @Override
    public Date getLastModifiedTs() {
        return lastModifiedTs;
    }

    public JsonValue getValue() {
        return value.getValue();
    }

    public String getSimpleValue() {
        return simpleValue;
    }

    public void update(Object value) throws IllegalArgumentException {
        
        this.value.update(value);
        
        lastModifiedTs = new Date();
        
        if (value != null && BeanUtils.isSimpleValueType(value.getClass())) {
            if (value instanceof Date) {
                try {
                    simpleValue = DateTimeFormatter.ISO_INSTANT.format(((Date) value).toInstant());
                } catch (DateTimeException e) {
                    throw new IllegalArgumentException();
                }
            } else  {
                simpleValue = StringUtils.abbreviate(value.toString(), 512);
            }
        } else {
            simpleValue = null;
        }
    }
}
