package it.kdm.docer.fonte.webservices.businesslogic;

public class Ticket {

	String userid = "";
	String codiceEnte = "";
	String dmsticket = "";
	
	public Ticket(){			
	}

	public Ticket(String codiceEnte, String userid, String dmsticket){
		
		this.codiceEnte = codiceEnte;
		this.userid = userid;
		this.dmsticket = dmsticket;
	}
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getCodiceEnte() {
		return codiceEnte;
	}
	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}
	public String getDmsTicket() {
		return dmsticket;
	}
	public void setDmsTicket(String dmsticket) {
		this.dmsticket = dmsticket;
	}
	
	
}
