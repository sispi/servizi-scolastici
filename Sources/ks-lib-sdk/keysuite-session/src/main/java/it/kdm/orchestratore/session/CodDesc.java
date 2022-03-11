package it.kdm.orchestratore.session;

import java.io.Serializable;

public class CodDesc implements Serializable{
	private String cod;
	private String desc;
	
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public CodDesc(String cod,String desc){
		this.cod = cod;
		this.desc = desc;
	}
}
