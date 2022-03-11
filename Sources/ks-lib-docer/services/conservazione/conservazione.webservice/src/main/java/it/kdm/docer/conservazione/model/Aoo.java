package it.kdm.docer.conservazione.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("aoo")
public class Aoo
{
    private String to;

    private String body;

    private String where_condition;

    private String period_hours;

    private String subject;

    @XStreamAsAttribute
    private String name;

    public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getWhere_condition() {
		return where_condition;
	}

	public void setWhere_condition(String where_condition) {
		this.where_condition = where_condition;
	}

	public String getPeriod_hours() {
		return period_hours;
	}

	public void setPeriod_hours(String period_hours) {
		this.period_hours = period_hours;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
    public String toString()
    {
        return "ClassPojo [to = "+to+", body = "+body+", where_condition = "+where_condition+", subject = "+subject+", name = "+name+"]";
    }
}
			
