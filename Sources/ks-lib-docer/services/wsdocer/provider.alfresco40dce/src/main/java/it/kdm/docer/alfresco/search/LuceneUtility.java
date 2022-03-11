package it.kdm.docer.alfresco.search;

import it.kdm.docer.alfresco.model.DocerModel;
import it.kdm.docer.alfresco.provider.CustomItemProps;
import it.kdm.docer.alfresco.provider.MetaMap;
import it.kdm.docer.alfresco.provider.MetaProperty;
import it.kdm.docer.sdk.EnumSearchOrder;
import it.kdm.docer.sdk.exceptions.DocerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.alfresco.webservice.util.Constants;
import org.apache.commons.lang.StringUtils;

import com.google.common.base.Joiner;

public class LuceneUtility {

    private static Pattern orderByPattern = Pattern.compile("^ *([^ =]+) *= *(ASC|DESC) *$");
    private static String INTEGER_REGEX = "^[+-]?[0-9]+$";

    private static String $ORDER_BY = "$ORDER_BY";
    private static String $MAX_RESULTS = "$MAX_RESULTS";
    private static String $KEYWORDS = "$KEYWORDS";

    private static String ORDERBY_ALFRESCO_FIELD_TYPE = "PREFIX";

    private static String range_regex = "^ *\\[(.+) +TO +(.+)\\] *$";
    static Pattern rangePattern = Pattern.compile(range_regex);

    // ******* LUCENE QUERY BULDING ******* //

    private static MetaMap METAMAPPING = new MetaMap();
    private static MetaMap METAMAPPING_USER = new MetaMap();
    private static MetaMap METAMAPPING_GROUP = new MetaMap();
//    private static boolean query_debug = false;

    public static void initialize(boolean queryDebug, MetaMap metamapping, MetaMap metamapping_user, MetaMap metamapping_group, String orderby_alfresco_field_type) throws DocerException {

	if (metamapping == null) {
	    throw new DocerException("cannot set metamapping as null");
	}
	if (metamapping_user == null) {
	    throw new DocerException("cannot set metamapping_user as null");
	}
	if (metamapping_group == null) {
	    throw new DocerException("cannot set metamapping_group as null");
	}
	if (orderby_alfresco_field_type == null) {
	    throw new DocerException("cannot set orderby_alfresco_field_type as null");
	}

	METAMAPPING = metamapping;
	METAMAPPING_USER = metamapping_user;
	METAMAPPING_GROUP = metamapping_group;
	ORDERBY_ALFRESCO_FIELD_TYPE = orderby_alfresco_field_type;
	
//	query_debug = queryDebug;
    }

    public static String buildAlfrescoDocumentsSearchQueryString(Map<String, List<String>> searchCriteria, List<String> keywords) throws DocerException {
	String search = DocerModel.SEARCH_DOCUMENT_QUERY;

	String criteria = buildAlfrescoSearchQueryString("document", searchCriteria, keywords);

	String luceneQuery = search + " " + criteria;

//	if(query_debug)
//	    System.out.println(luceneQuery);
	return luceneQuery;
    }

    public static String buildAlfrescoFoldersQueryString(Map<String, List<String>> searchCriteria, boolean searchOnlyDocerFolders) throws DocerException {

	String search = DocerModel.SEARCH_QUERY_CM_FOLDER;

	if (searchOnlyDocerFolders) {
	    search = DocerModel.SEARCH_QUERY_DOCER_FOLDER;
	}

	String criteria = buildAlfrescoSearchQueryString("folder", searchCriteria, null);
	// String luceneParam = "";
	//
	// if (!criteria.equals(""))
	// luceneParam = "+(" + criteria + ")";
	//
	// String luceneQuery = search + " " + luceneParam;

	String luceneQuery = search + " " + criteria;

//	if(query_debug)
//	    System.out.println(luceneQuery);

	return luceneQuery;
    }

    public static String buildAlfrescoGroupsSearchQueryString(Map<String, List<String>> searchCriteria) throws DocerException {

	String search = DocerModel.SEARCH_QUERY_GROUP_ROOT;

	String criteria = buildAlfrescoSearchQueryString("group", searchCriteria, null);
	// String luceneParam = "";
	//
	// if (!criteria.equals(""))
	// luceneParam = "+(" + criteria + ")";

	// String luceneQuery = search + " " + luceneParam;

	String luceneQuery = search + " " + criteria;

//	if(query_debug)
//	    System.out.println(luceneQuery);

	return luceneQuery;

    }

    public static String buildAlfrescoUsersSearchQueryString(Map<String, List<String>> searchCriteria) throws DocerException {

	// searchCriteria = resolveMultivalues(searchCriteria);
	// Create a query object
	String search = DocerModel.SEARCH_QUERY_USER_ROOT;

	String criteria = buildAlfrescoSearchQueryString("user", searchCriteria, null);
	// String luceneParam = "";
	//
	// if (!criteria.equals(""))
	// luceneParam = "+(" + criteria + ")";
	//
	// String luceneQuery = search + " " + luceneParam;

	String luceneQuery = search + " " + criteria;

//	if(query_debug)
//	    System.out.println(luceneQuery);

	return luceneQuery;
    }

    public static String buildAlfrescoAnagraficheQueryString(String type, Map<String, List<String>> searchCriteria) throws DocerException {

	String tipo = type;
	
	StringBuilder search = new StringBuilder();

	search.append("+TYPE:\"");
	search.append(type);
	search.append("\" ");

	String criteria = buildAlfrescoSearchQueryString(tipo, searchCriteria, null);

	String luceneQuery = search + " " + criteria;

//	if(query_debug)
//	    System.out.println(luceneQuery);

	return luceneQuery;
    }

    private static String buildAlfrescoSearchQueryString(String tipo, Map<String, List<String>> searchCriteria, List<String> keywords) throws DocerException {

	String querySearch = "";

	List<String> values = null;
	if (searchCriteria != null) {

	    for (String key : searchCriteria.keySet()) {

		if (StringUtils.isEmpty(key)) {
		    continue;
		}

		if (key.startsWith("$")) {
		    continue;
		}

		boolean isGroupIdCriteria = false;

		MetaProperty mp = null;

		if (tipo.equalsIgnoreCase("user")) {
		    mp = METAMAPPING_USER.getMetaPropertyFromBusinessLogicName(key);
		}
		else if (tipo.equalsIgnoreCase("group")) {
		    mp = METAMAPPING_GROUP.getMetaPropertyFromBusinessLogicName(key);

		    if (key.equalsIgnoreCase(it.kdm.docer.sdk.Constants.group_group_id)) {
			isGroupIdCriteria = true;
		    }
		}

		if (mp == null) {
		    mp = METAMAPPING.getMetaPropertyFromBusinessLogicName(key);
		}

		if (mp == null) {

		    // if (exceptionIfNotMapped) {
		    // throw new
		    // DocerException("buildAlfrescoSearchQueryString: il metadato "
		    // + key + " non e' mappato in configurazione");
		    // }
		    //
		    // continue; // se non e' mappato

		    mp = new MetaProperty("#" + key, "{http://www.docarea.it/model/content/1.0}UNMAPPED_" + key);
		}

		values = searchCriteria.get(key);

		if (values == null) {
		    continue;
		}

		String orCriteria = "";

		for (String singoloValore : values) {

			if (StringUtils.isEmpty(singoloValore)) {

				// bug fix ricerche ottimizzate per parent
				if (mp.getAlfShortPropName().startsWith("docarea:parent")) {
				    orCriteria += "@" + mp.getAlfShortPropName().replaceAll(":", "\\\\:") + ":\"\" ";
				}
				else {
				    orCriteria += "@" + mp.getAlfShortPropName().replaceAll(":", "\\\\:") + ":\"\" ISNULL:\"" + mp.getAlfShortPropName() + "\" ";
				}

				continue;
			    }
			

		    if (singoloValore.equals("*")) {

			orCriteria += "@" + mp.getAlfShortPropName().replaceAll(":", "\\\\:") + ":" + singoloValore + " ";
			continue;
		    }

		    // se e' un range
		    if (singoloValore.matches(range_regex)) {

			Matcher m = rangePattern.matcher(singoloValore);
			if (m.matches()) {
			    String minVal = m.group(1).trim();
			    String maxVal = m.group(2).trim();

			    if (mp.getIsUppercase()) {
				minVal = minVal.toUpperCase();
				maxVal = maxVal.toUpperCase();
			    }
			    else if (mp.getIsLowercase()) {
				minVal = minVal.toLowerCase();
				maxVal = maxVal.toLowerCase();
			    }

			    if (isGroupIdCriteria) {
				// aggiungiamo il prefix alfresco dei group_id
				minVal = Constants.GROUP_PREFIX + minVal;
				maxVal = Constants.GROUP_PREFIX + maxVal;
			    }

			    orCriteria += "@" + mp.getAlfShortPropName().replaceAll(":", "\\\\:") + ":[" + minVal + " TO " + maxVal + "] "; // RANGE
			    continue;
			}

		    }

		    if (mp.getIsUppercase()) {
			singoloValore = singoloValore.toUpperCase();
		    }
		    else if (mp.getIsLowercase()) {
			singoloValore = singoloValore.toLowerCase();
		    }

		    if (isGroupIdCriteria) {
			singoloValore = Constants.GROUP_PREFIX + singoloValore;
		    }

		    orCriteria += "@" + mp.getAlfShortPropName().replaceAll(":", "\\\\:") + ":\"" + escapeLuceneQueryValue(singoloValore) + "\" ";
		}

		if (StringUtils.isNotEmpty(orCriteria)) {
		    querySearch += " +(" + orCriteria.replaceAll(" +$", "") + ")";
		}

	    }

	}

	String keywordSearch = "";
	if (keywords != null && keywords.size() > 0) {
	    for (String keyword : keywords) {
		if (keyword.equals("")) {
		    continue;
		}
		// le keyword sono in AND
		keywordSearch += " +ALL:\"" + escapeLuceneQueryValue(keyword) + "\" ";
	    }
	}

	List<String> extraKeywords = extractKeywordsFromSearchCriteria(searchCriteria);
	if (extraKeywords != null && extraKeywords.size() > 0) {
	    for (String keyword : extraKeywords) {
		if (keyword.equals("")) {
		    continue;
		}
		// le keyword sono in AND
		keywordSearch += " +ALL:\"" + escapeLuceneQueryValue(keyword) + "\" ";
	    }
	}

	if (!keywordSearch.equals("")) {
	    querySearch += " +(" + keywordSearch.replaceAll(" +$", "") + ")";
	}

	return querySearch;
    }

    public static String buildAlfrescoSearchOrderBy(String type, Map<String, EnumSearchOrder> orderby) {

	String orderCriteria = "";
	if (orderby == null)
	    return orderCriteria;

	boolean prefixed = true;
	if (ORDERBY_ALFRESCO_FIELD_TYPE.equalsIgnoreCase("QNAME")) {
	    prefixed = false;
	}

	for (String key : orderby.keySet()) {

	    String nameField = null;
	    EnumSearchOrder ascOrDesc = orderby.get(key);

	    MetaProperty mp = null;

	    if (type.equalsIgnoreCase("user")) {
		mp = METAMAPPING_USER.getMetaPropertyFromBusinessLogicName(key);
	    }
	    else if (type.equalsIgnoreCase("group")) {
		mp = METAMAPPING_GROUP.getMetaPropertyFromBusinessLogicName(key);
	    }

	    if (mp == null) {
		mp = METAMAPPING.getMetaPropertyFromBusinessLogicName(key);
	    }

	    if (mp == null) {
		mp = new MetaProperty("#" + key, "{http://www.docarea.it/model/content/1.0}UNMAPPED_" + key);
	    }

	    nameField = mp.getAlfShortPropName();
	    if (!prefixed) {
		nameField = mp.getAlfFullPropName();
	    }

	    if (nameField != null) {
		orderCriteria += "@" + nameField + "=" + ascOrDesc + ";";
	    }

	}
	
//	if(query_debug)	    
//	    System.out.println(orderCriteria);

	return orderCriteria;
    }

    public static int extractMaxResultsFromSearchCriteria(Map<String, List<String>> searchCriteria) throws DocerException {

	if (searchCriteria == null) {
	    return -1;
	}

	List<String> maxResultsCriteria = searchCriteria.get($MAX_RESULTS);
	if (maxResultsCriteria == null) {
	    return -1;
	}

	int max = -1;

	for (String val : maxResultsCriteria) {

	    if (val.matches(INTEGER_REGEX)) {
		try {
		    max = Integer.parseInt(val);
		}
		catch (NumberFormatException nfe) {

		}
	    }

	}

	if (max < 0) {
	    max = -1;
	}

	return max;
    }

    public static Map<String, EnumSearchOrder> extractOrderByFromSearchCriteria(Map<String, List<String>> searchCriteria) throws DocerException {

	if (searchCriteria == null) {
	    return null;
	}

	List<String> orderbyCriteria = searchCriteria.get($ORDER_BY);
	if (orderbyCriteria == null) {
	    return null;
	}
	// deve essere 1 solo splittabile senno' perdiamo l'ordinamento dei
	// criteri
	String value = orderbyCriteria.get(0);
	if (StringUtils.isEmpty(value)) {
	    return null;
	}

	Map<String, EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();

	Matcher m = null;
	String[] values = value.split(" *; *");
	String propName = "";
	EnumSearchOrder ascOrDesc = EnumSearchOrder.ASC;

	for (String crit : values) {
	    m = orderByPattern.matcher(crit);
	    if (m.matches()) {
		propName = m.group(1);
		ascOrDesc = EnumSearchOrder.ASC;
		if (m.group(2).equalsIgnoreCase("DESC")) {
		    ascOrDesc = EnumSearchOrder.DESC;
		}
		orderby.put(propName, ascOrDesc);
	    }

	}

	if (orderby.size() > 0) {
	    return orderby;
	}

	return null;

    }

    private static List<String> extractKeywordsFromSearchCriteria(Map<String, List<String>> searchCriteria) throws DocerException {

	if (searchCriteria == null) {
	    return null;
	}

	List<String> keywordsCriteria = searchCriteria.get($KEYWORDS);
	if (keywordsCriteria == null) {
	    return null;
	}
	// ne prendiamo 1 solo per coerenza con il recupero dell'order-by dai
	// searchCriteria
	String value = keywordsCriteria.get(0);
	if (StringUtils.isEmpty(value)) {
	    return null;
	}

	List<String> keywords = new ArrayList<String>();

	String[] values = value.split(" *; *");

	for (String val : values) {
	    val = val.trim();
	    if (StringUtils.isEmpty(val)) {
		continue;
	    }
	    keywords.add(val);

	}

	if (keywords.size() > 0) {
	    return keywords;
	}

	return null;

    }

    private static String escapeChars1 = "[\\.\\\\\\+\\-\\!\\(\\)\\:\\^\\]\\[\\{\\}\\~\\?\\\"]";

    public static String escapeLuceneQueryValue(String userInput) {
	// String escapeChars1 =
	// "[\\.\\\\\\+\\-\\!\\(\\)\\:\\^\\]\\[\\{\\}\\~\\*\\?\\\"]";

	// proibiti \:*"?

	String escaped = userInput;
	escaped = escaped.replaceAll(escapeChars1, "\\\\$0");

	return escaped;
    }
    // ******* LUCENE QUERY BULDING ******* //

}
