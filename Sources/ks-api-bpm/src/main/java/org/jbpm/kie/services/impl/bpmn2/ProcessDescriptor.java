/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.kie.services.impl.bpmn2;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.jbpm.kie.services.impl.model.ProcessAssetDesc;
import org.jbpm.services.api.model.NodeDesc;
import org.jbpm.services.api.model.TimerDesc;
import org.jbpm.services.api.model.UserTaskDefinition;
import org.kie.api.definition.process.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a package level class that is used by different BPMN2 handlers ( in
 * this package) to store information about a BPMN2 process.
 */
public class ProcessDescriptor implements Serializable {

    private static final long serialVersionUID = -6304675827486128074L;

    private static final Logger logger = LoggerFactory.getLogger(ProcessDescriptor.class);

    private ProcessAssetDesc process;
    private Set<NodeDesc> nodes = new HashSet<>();
    private Set<TimerDesc> timers = new HashSet<>();
    private Map<String, UserTaskDefinition> tasks = new HashMap<String, UserTaskDefinition>();
    private Map<String, Map<String, String>> taskInputMappings = new HashMap<String, Map<String, String>>();
    private Map<String, Map<String, String>> taskOutputMappings = new HashMap<String, Map<String, String>>();
    private Map<String, String> inputs = new HashMap<String, String>();
    private Map<String, Collection<String>> taskAssignments = new HashMap<String, Collection<String>>();
    private Map<String, String> itemDefinitions = new HashMap<String, String>();
    private Map<String, String> serviceTasks = new HashMap<String, String>();
    private Map<String, String> globalItemDefinitions = new HashMap<String, String>();

    private Collection<String> reusableSubProcesses = new HashSet<String>(1);
    private Set<String> referencedClasses = new HashSet<String>(1);
    private Set<String> unqualifiedClasses = new HashSet<String>(1);
    private Set<String> referencedRules = new HashSet<String>(1);

    private Collection<String> signals = Collections.emptySet();
    private Collection<String> globals = Collections.emptySet();

    private Queue<String> unresolvedReusableSubProcessNames = new ArrayDeque<String>();

    public ProcessDescriptor() {
    }

    public void setProcess(ProcessAssetDesc process) {
        this.process = process;
    }

    public boolean hasUnresolvedReusableSubProcessNames() {
        return !unresolvedReusableSubProcessNames.isEmpty();
    }

    public void resolveReusableSubProcessNames(Collection<Process> deploymentProcesses) {
        synchronized (unresolvedReusableSubProcessNames) {
            for (Process process : deploymentProcesses) {
                reusableSubProcesses.add(process.getId());
            }
        }
    }

    public ProcessAssetDesc getProcess() {
        return process;
    }

    public Map<String, UserTaskDefinition> getTasks() {
        return tasks;
    }

    public Map<String, Map<String, String>> getTaskInputMappings() {
        return taskInputMappings;
    }

    public Map<String, Map<String, String>> getTaskOutputMappings() {
        return taskOutputMappings;
    }

    public Map<String, String> getInputs() {
        return inputs;
    }

    public Map<String, Collection<String>> getTaskAssignments() {
        return taskAssignments;
    }

    public Map<String, String> getItemDefinitions() {
        return itemDefinitions;
    }

    public Map<String, String> getServiceTasks() {
        return serviceTasks;
    }

    public Map<String, String> getGlobalItemDefinitions() {
        return globalItemDefinitions;
    }

    public Collection<String> getReusableSubProcesses() {
        return reusableSubProcesses;
    }

    public void addReusableSubProcessName(String processName) {
        synchronized (unresolvedReusableSubProcessNames) {
            unresolvedReusableSubProcessNames.add(processName);
        }
    }

    public Set<String> getReferencedClasses() {
        return referencedClasses;
    }

    public Set<String> getUnqualifiedClasses() {
        return unqualifiedClasses;
    }

    public Set<String> getReferencedRules() {
        return referencedRules;
    }

    public Collection<String> getSignals() {
        return signals;
    }

    public void setSignals(Collection<String> signals) {
        this.signals = signals;
    }

    public Collection<String> getGlobals() {
        return globals;
    }

    public void setGlobals(Collection<String> globals) {
        this.globals = globals;
    }

    public Set<NodeDesc> getNodes() {
        return nodes;
    }

    public Set<TimerDesc> getTimers() {
        return timers;
    }

    public void clear() {
        process = null;
        tasks.clear();
        taskInputMappings.clear();
        taskOutputMappings.clear();
        inputs.clear();
        taskAssignments.clear();
        reusableSubProcesses.clear();
        itemDefinitions.clear();
        serviceTasks.clear();
        globalItemDefinitions.clear();
        referencedClasses.clear();
        referencedRules.clear();
        nodes.clear();
        timers.clear();
    }

    public ProcessDescriptor clone() {

        ProcessDescriptor cloned = new ProcessDescriptor();

        cloned.process = this.process.copy();
        cloned.tasks = new HashMap<String, UserTaskDefinition>(this.tasks);
        cloned.taskInputMappings = new HashMap<String, Map<String, String>>(this.taskInputMappings);
        cloned.taskOutputMappings = new HashMap<String, Map<String, String>>(this.taskOutputMappings);
        cloned.inputs = new HashMap<String, String>(this.inputs);
        cloned.taskAssignments = new HashMap<String, Collection<String>>(this.taskAssignments);
        cloned.reusableSubProcesses = new HashSet<String>(this.reusableSubProcesses);
        cloned.itemDefinitions = new HashMap<String, String>(this.itemDefinitions);
        cloned.serviceTasks = new HashMap<String, String>(this.serviceTasks);
        cloned.globalItemDefinitions = new HashMap<String, String>(this.globalItemDefinitions);
        cloned.referencedClasses = new HashSet<String>(this.referencedClasses);
        cloned.referencedRules = new HashSet<String>(this.referencedRules);
        cloned.unqualifiedClasses = new HashSet<String>(this.unqualifiedClasses);
        cloned.signals = new HashSet<String>(this.signals);
        cloned.globals = new HashSet<String>(this.globals);
        cloned.nodes = new HashSet<>(this.nodes);
        cloned.timers = new HashSet<>(this.timers);

        cloned.unresolvedReusableSubProcessNames = new ArrayDeque<String>(this.unresolvedReusableSubProcessNames);

        return cloned;
    }
}
