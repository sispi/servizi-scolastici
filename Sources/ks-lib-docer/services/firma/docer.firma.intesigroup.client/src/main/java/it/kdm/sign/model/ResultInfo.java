package it.kdm.sign.model;

import java.io.Serializable;

public class ResultInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int status;
	private String info;
	
	public ResultInfo( ) {
		super();
	}
	
	public ResultInfo( int status, String info ) {
		this.status = status;
		this.info = info;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	@Override
	public String toString() {
		return "ResultInfo [status=" + status + ", info=" + info + "]";
	}
	
}
