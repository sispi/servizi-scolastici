package it.kdm.docer.alfresco.provider;

import it.kdm.docer.alfresco.model.DocerModel;

import org.alfresco.webservice.util.Constants;
import org.apache.axis.utils.StringUtils;

public class MetaProperty {

	private static final String MV = "MV";
	private static final String UC = "uppercase";
	private static final String LC = "lowercase";
	private String alfFullPropName = null;
	private String alfShortPropName = null;
	private String blPropName = null;
	private boolean isMultivalue = false;
	private boolean isUppercase = false;
	private boolean isLowercase = false;
	private boolean isValid = false;
	
	
	public MetaProperty(String key, String value){
	
		try {
			if (StringUtils.isEmpty(key)){
				return;
			}
				
			if (StringUtils.isEmpty(value)){
				return;
			}
				
			this.blPropName = key.replaceAll("^#", "").toUpperCase();

			String[] values = value.split(";");
			if (values.length < 1) {
				return; // controllo stupido
			}

			this.alfFullPropName = values[0];

			this.alfShortPropName = this.alfFullPropName
					.replace("{" + Constants.NAMESPACE_CONTENT_MODEL + "}",
							"cm:")
					.replace("{" + Constants.NAMESPACE_SYSTEM_MODEL + "}",
							"sys:")
					.replace("{" + DocerModel.DOCAREA_NAMESPACE_CONTENT_MODEL + "}", 
							DocerModel.MY_MODEL_PREFIX + ":");

			// se hanno indicato il multivalue
			if (values.length > 1) {
				if (values[1].equalsIgnoreCase(MV)) {
					this.isMultivalue = true;
				}
			}
			// se hanno indicato che e' uppercase o lowercase
			if (values.length > 2) {
				if (values[2].equalsIgnoreCase(UC)) {
					this.isUppercase = true;
				}
				else
					if (values[2].equalsIgnoreCase(LC)) {
						this.isLowercase = true;
					}
			}

			this.isValid = true;
		} catch (Exception e) {
			this.isValid = false;
		}
	}
	
	public String getAlfFullPropName(){
		return this.alfFullPropName;
	}
	public String getAlfShortPropName(){
		return this.alfShortPropName;
	}
	public String getBlPropName(){
		return this.blPropName;
	}
	public boolean getIsMultivalue(){
		return this.isMultivalue;
	}
	public boolean getIsUppercase(){
		return this.isUppercase;
	}
	public boolean getIsLowercase(){
		return this.isLowercase;
	}
	public boolean isValid(){
		return this.isValid;
	}
}
