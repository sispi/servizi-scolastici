package it.kdm.docer.management.batch.util;

import it.kdm.docer.management.model.Group;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.classes.KeyValuePair;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vaio
 * Date: 02/11/15
 * Time: 13.01
 * To change this template use File | Settings | File Templates.
 */
public class AclUtil {

    /**
     * Trasforma una string nel formato acl_explicit in una lista di coppie (key, value)
     * @param aclExplicit
     * @return
     */
    public static List<KeyValuePair> splitAclExplicit(String aclExplicit) {
        List<KeyValuePair> aclList = new ArrayList<KeyValuePair>();

        String acl = aclExplicit.replace("[", "").replace("]", "");
        String[] couples = acl.split(",");
        for (String couple : couples) {
            String[] coupleParts = couple.split(":");
            if (coupleParts.length == 2)
                aclList.add(new KeyValuePair(coupleParts[0], coupleParts[1]));
        }

        return aclList;
    }

    /**
     * Ricostruisce la stringa acl_explicit da una lista di coppie (group, permission)
     * @param aclList
     * @return
     */
    public static String compactAclExplicit(List<KeyValuePair> aclList) {
        StringBuilder aclExplicit = new StringBuilder("[");

        for (KeyValuePair pair : aclList) {
            aclExplicit.append(pair.getKey());
            aclExplicit.append(":");
            aclExplicit.append(pair.getValue());
            aclExplicit.append(",");
        }

        // Elimino l'ultima virgola
        if (aclExplicit.lastIndexOf(",") != -1)
            aclExplicit.deleteCharAt(aclExplicit.lastIndexOf(","));

        aclExplicit.append("]");

        return aclExplicit.toString();
    }

    /**
     * Costruisce una stringa acl_explicit con lo stesso permesso da una lista di gruppi
     * @param groups
     * @param permission
     * @return
     */
    public static String buildSplitTarget(List<Group> groups, String permission) {
        StringBuilder acl = new StringBuilder("");
        for (Group group : groups) {
            // Se il gruppo contiene già il permesso, questo ha la priorita' su quello passato per parametro
            String perm = !StringUtils.isEmpty(group.getRights()) ? group.getRights() : permission;

            acl.append(group.getValue());
            acl.append(IConst.GROUP);
            acl.append(":");
            acl.append(perm);
            acl.append(",");
        }
        // Elimino l'ultima virgola
        if (acl.lastIndexOf(",") != -1)
            acl.deleteCharAt(acl.lastIndexOf(","));

        return acl.toString();
    }

    /**
     * Trova la permission relativa a un gruppo in una stringa acl_explicit
     * @param group
     * @param aclExplicit
     * @return
     */
    public static String findGroupPermission(String group, String aclExplicit) {
        List<KeyValuePair> aclList = splitAclExplicit(aclExplicit);
        return findGroupPermission(group, aclList);
    }

    /**
     * Trova la permission di un gruppo nella aclList
     * @param group
     * @param aclList
     * @return
     */
    public static String findGroupPermission(String group, List<KeyValuePair> aclList) {
        for (KeyValuePair pair : aclList) {
            if (group.equals(pair.getKey()))
                return pair.getValue();
        }

        return "";
    }

    /**
     * Trova la più ampia permission relativa a un insieme di gruppi in una stringa acl_explicit
     * @param groups
     * @param aclExplicit
     * @return
     */
    public static String findBestPermission(List<Group> groups, String aclExplicit) {

        EnumACLRights permission = EnumACLRights.viewProfile;
        for (Group group : groups) {
            String source = group.getValue().lastIndexOf(IConst.GROUP) == -1 ? group.getValue() + IConst.GROUP : group.getValue();
            String permTemp = findGroupPermission(source, aclExplicit);
            try {
                EnumACLRights permEnum = EnumACLRights.valueOf(permTemp);
                if (permEnum.getCode() < permission.getCode())
                    permission = permEnum;
            }catch(IllegalArgumentException e) {

            }
        }

        return permission.name();
    }

    /**
     * Cancella un gruppo dalla acl_explicit
     * @param group
     * @param aclExplicit
     * @return
     */
    public static String deleteGroup(String group, String aclExplicit) {
        List<KeyValuePair> aclList = splitAclExplicit(aclExplicit);
        List<KeyValuePair> aclListNew = deleteGroup(group, aclList);

        return compactAclExplicit(aclListNew);
    }

    /**
     * Accoda una stringa alla acl_explicit prima del carattere ]
     * @param group
     * @param aclExplicit
     * @return
     */
    public static String addGroup(String group, String aclExplicit) {
        // Se è l'unico gruppo non va messa la virgola
        if ("".equals(aclExplicit) || "[]".equals(aclExplicit))
            return "[" + group + "]";
        else
            return aclExplicit.replace("]", "," + group + "]");
    }

     /**
     * Cancella un gruppo da una aclList
     * @param group
     * @param aclList
     * @return
     */
    public static List<KeyValuePair> deleteGroup(String group, List<KeyValuePair> aclList) {
        List<KeyValuePair> aclListNew = new ArrayList<KeyValuePair>();

        for (KeyValuePair pair : aclList) {
            if (!group.equals(pair.getKey()))
                aclListNew.add(pair);
        }

        return aclListNew;
    }

    /**
     * Sostituisce un gruppo con uno nuovo nella stringa acl_explicit
     * @param oldGroup
     * @param newGroup
     * @param aclExplicit
     * @return
     */
    public static String replaceGroup(String oldGroup, Group newGroup, String aclExplicit) {
        /*if (aclExplicit.indexOf("," + oldGroup) != -1)
            return aclExplicit.replace("," + oldGroup, "," + newGroup);
        else
        // Se è il primo la replace sopra non funziona
            return aclExplicit.replace("[" + oldGroup, "[" + newGroup);
            */

        List<KeyValuePair> pairList = splitAclExplicit(aclExplicit);
        for (KeyValuePair pair : pairList) {
            if (pair.getKey().equals(oldGroup)) {
                pair.setKey(newGroup.getValue());

                // Se e' stata definita una acl nell'xml la forzo
                if (!StringUtils.isEmpty(newGroup.getRights()))
                    pair.setValue(newGroup.getRights());
            }
        }

        return compactAclExplicit(pairList);
    }

    /**
     * Costruisce la mappa dei metadati da passare a Solr
     * @param aclExplicit
     * @return
     */
    public static Map<String, EnumACLRights> calculateAclForSolr(String aclExplicit) {
        List<KeyValuePair> aclList = splitAclExplicit(aclExplicit);
        if (aclList.size() == 0) {
            return new HashMap<String, EnumACLRights>();
        }

        Map<String, EnumACLRights> mapAcl = new HashMap<String, EnumACLRights>();

        for (KeyValuePair acl : aclList) {
            try {
                EnumACLRights enumPerm = EnumACLRights.valueOf(acl.getValue());
                String group = acl.getKey().replace(IConst.GROUP, "").replace(IConst.USER, "");
                mapAcl.put(group, enumPerm);
            }catch(IllegalArgumentException e) {
                System.out.println("Permission " + acl.getValue() + " incorretta");
            }
        }

        return mapAcl;
    }

    /**
     * Mette i separatori alla uniqueLey di una rule
     * @param uniqueKey
     * @return
     */
    public static String makeRuleId(String uniqueKey) {
        return "[" + uniqueKey + "]";
    }

    /**
     * Controlla se l'attributo rights contiene un valore ammesso
     * @param rights
     * @return
     */
    public static boolean checkRightsExists(String rights) {

        if (rights == null)
            return true;

        if ( EnumACLRights.fullAccess.toString().equalsIgnoreCase(rights)
                || EnumACLRights.normalAccess.toString().equalsIgnoreCase(rights)
                || EnumACLRights.readOnly.toString().equalsIgnoreCase(rights) )
            return true;

        return false;
    }
}
