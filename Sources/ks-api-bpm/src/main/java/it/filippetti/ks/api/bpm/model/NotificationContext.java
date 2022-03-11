/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author marco.mazzocchetti
 */
public interface NotificationContext {

    public String getTenant();
    public String getOrganization();
    public Instance getInstance();
    public Task getTask();
    public Set<String> getDefaultTags();
    public Map<String, Object> getVariables();
    
    public static NotificationContext of(Instance instance, Map<String, Object> variables) {
        return new NotificationContext() {
            @Override
            public String getTenant() {
                return instance.getTenant();
            }

            @Override
            public String getOrganization() {
                return instance.getOrganization();
            }

            @Override
            public Instance getInstance() {
                return instance;
            }

            @Override
            public Task getTask() {
                return null;
            }
            
            @Override
            public Set<String> getDefaultTags() {
                return Set.of(
                    String.format("%s:%s", "organization", instance.getOrganization()),
                    String.format("%s:%s", "processId", instance.getProcessId()),
                    String.format("%s:%d", "instanceId", instance.getId()));
            }            

            @Override
            public Map<String, Object> getVariables() {
                return Collections.unmodifiableMap(variables);
            }
        };
    }

    public static NotificationContext of(Task task, Map<String, Object> variables) {
        return new NotificationContext() {
            @Override
            public String getTenant() {
                return task.getTenant();
            }

            @Override
            public String getOrganization() {
                return task.getOrganization();
            }

            @Override
            public Instance getInstance() {
                return task.getInstance();
            }

            @Override
            public Task getTask() {
                return task;
            }

            @Override
            public Set<String> getDefaultTags() {
                return Set.of(
                    String.format("%s:%s", "organization", task.getOrganization()),
                    String.format("%s:%s", "processId", task.getInstance().getProcessId()),
                    String.format("%s:%d", "instanceId", task.getInstance().getId()),
                    String.format("%s:%d", "taskId", task.getId()));
            }            

            @Override
            public Map<String, Object> getVariables() {
                return Collections.unmodifiableMap(variables);
            }
        };
    }

    public static NotificationContext of(AuthenticationContext context, Map<String, Object> variables) {
        return new NotificationContext() {
            @Override
            public String getTenant() {
                return context.getTenant();
            }

            @Override
            public String getOrganization() {
                return context.getOrganization();
            }
            
            @Override
            public Instance getInstance() {
                return null;
            }

            @Override
            public Task getTask() {
                return null;
            }

            @Override
            public Set<String> getDefaultTags() {
                return Collections.EMPTY_SET;
            }            

            @Override
            public Map<String, Object> getVariables() {
                return Collections.unmodifiableMap(variables);
            }
        };
    }
}
