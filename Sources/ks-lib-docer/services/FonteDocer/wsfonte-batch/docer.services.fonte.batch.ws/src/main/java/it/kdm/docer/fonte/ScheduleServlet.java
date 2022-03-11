package it.kdm.docer.fonte;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ScheduleServlet extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String token = null;
	
	public ScheduleServlet(){
		if(token==null){
			
			WSFonteBatch ws;
			try {
				ws = new WSFonteBatch();
				String token = ws.login("admin", "admin", "");
				ws.scheduleBatchPopolamentoFonte(token);
				ws.scheduleBatchPopolamentoRaccoglitore(token);
				
			}
			catch (FonteBatchException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void init() throws ServletException {

		if(token==null){
		
			WSFonteBatch ws;
			try {
				ws = new WSFonteBatch();
				String token = ws.login("admin", "admin", "");
				ws.scheduleBatchPopolamentoFonte(token);
				ws.scheduleBatchPopolamentoRaccoglitore(token);
				
			}
			catch (FonteBatchException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		super.init();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
	
//	public void init() throws ServletException{
//	
//		if(token==null){
//		
//			WSFonteBatch ws;
//			try {
//				ws = new WSFonteBatch();
//				String token = ws.login("admin", "admin", "");
//				ws.scheduleBatchPopolamentoFonte(token);
//				ws.scheduleBatchPopolamentoRaccoglitore(token);
//				
//			}
//			catch (FonteBatchException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
//		
//		super.init();
//		
//	}
//	
//	public void destroy(){
//		if(token!=null){
//		
//			WSFonteBatch ws;
//			try {
//				ws = new WSFonteBatch();
//				ws.unscheduleBatchPopolamentoFonte(token);
//				ws.scheduleBatchPopolamentoRaccoglitore(token);
//			}
//			catch (FonteBatchException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
//		
//		super.destroy();
//		
//	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}
    
}
