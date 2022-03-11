package it.kdm.docer.providers.solr4.bl.sicurezza;

import com.google.common.base.Strings;
import it.kdm.docer.providers.solr4.SolrBaseUtil;
import it.kdm.docer.providers.solr4.bl.Fields;
import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.classes.*;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.*;
import it.kdm.solr.components.common.SolrClient;
import org.slf4j.Logger;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;

import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by microchip on 05/05/15.
 */
public class AttoriBL extends SolrBaseUtil {

    static Logger log = org.slf4j.LoggerFactory.getLogger(AttoriBL.class.getName());

    public AttoriBL() throws DocerException {
        super();
    }

    public String login(String username, String password, String ente) throws DocerException {

        ILoggedUserInfo adminUser = new LoggedUserInfo();
        adminUser.setUserId("admin");
        adminUser.setCodiceEnte(ente);
        adminUser.setTicket("admin");

        String pwdMd5 = null;
        try {
            pwdMd5 = buildMd5Password(username);
        } catch (NoSuchAlgorithmException e) {
            throw new DocerException(e);
        }

        //STEP 1
        //verifica password modaità MD5
        if (pwdMd5.equals(password))
            return username;

        //STEP 2
        //verifica password su indice
        SolrDocument doc = getSolrObjectByFieldId(adminUser, TYPE_USER, username, Fields.User.USER_ID);
        String userid = (String)doc.get(Fields.User.USER_ID);
        String pwd = (String)doc.get(Fields.User.USER_PASSWORD);

        if (username.equals(userid) && password.equals(pwd))
            return username;

        throw new DocerException("Autenticazione fallita. Controllare userename e password.");
    }

    public void addUsersToGroup(ILoggedUserInfo currentUser, String groupId, List<String> users) throws DocerException {
//        String group;
//        if (groupId.startsWith("aoo_"))
//            group = groupId.replace("aoo_","")+"@aoo";
//        else if (groupId.startsWith("ente_"))
//            group = groupId.replace("ente_","")+"@ente";
//        else
//            group = groupId+"@group";

        Map<String,String> userGroup = getUserOrGroup(currentUser,Collections.singletonList(groupId));
        if (!userGroup.containsKey(groupId))
            throw new DocerException("GroupId non trovato.");

        String group = userGroup.get(groupId);

        String q = "";
        q += SolrClient.Query.makeClause("type", TYPE_USER);
        q += SolrClient.Query.makeClause(Fields.User.USER_ID, users.toArray());
        q += SolrClient.Query.makeNotClause(Fields.User.GROUPS, group);

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(Fields.User.USER_ID);

        SolrDocumentList userList = solrSearchByQuery(currentUser, q, returnProperties, -1, null);

        for (Iterator iterator = userList.iterator(); iterator.hasNext();) {
            SolrDocument rec = (SolrDocument) iterator.next();
            String user_id = (String)rec.getFieldValue(Fields.User.USER_ID);
            addGroupsToUser(currentUser, user_id, Collections.singletonList(groupId));
        }
    }

    public void removeUsersFromGroup(ILoggedUserInfo currentUser, String groupId, List<String> users) throws DocerException {
        Map<String,String> userGroup = getUserOrGroup(currentUser,Collections.singletonList(groupId));
        if (!userGroup.containsKey(groupId))
            throw new DocerException("GroupId non trovato.");

        String group = userGroup.get(groupId);

        Map<String,List<String>> criteria = new HashMap<String, List<String>>();
        criteria.put(Fields.User.USER_ID, users);
        criteria.put(Fields.User.GROUPS, Collections.singletonList(group));
        //TODO: gestire gruppi ente e aoo come sopra
        List<String> returnProps = new ArrayList<String>();
        returnProps.add(Fields.User.USER_ID);

        SolrDocumentList userList = solrSearch(currentUser, TYPE_USER, criteria, null, returnProps, -1, null);

        for (Iterator iterator = userList.iterator(); iterator.hasNext();) {
            Map rec = (Map) iterator.next();
            String user_id = rec.get(Fields.User.USER_ID).toString();
            removeGroupsFromUser(currentUser, user_id, Collections.singletonList(groupId));
        }
    }

    public void addGroupsToUser(ILoggedUserInfo currentUser, String userId, List<String> groups) throws DocerException {
        SolrDocument user = getSolrObjectByFieldId(currentUser, TYPE_USER, userId, Fields.User.SYSTEM_ID);
        String solrId = (String)user.getFieldValue(Fields.User.SOLR_ID);
        Collection<Object> groupList = user.getFieldValues(Fields.User.GROUPS);

        SolrInputDocument metadata = new SolrInputDocument();

        if (groups == null)
            return;

        if (groupList==null)
            groupList = new ArrayList<Object>();

        Map<String,String> userGroup = getUserOrGroup(currentUser,groups);

        for (String g : groups) {

//            String group;
//            if (g.startsWith("aoo_"))
//                group = g.replace("aoo_","")+"@aoo";
//            else if (g.startsWith("ente_"))
//                group = g.replace("ente_","")+"@ente";
//            else
//                group = g+"@group";

            String group = userGroup.get(g);
            if (!groupList.contains(group)) {
                groupList.add(group);
            }
        }

        metadata.addField(Fields.User.GROUPS, groupList);

        solrUpdate(currentUser, solrId, metadata);
    }

    public void removeGroupsFromUser(ILoggedUserInfo currentUser, String userId, List<String> groups) throws DocerException {
        SolrDocument user = getSolrObjectByFieldId(currentUser, TYPE_USER, userId, Fields.User.SYSTEM_ID);
        String solrId = (String)user.getFieldValue(Fields.User.SOLR_ID);
        Collection<Object> groupList = user.getFieldValues(Fields.User.GROUPS);

        if (groupList==null)
            return;

        Map<String,String> userGroup = getUserOrGroup(currentUser,groups);

        for (String g : groups) {
            groupList.remove(userGroup.get(g));
//            groupList.remove(g+ "@group");
//            groupList.remove(g.replace("aoo_","")+ "@aoo");
//            groupList.remove(g.replace("ente_","")+ "@ente");
        }

        SolrInputDocument metadata = new SolrInputDocument();
        if (groupList.size()==0)
            metadata.setField(Fields.User.GROUPS, Collections.singletonMap("set",null));
        else
            metadata.setField(Fields.User.GROUPS, groupList);

        solrUpdate(currentUser, solrId, metadata);
    }

    public IUserInfo getUser(ILoggedUserInfo currentUser, String userId) throws DocerException {
        IUserInfo info = null;
        try {
            SolrDocument item = getSolrObjectByFieldId(currentUser, TYPE_USER, userId, Fields.User.SYSTEM_ID);
            info = convertSolrDocumentToUserInfo(item);
        } catch (DocerException de) {
            if (de.getErrNumber()!=404) {
                throw de;
            }
        }
        return info;
    }

    public IGroupInfo getGroup(ILoggedUserInfo currentUser, String groupId) throws DocerException {
        IGroupInfo info = null;
        Map<String,List<String>> criteria = new HashMap<String, List<String>>();
        criteria.put(Fields.Group.SYSTEM_ID,Arrays.asList(groupId));

        List<IGroupProfileInfo> group = searchGroups(currentUser, criteria);
        if (group.size()==0)
            return null;

        info = new GroupInfo();
        info.setProfileInfo(group.get(0));

        String q = "";
        q += SolrClient.Query.makeClause("groups", "*"+groupId+"@*");

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(Fields.User.USER_ID);

        SolrDocumentList userList = solrSearchByQuery(currentUser, q, returnProperties, -1, null);
        List<String> users = new ArrayList<String>();
        for (SolrDocument u : userList) {
            users.add((String)u.getFieldValue(Fields.User.USER_ID));
        }

        info.setUsers(users);

//        try {
//            SolrDocument item = getSolrObjectByFieldId(currentUser, TYPE_GROUP, groupId, Fields.Group.SYSTEM_ID);
//            info = convertSolrDocumentToGroupInfo(item);
//        } catch (DocerException de) {
//            if (de.getErrNumber()!=404) {
//                throw de;
//            }
//        }

        return info;
    }

    public void createUser(ILoggedUserInfo currentUser, IUserProfileInfo userNewInfo) throws DocerException {

        if (isNullOrEmpty(userNewInfo.getUserId()))
            throw new DocerException("Impossible creare l'Utente. UserId non impostato.");

        String solrId = getSolrId(userNewInfo, currentUser.getCodiceEnte());

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

//        metadata.addField(Fields.User.COD_ENTE, currentUser.getCodiceEnte());

        if (userNewInfo.getUserId()!=null)
            metadata.addField(Fields.User.USER_ID, userNewInfo.getUserId());

        try {
            if (!Strings.isNullOrEmpty(userNewInfo.getUserPassword()))
                metadata.addField(Fields.User.USER_PASSWORD, userNewInfo.getUserPassword());
            else
                metadata.addField(Fields.User.USER_PASSWORD, buildMd5Password(userNewInfo.getUserId()));
        } catch (NoSuchAlgorithmException ne) {
            throw new DocerException(ne);
        }
        if (userNewInfo.getNetworkAlias()!=null)
            metadata.addField(Fields.User.NETWORK_ALIAS, userNewInfo.getNetworkAlias());

        if (userNewInfo.getFullName()!=null)
            metadata.addField(Fields.User.FULL_NAME, userNewInfo.getFullName());

        if (userNewInfo.getEmailAddress()!=null)
            metadata.addField(Fields.User.EMAIL_ADDRESS, userNewInfo.getEmailAddress());

        if (userNewInfo.getFirstName()!=null)
            metadata.addField(Fields.User.FIRST_NAME, userNewInfo.getFirstName());

        if (userNewInfo.getLastName()!=null)
            metadata.addField(Fields.User.LAST_NAME, userNewInfo.getLastName());

        if (userNewInfo.getEnabled()!=null && !userNewInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
            metadata.addField(Fields.User.ENABLED, userNewInfo.getEnabled().equals(EnumBoolean.TRUE));

        userNewInfo.getExtraInfo().put(Fields.Group.COD_ENTE,currentUser.getCodiceEnte());

        metadata = buildExtraInfo(metadata, userNewInfo.getExtraInfo());
        //TODO: rimappare errore se già esiste???

        solrInsert(currentUser, solrId, metadata);
    }

    public void createGroup(ILoggedUserInfo currentUser, IGroupProfileInfo groupNewInfo) throws DocerException {

        //impedire la presenza del prefisso "ADMINS_" non ammessa in creazione
//        if (groupNewInfo.getGroupId().startsWith("ADMINS_"))
//            throw new DocerException("Impossibile creare il gruppo. Parola chiave 'ADMINNS_' non ammessa all'interno del groupId.");

        //impedire la creazione di un gruppo all'interno di un gruppo con prefisso "ADMINS_"
        if (groupNewInfo.getParentGroupId()!=null && groupNewInfo.getParentGroupId().startsWith("ADMINS_"))
            throw new DocerException("Impossibile creare il gruppo. Non è ammesso creare un gruppo all'interno di un gruppo di amministrazione.");

        if (isNullOrEmpty(groupNewInfo.getGroupId()))
            throw new DocerException("Impossible creare il gruppo. GroupId non impostato.");

        String solrId = getSolrId(groupNewInfo, currentUser.getCodiceEnte());

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

//        metadata.addField(Fields.Group.COD_ENTE, currentUser.getCodiceEnte());

        if (groupNewInfo.getGroupId()!=null)
            metadata.addField(Fields.Group.GROUP_ID, groupNewInfo.getGroupId());

        if (groupNewInfo.getParentGroupId()!=null)
            metadata.addField(Fields.Group.PARENT_GROUP_ID, groupNewInfo.getParentGroupId());

        if (groupNewInfo.getGroupName()!=null)
            metadata.addField(Fields.Group.GROUP_NAME, groupNewInfo.getGroupName());

        if (groupNewInfo.getEnabled()!=null && !groupNewInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
            metadata.addField(Fields.Group.ENABLED, groupNewInfo.getEnabled().equals(EnumBoolean.TRUE));

        groupNewInfo.getExtraInfo().put(Fields.Group.COD_ENTE,currentUser.getCodiceEnte());

        metadata = buildExtraInfo(metadata, groupNewInfo.getExtraInfo());

        if (!isNullOrEmpty(groupNewInfo.getParentGroupId())) {
            SolrDocument doc = getSolrObjectByFieldId(currentUser, "*", groupNewInfo.getParentGroupId(), Fields.Group.GROUP_ID);
            String parentSolrId = (String)doc.get(Fields.Folder.SOLR_ID);
            metadata.setField("parent", parentSolrId);
        }

        solrInsert(currentUser, solrId, metadata);
    }

    public NamedList<?> updateUser(ILoggedUserInfo currentUser, String userId, IUserProfileInfo userNewInfo) throws DocerException {

//        String newCodiceEnte = userNewInfo.getExtraInfo().get(it.kdm.docer.sdk.Constants.user_cod_ente);
//        String newCodiceAOO = userNewInfo.getExtraInfo().get(it.kdm.docer.sdk.Constants.user_cod_aoo);
//        String newCodiceFiscale = userNewInfo.getExtraInfo().get(it.kdm.docer.sdk.Constants.user_cod_fiscale);


        if (userNewInfo.getUserId()!=null && !userNewInfo.getUserId().equals("") && !userId.equalsIgnoreCase(userNewInfo.getUserId())) {
            throw new DocerException("Non è possibile cambiare la userid.");
        }

        //setta la userId in userNewInfo per calcolare il solrId
        userNewInfo.setUserId(userId);
        String solrId = getSolrId(userNewInfo, currentUser.getCodiceEnte());

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

//        metadata.addField(Fields.User.COD_ENTE, currentUser.getCodiceEnte());

        if (userNewInfo.getUserId()!=null)
            metadata.addField(Fields.User.USER_ID, userNewInfo.getUserId());
        //TODO: generare MD5 della password prima di storicizzare su solr
        try {
            if (!Strings.isNullOrEmpty(userNewInfo.getUserPassword()))
                metadata.addField(Fields.User.USER_PASSWORD, userNewInfo.getUserPassword());
            else
                metadata.addField(Fields.User.USER_PASSWORD, buildMd5Password(userNewInfo.getUserId()));
        } catch (NoSuchAlgorithmException ne) {
            throw new DocerException(ne);
        }

        if (userNewInfo.getNetworkAlias()!=null)
            metadata.addField(Fields.User.NETWORK_ALIAS, userNewInfo.getNetworkAlias());

        if (userNewInfo.getFullName()!=null)
            metadata.addField(Fields.User.FULL_NAME, userNewInfo.getFullName());

        if (userNewInfo.getEmailAddress()!=null)
            metadata.addField(Fields.User.EMAIL_ADDRESS, userNewInfo.getEmailAddress());

        if (userNewInfo.getFirstName()!=null)
            metadata.addField(Fields.User.FIRST_NAME, userNewInfo.getFirstName());

        if (userNewInfo.getLastName()!=null)
            metadata.addField(Fields.User.LAST_NAME, userNewInfo.getLastName());

        if (userNewInfo.getEnabled()!=null && !userNewInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
            metadata.addField(Fields.User.ENABLED, userNewInfo.getEnabled().equals(EnumBoolean.TRUE));

        metadata = buildExtraInfo(metadata, userNewInfo.getExtraInfo());

        return solrUpdate(currentUser, solrId, metadata);
    }

    public NamedList<?> updateGroup(ILoggedUserInfo currentUser, String groupId, IGroupProfileInfo groupNewInfo) throws DocerException {

        if (!isNullOrEmpty(groupNewInfo.getGroupId()) && !groupId.equalsIgnoreCase(groupNewInfo.getGroupId())) {
            throw new DocerException("Non è possibile cambiare il groupid.");
        }

        IGroupProfileInfo groupInfo = new GroupProfileInfo();
        groupInfo.setGroupId(groupId);

        SolrDocument doc = getSolrObjectByFieldId(currentUser, "*", groupId, Fields.Group.GROUP_ID);
        String solrId = (String)doc.get(Fields.Folder.SOLR_ID);

        //String solrId = getSolrId(groupInfo, currentUser.getCodiceEnte());

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

//        metadata.addField(Fields.User.COD_ENTE, currentUser.getCodiceEnte());

        if (groupNewInfo.getGroupId()!=null)
            metadata.addField(Fields.Group.GROUP_ID, groupNewInfo.getGroupId());

        if (groupNewInfo.getParentGroupId()!=null)
            metadata.addField(Fields.Group.PARENT_GROUP_ID, groupNewInfo.getParentGroupId());

        if (groupNewInfo.getGroupName()!=null)
            metadata.addField(Fields.Group.GROUP_NAME, groupNewInfo.getGroupName());

        if (groupNewInfo.getEnabled()!=null && !groupNewInfo.getEnabled().equals(EnumBoolean.UNSPECIFIED))
            metadata.addField(Fields.Group.ENABLED, groupNewInfo.getEnabled().equals(EnumBoolean.TRUE));

        metadata = buildExtraInfo(metadata, groupNewInfo.getExtraInfo());

        return solrUpdate(currentUser, solrId, metadata);
    }

    public List<IUserProfileInfo> searchUsers(ILoggedUserInfo currentUser, Map<String, List<String>> searchCriteria) throws DocerException {
        log.debug("----------------------------------------------------------------------------------------------");
        log.debug("METODO: searchUsers");
        log.debug("----------------------------------------------------------------------------------------------");

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(Fields.User.FIRST_NAME);
        returnProperties.add(Fields.User.LAST_NAME);
        returnProperties.add(Fields.User.EMAIL_ADDRESS);
        returnProperties.add(Fields.User.ENABLED);
        returnProperties.add(Fields.User.FULL_NAME);
        returnProperties.add(Fields.User.USER_ID);
        returnProperties.add(Fields.User.USER_PASSWORD);
        returnProperties.add(Fields.User.COD_AOO);
        returnProperties.add(Fields.User.COD_ENTE);
        returnProperties.add(Fields.User.NETWORK_ALIAS);
        returnProperties.add(Fields.User.USER_PREFIX+"*");

        SolrDocumentList results = solrSearch(currentUser, TYPE_USER, searchCriteria, null, returnProperties, -1, null);

        //converte solr results in DataTable<String>
        List<IUserProfileInfo> userList = this.convertSolrToUserList(results);

        return userList;
    }

    public List<IGroupProfileInfo> searchGroups(ILoggedUserInfo currentUser, Map<String, List<String>> searchCriteria) throws DocerException {
        List<String> returnProperties1 = new ArrayList<String>();
        returnProperties1.add(Fields.Group.GROUP_ID);
        returnProperties1.add(Fields.Group.GROUP_NAME);
        returnProperties1.add(Fields.Group.PARENT_GROUP_ID);
        returnProperties1.add(Fields.Group.GRUPPO_STRUTTURA);
        returnProperties1.add(Fields.Group.ENABLED);
        returnProperties1.add(Fields.Group.COD_ENTE);
        returnProperties1.add(Fields.Group.COD_AOO);
        returnProperties1.add(Fields.Group.GROUP_PREFIX+"*");

        //prima query per group / ente / aoo
        String q = "";
        List<String> typeList = new ArrayList<String>();
        typeList.add(TYPE_ENTE);
        typeList.add(TYPE_AOO);
        typeList.add(TYPE_GROUP);

        String filter = " +((type:(ente aoo) AND location:%s) OR type:group) ";

        q += String.format(filter,this.getLocation()); //SolrClient.Query.makeClause("type", typeList.toArray());

        for (String prop : searchCriteria.keySet()) {
            q += SolrClient.Query.makeClause(prop, searchCriteria.get(prop).toArray());
        }

        SolrDocumentList groupList = solrSearchByQuery(currentUser, q, returnProperties1, -1, null);

        List<String> returnProperties2 = new ArrayList<String>();
        returnProperties2.add("GROUP_ID:ADMIN_GROUP_ID");
        returnProperties2.add("GROUP_NAME:ADMIN_GROUP_NAME");
        returnProperties2.add(Fields.Group.PARENT_GROUP_ID);
        returnProperties2.add(Fields.Group.GRUPPO_STRUTTURA);
        returnProperties2.add(Fields.Group.ENABLED);
        returnProperties2.add(Fields.Group.COD_ENTE);
        returnProperties2.add(Fields.Group.COD_AOO);
        returnProperties2.add(Fields.Group.GROUP_PREFIX+"*");

        //seconda query per i gruppi admin
        q = "";
        typeList = new ArrayList<String>();
        typeList.add(TYPE_ENTE);
        typeList.add(TYPE_AOO);

        q += SolrClient.Query.makeClause("type", typeList.toArray());

        List<String> groupIdList = searchCriteria.remove("GROUP_ID");
        if (groupIdList==null)
            searchCriteria.put("ADMIN_GROUP_ID",Collections.singletonList("*"));
        else
            searchCriteria.put("ADMIN_GROUP_ID",groupIdList);

        List<String> groupNameIdList = searchCriteria.remove("GROUP_NAME");
        if (groupNameIdList==null)
            searchCriteria.put("ADMIN_GROUP_NAME",Collections.singletonList("*"));
        else
            searchCriteria.put("ADMIN_GROUP_NAME",groupNameIdList);

        for (String prop : searchCriteria.keySet()) {
            q += SolrClient.Query.makeClause(prop, searchCriteria.get(prop).toArray());
        }

        q += SolrClient.Query.makeClause("location", this.getLocation());

        SolrDocumentList admingroupList = solrSearchByQuery(currentUser, q, returnProperties2, -1, null);

        //converte solr results in DataTable<String>
        List<IGroupProfileInfo> outgroupList = this.convertSolrToGroupList(groupList);
        List<IGroupProfileInfo> outadmingroupList = this.convertSolrToGroupList(admingroupList);

        outgroupList.addAll(outadmingroupList);

        return outgroupList;
    }


}
