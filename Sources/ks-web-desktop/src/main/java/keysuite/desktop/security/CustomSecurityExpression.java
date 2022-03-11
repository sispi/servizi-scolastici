package keysuite.desktop.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import keysuite.desktop.DesktopUtils;
import org.springframework.security.core.Authentication;

public class CustomSecurityExpression extends MethodSecurityExpressionRoot {

    public CustomSecurityExpression(Authentication authentication, MethodSecurityConfig env) {
        super(authentication, env);
    }

    public CustomSecurityExpression(MethodSecurityConfig env) {
        super(null, env);
    }

    /*private String getTargetType(Object targetObject){

        if (targetObject==null)
            return null;
        else if (targetObject instanceof SolrDocument)
            return (String) ((SolrDocument)targetObject).get("type");
        else if (targetObject instanceof ICIFSObject)
            return ((ICIFSObject)targetObject).getProperty("type");
        else if (targetObject instanceof Instances)
            return "instance";
        else if (targetObject instanceof Task)
            return "task";
        else if (targetObject instanceof ProcessConfigurationObject)
            return "process";
        else if (targetObject instanceof String)
            return "page";

        throw new KSRuntimeException(Code.H5xx,"Invalid type:"+targetObject.getClass().getSimpleName());
    }*/

    /*private String getRule(String targetType, String rule){
        return env.getProperty("securityExpression."+targetType+"."+rule, env.getProperty("securityExpression."+rule));
    }

    public boolean followRule(Object targetObject, String rule){
        String targetType = getTargetType(targetObject);
        if (targetType!=null) {
            rule = getRule(targetType, rule);
            return eval(rule, targetObject);
        } else {
            return false;
        }

    }*/

    /*public Collection activeRules(Object targetObject){

        Properties props = (Properties) ((StandardServletEnvironment) env).getPropertySources().get("security.properties").getSource();

        String targetType = getTargetType(targetObject);

        List list = new ArrayList();
        list.add("RpermitAll");

        Set<String> rules = new HashSet<>();

        for( String name : props.stringPropertyNames() ){
            if (name.startsWith("securityExpression.")){
                int idx = name.lastIndexOf(".");
                if (name.startsWith("securityExpression."+targetType+".") || idx == name.indexOf(".") ){
                    rules.add(name.substring(idx+1));
                }
            }
        }

        for( String rule : rules ){
            String expr = getRule(targetType, rule);
            if (eval(expr, targetObject))
                list.add("R"+rule);
        }

        return list;
    }

    public boolean followRule(String rule){
        rule = getRule(null,rule);
        return eval(rule,null);
    }*/

    /*private EffectiveRight getEffectiveRight(Object targetObject){
        return getEffectiveRight(targetObject.getClass().getSimpleName().toLowerCase());
    }

    private EffectiveRight getEffectiveRight(String targetType){

        EffectiveRight er;
        switch(targetType){
            case "task":
                er = new TaskRight();
                break;
            case "instance":
            case "instances":
                er = new InstanceRight();
                break;
            case "process":
            case "processconfigurationobject":
                er =new ProcessRight();
                break;
            case "page":
            case "string":
                er = new PageRight();
                break;
            default:
                er = new SolrRight();
                break;
        }
        return er;
    }*/

    public Collection<String> getPermissions(Object targetObject){
        Collection<String> permissions;
        /*if (targetObject instanceof String)
            permissions = PageRight.get( (String) targetObject);
        else*/ if (targetObject instanceof Map)
            permissions = (Collection<String>) ((Map)targetObject).get("permissions");
        else
            permissions = null;

        if (permissions==null)
            permissions = new ArrayList<>();

        return permissions;
    }

    public Collection<String> getPermissions(Object targetId, String targetType){
        return getPermissions(getTargetObject(targetId,targetType));
    }

    private Object getTargetObject(Object targetId, String targetType){
        Object targetObject;
        if ( targetId instanceof String)
            targetObject = targetId;
        else {
            String urlTemplate = env.getProperty("targetUrl."+targetType);
            urlTemplate = String.format(urlTemplate, targetId);

            targetObject = DesktopUtils.GET(urlTemplate);
        }
        return targetObject;
    }

    /*** native methods ***/

    @Override
    public boolean hasPermission(Object targetObject, Object permission) {

        if (isAdmin() || permission==null || permission.equals(permitAll))
            return true;

        if (permission.equals(denyAll))
            return false;

        return getPermissions(targetObject).contains(permission);
    }

    @Override
    public boolean hasPermission(Object targetId, String targetType, Object permission) {
        return hasPermission(getTargetObject(targetId,targetType),permission);
    }
}
