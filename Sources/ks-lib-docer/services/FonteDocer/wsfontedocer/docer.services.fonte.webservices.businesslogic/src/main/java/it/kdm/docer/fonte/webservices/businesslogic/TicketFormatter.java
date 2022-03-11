package it.kdm.docer.fonte.webservices.businesslogic;

import it.kdm.docer.sdk.exceptions.DocerException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class TicketFormatter {

	static String codiceEnte_field = "$codiceEnte$";
	static String userid_field = "$userid$";
	static String dmsticket_field = "$dmsticket$";
	static String format = "codiceEnte:" + codiceEnte_field + ";userid:"
			+ userid_field + ";dmsticket:" + dmsticket_field;
	static String regex = "^codiceEnte:([^;]*);userid:([^;]+);dmsticket:(.+)$";
	
	TicketCipher ticketCypher;
	
	public TicketFormatter() throws DocerException {
		 this.ticketCypher = new TicketCipher();
	}

	public String generateTicket(Ticket ticketObject) throws DocerException {

		String ticket = format.replace(userid_field, ticketObject.getUserid());
		ticket = ticket.replace(codiceEnte_field, ticketObject.getCodiceEnte());
		ticket = ticket.replace(dmsticket_field, ticketObject.getDmsTicket());

		return this.ticketCypher.encryptTicket(ticket);
	}

	public Ticket parseTicket(String ticket) throws DocerException {

		try {
			Pattern pattern = Pattern.compile(regex);

			Ticket ticketObject = new Ticket();

			Matcher matcher = pattern.matcher(this.ticketCypher.decryptTicket(ticket));
			boolean matchFound = matcher.find();
			if (!matchFound){				
				throw new DocerException(-429,"errore formato ticket. Formato atteso " + regex);
			}
			ticketObject.setCodiceEnte(matcher.group(1));
			ticketObject.setUserid(matcher.group(2));
			ticketObject.setDmsTicket(matcher.group(3));

			return ticketObject;
		} catch (PatternSyntaxException pse) {
			throw new DocerException(-550, "errore ticket regex: "
					+ pse.getMessage());
		}
	}
}
