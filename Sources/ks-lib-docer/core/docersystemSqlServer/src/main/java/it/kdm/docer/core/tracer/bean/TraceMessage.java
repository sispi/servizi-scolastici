package it.kdm.docer.core.tracer.bean;

import it.kdm.docer.core.tracer.Trace;
import it.kdm.docer.core.tracer.Tracer;

/**
 * Created by antsic on 12/12/14.
 */
public class TraceMessage {

    private String level;
    private Tracer.TYPE type;

    private Trace trace;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Tracer.TYPE getType() {
        return type;
    }

    public void setType(Tracer.TYPE type) {
        this.type = type;
    }

    public Trace getTrace() {
        return trace;
    }

    public void setTrace(Trace trace) {
        this.trace = trace;
    }
}
