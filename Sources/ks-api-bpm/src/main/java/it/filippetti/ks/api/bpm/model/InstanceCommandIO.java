/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import it.filippetti.ks.api.bpm.command.CommandContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.apache.commons.io.input.ClassLoaderObjectInputStream;
import org.hibernate.annotations.Immutable;
import org.kie.api.executor.ExecutionResults;
import org.kie.internal.runtime.manager.InternalRuntimeManager;
import org.kie.internal.runtime.manager.RuntimeManagerRegistry;

/**
 *
 * @author marco.mazzocchetti
 */
@Immutable 
@Entity
@Table(
    name = "RequestInfo"
)
public class InstanceCommandIO extends Identifiable {

    @Id
    private Long id;
    
    @Column(name = "deploymentId")
    private String unitId;

    @MapsId 
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))    
    private InstanceCommand command;

    @Lob
    @Column(name = "requestData")
    private byte[] context;
    
    @Lob
    @Column(name = "responseData")
    private byte[] results;

    public InstanceCommandIO() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public InstanceCommand getNode() {
        return command;
    }

    public CommandContext getContext() throws IOException {
        return new CommandContext(readObject(context, org.kie.api.executor.CommandContext.class));
    }

    public ExecutionResults getResults() throws IOException {
        return readObject(results, ExecutionResults.class);
    }

    private <T> T readObject(byte[] data, Class<T> type) throws IOException {
        
        ObjectInputStream inputStream;
        
        if (data != null) {
            inputStream = null;
            try {
                inputStream = new ClassLoaderObjectInputStream(
                    getClassLoader(), 
                    new ByteArrayInputStream(data));
                return type.cast(inputStream.readObject());
            } catch (ClassNotFoundException | ClassCastException e) {
                throw new IncompatibleClassChangeError();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } else {
            try {
                return type.getConstructor().newInstance();
            } catch (Exception e) {
                throw new IncompatibleClassChangeError();
            }
        }
    }
    
    private ClassLoader getClassLoader() {
        
        ClassLoader classLoader;
        InternalRuntimeManager runtimeManager;
        
        classLoader = Thread.currentThread().getContextClassLoader();
        if (unitId == null) {
            return classLoader;
        }
        runtimeManager = ((InternalRuntimeManager) RuntimeManagerRegistry
            .get()
            .getManager(unitId));
        if (runtimeManager != null && 
            runtimeManager.getEnvironment().getClassLoader() != null) {
            classLoader = runtimeManager.getEnvironment().getClassLoader();
        }
        return classLoader;
    }    
}
