package it.kdm.docer.conservazione.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("section")
public class Section
{
    
	private String log_path;

    @XStreamAsAttribute
	private String cronExpr;

    @XStreamAsAttribute
    private String name;

    @XStreamAsAttribute
    private String className;

    @XStreamAsAttribute
    private Boolean isCliJob;

    private int rows_displayed;

    private String cli_params;
    
    @XStreamImplicit(itemFieldName = "ente")
    private List<Ente> ente = new ArrayList<Ente>();

    
    public String getLog_path() {
		return log_path;
	}

	public void setLog_path(String log_path) {
		this.log_path = log_path;
	}


	public String getCronExpr() {
		return cronExpr;
	}

	public void setCronExpr(String cronExpr) {
		this.cronExpr = cronExpr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Boolean getIsCliJob() {
		return isCliJob;
	}

	public void setIsCliJob(Boolean isCliJob) {
		this.isCliJob = isCliJob;
	}

	public int getRows_displayed() {
		return rows_displayed;
	}

	public void setRows_displayed(int rows_displayed) {
		this.rows_displayed = rows_displayed;
	}

    public String getCli_params() {
		return cli_params;
	}

	public void setCli_params(String cli_params) {
		this.cli_params = cli_params;
	}

	public List<Ente> getEnte() {
		return ente;
	}

	public void setEnte(List<Ente> ente) {
		this.ente = ente;
	}

	@Override
    public String toString()
    {
        return "ClassPojo [log_path = "+log_path+", cronExpr = "+cronExpr+", name = "+name+", className = "+className+", rows_displayed = "+rows_displayed+", ente = "+ente+"]";
    }
}
			
