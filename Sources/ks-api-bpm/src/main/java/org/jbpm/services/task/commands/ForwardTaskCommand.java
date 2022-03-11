/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.services.task.commands;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.kie.api.runtime.Context;
import org.kie.api.task.model.Group;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.internal.task.api.TaskModelProvider;

/**
 *
 * @author marco.mazzocchetti
 */
@XmlRootElement(name = "forward-task-command")
@XmlAccessorType(XmlAccessType.NONE)
public class ForwardTaskCommand extends UserGroupCallbackTaskCommand<Void> {

    private static final long serialVersionUID = -3291367442760747824L;
    
    public ForwardTaskCommand() {
    }

    public ForwardTaskCommand(long taskId, String userId, String targetEntityId) {
        this.taskId = taskId;
        this.userId = userId;
        this.targetEntityId = targetEntityId;
    }

    @Override
    public Void execute(Context context) {
        
        TaskContext taskContext;
        
        taskContext = (TaskContext) context;
        doCallbackUserOperation(userId, taskContext, true);
        doCallbackIdentityOperation(targetEntityId, taskContext);
        groupIds = doUserGroupCallbackOperation(userId, null, taskContext);
        taskContext.set("local:groups", groupIds);

        taskContext.getTaskInstanceService().forward(taskId, userId, targetEntityId);

        return null;
    }
    
    protected OrganizationalEntity doCallbackIdentityOperation(
        String identity, TaskContext taskContext) {

        if (identity != null && taskContext.getUserGroupCallback().existsUser(identity)) {
            return addUserFromCallbackOperation(identity, taskContext);
        }
        if (identity != null && taskContext.getUserGroupCallback().existsGroup(identity)) {
            return addGroupFromCallbackOperation(identity, taskContext);
        }        
        throw new IllegalArgumentException(String.format(
            "Identity %s was not found in callback %s", 
            identity, 
            taskContext.getUserGroupCallback()));
    }        
    
    protected Group addGroupFromCallbackOperation(String groupId, TaskContext context) {
        
        Group group;
        
        group = context.getPersistenceContext().findGroup(groupId);
        if (group == null) {
            group = TaskModelProvider.getFactory().newGroup(groupId);
            persistIfNotExists(group, context);
        }    
        return group;
    }   
}
