package it.kdm.sign.service;

import it.kdm.sign.model.ResultInfo;

import java.util.List;
import java.util.Map;

public interface SignServiceInterface {

	void setConfig(Map<String,String> config );
	ResultInfo richiestaOTP( String otpId, String pin );
	ResultInfo sbloccaOTP(String otpId);
	Map<String,ResultInfo> firmaDocumenti( String alias , String pin, String OTP, Map<String,String> opzioni, List<String> documenti );

}
