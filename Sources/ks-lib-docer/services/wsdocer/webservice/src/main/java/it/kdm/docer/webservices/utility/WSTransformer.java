package it.kdm.docer.webservices.utility;

import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.EnumSearchOrder;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.businesslogic.BusinessLogic;
import it.kdm.docer.sdk.classes.SearchItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WSTransformer {

	
	public static Map<String, String> toMap1(KeyValuePair[] kvpArray){
		
		Map<String, String> metadata = new HashMap<String, String>();
		
		if(kvpArray!=null)
			for(KeyValuePair kvp : kvpArray){			
				//if(kvp==null || kvp.getKey() == null || kvp.getValue()==null)
				if(kvp==null || kvp.getKey() == null)
					continue;
				//metadata.put(kvp.getKey().toUpperCase(), kvp.getValue());
				metadata.put(kvp.getKey(), kvp.getValue());
			}
		
		return metadata;
	}

    public static List<Map<String, String>> toList1(SearchItem[] searchItemsArray){

        List<Map<String, String>> listMetadata = new ArrayList<Map<String, String>>();
        for (SearchItem item : searchItemsArray){
            Map<String, String> metadata = new HashMap<String, String>();

            if(item.getMetadata()!=null) {
                for (KeyValuePair kvp : item.getMetadata()) {
                    //if(kvp==null || kvp.getKey() == null || kvp.getValue()==null)
                    if (kvp == null || kvp.getKey() == null)
                        continue;
                    //metadata.put(kvp.getKey().toUpperCase(), kvp.getValue());
                    metadata.put(kvp.getKey(), kvp.getValue());
                }
                listMetadata.add(metadata);
            }
        }


        return listMetadata;
    }
	
	public static KeyValuePair[] toArray1(Map<String, String> metadataMap){
		
		List<KeyValuePair> list = new ArrayList<KeyValuePair>();
		
		if(metadataMap!=null)
			for(String propName : metadataMap.keySet()){
				if(propName == null)
					continue;
				KeyValuePair kvp = new KeyValuePair(propName, metadataMap.get(propName));
				list.add(kvp);
			}
		
		return list.toArray(new KeyValuePair[0]);
	}

	public static Map<String, EnumACLRights> toMap2(KeyValuePair[] kvpArray){
		
		Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();
		if(kvpArray!=null)
			for(KeyValuePair kvp : kvpArray){			
				//if(kvp==null || kvp.getKey() == null || kvp.getValue()==null)
				if(kvp==null || kvp.getKey() == null)
					continue;
				acls.put(kvp.getKey(),BusinessLogic.getEnumACLRights(kvp.getValue()));
			}
		
		return acls;
	}
	
	public static KeyValuePair[] toArray2(Map<String, EnumACLRights> acls){
		
		List<KeyValuePair> aclsList = new ArrayList<KeyValuePair>();
		
		if(acls!=null)
			for(String id : acls.keySet()){
				if(id == null || acls.get(id) == null)
					continue;
				int aclCode = acls.get(id).getCode();
				KeyValuePair kvp = new KeyValuePair(id, String.valueOf(aclCode));
				aclsList.add(kvp);
			}
		
		return aclsList.toArray(new KeyValuePair[0]);
	}
	
	public static Map<String, List<String>> toMap3(KeyValuePair[] kvpArray){
		
		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
		
		if(kvpArray!=null) {
			
			for(KeyValuePair kvp : kvpArray) {			
				
				//if(kvp==null || kvp.getKey() == null || kvp.getValue()==null)
				if(kvp==null || kvp.getKey() == null)
					continue;
				
				if ( !searchCriteria.containsKey(kvp.getKey().toUpperCase()) )
					searchCriteria.put(kvp.getKey().toUpperCase(), new ArrayList<String>());
				
				List<String> criteria = searchCriteria.get(kvp.getKey().toUpperCase());

				criteria.add(kvp.getValue());
				
				//searchCriteria.put(kvp.getKey().toUpperCase(), criteria);
				searchCriteria.put(kvp.getKey(), criteria);
			}
		}
		
		return searchCriteria;
	}
	

	public static Map<String, EnumSearchOrder> toMap4(KeyValuePair[] kvpArray) {

		Map<String, EnumSearchOrder> orderby = null;

		if (kvpArray != null) {

			orderby = new HashMap<String, EnumSearchOrder>();

			for (KeyValuePair kvp : kvpArray) {

				if (kvp==null || kvp.getKey() == null || kvp.getValue() == null)
					continue;

				if (kvp.getValue().equalsIgnoreCase("ASC")) {
					//orderby.put(kvp.getKey().toUpperCase(),EnumSearchOrder.ASC);
					orderby.put(kvp.getKey(),EnumSearchOrder.ASC);
					continue;
				}
				if (kvp.getValue().equalsIgnoreCase("DESC")) {
					//orderby.put(kvp.getKey().toUpperCase(),EnumSearchOrder.DESC);
					orderby.put(kvp.getKey(),EnumSearchOrder.DESC);
					continue;
				}
			}
		}

		return orderby;
	}

	
	
	
	public static List<String> toList(String[] array){
		List<String> list = new ArrayList<String>();
		if(array!=null){
			for(String key : array) {
				list.add(key);				
			}
		}
		
		return list;
	}
}
