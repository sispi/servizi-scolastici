package keysuite.desktop.security;

import it.kdm.orchestratore.session.UserInfo;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.env.Environment;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class MethodSecurityExpressionRoot implements SecurityExpressionOperations,MethodSecurityExpressionOperations {

    public final static ExpressionParser expressionParser = new SpelExpressionParser();

    protected final Authentication authentication;
    protected AuthenticationTrustResolver trustResolver;
    protected RoleHierarchy roleHierarchy;
    private Set<String> roles;
    private String defaultRolePrefix = "ROLE_";
    protected Object filterObject;
    protected Object returnObject;
    protected ScriptEngine engine;
    protected Environment env;
    protected Object target;
    protected MethodInvocation methodInvocation;

    public final boolean permitAll = true;
    public final boolean denyAll = false;
    public final String read = "read";
    public final String write = "write";
    public final String create = "create";
    public final String delete = "delete";
    public final String admin = "administration";
    MethodSecurityConfig config;
    EvaluationContext ctx = null;
    public final String admins = "admins";

    public MethodSecurityExpressionRoot(Authentication authentication, MethodSecurityConfig config) {
        this.config = config;
        this.authentication = authentication;
        this.engine = new ScriptEngineManager().getEngineByName("nashorn");
        this.env = config.env;
        this.target = this;
    }

    public boolean eval(String expression, Object targetObject ){

        StandardEvaluationContext ctx = new StandardEvaluationContext(this);

        if (targetObject instanceof Map) {
            ctx.setVariables((Map) targetObject);
        } /*else if (targetObject instanceof ICIFSObject) {
            ctx.setVariables( (Map) ((ICIFSObject)targetObject).properties );
        }*/

        ctx.setVariable("target",targetObject);

        return (Boolean) expressionParser.parseExpression(expression).getValue(ctx);
    }

    protected Set<String> getAuthoritySet() {
        if (roles == null) {
            Collection<? extends GrantedAuthority> userAuthorities = getAuthentication()
                    .getAuthorities();

            if (roleHierarchy != null) {
                userAuthorities = roleHierarchy
                        .getReachableGrantedAuthorities(userAuthorities);
            }

            roles = AuthorityUtils.authorityListToSet(userAuthorities);
        }

        return roles;
    }

    public boolean isAdmin(){
        return hasRole(admins);
    }

    protected boolean hasAnyAuthorityName(String prefix, String... roles) {
        Set<String> roleSet = getAuthoritySet();

        if (roleSet.contains(defaultRolePrefix+admins))
            return true;

        for (String role : roles) {
            String defaultedRole = getRoleWithDefaultPrefix(prefix, role);
            if (roleSet.contains(defaultedRole)) {
                return true;
            }
            if (defaultedRole.contains("*")){
                defaultedRole = defaultedRole.replaceAll("\\.?\\*",".*");
                for( String userRole : roleSet ){
                    if (userRole.matches(defaultedRole))
                        return true;
                }
            }
        }

        return false;
    }

    public final boolean hasAuthority(String authority) {
        return hasAnyAuthority(authority);
    }

    public final boolean hasAnyAuthority(String... authorities) {
        return hasAnyAuthorityName(null, authorities);
    }

    public final boolean hasRole(String role) {
        return hasAnyRole(role);
    }

    public final boolean hasAnyRole(String... roles) {
        return hasAnyAuthorityName(defaultRolePrefix, roles);
    }

    public final boolean permitAll() {
        return true;
    }

    public final boolean denyAll() {
        return false;
    }

    public final boolean isAnonymous() {
        return trustResolver.isAnonymous(getAuthentication());
    }

    public final boolean isAuthenticated() {
        return !isAnonymous();
    }

    public final boolean isRememberMe() {
        return trustResolver.isRememberMe(getAuthentication());
    }

    public final boolean isFullyAuthenticated() {
        return !trustResolver.isAnonymous(getAuthentication())
                && !trustResolver.isRememberMe(getAuthentication());
    }

    public final Authentication getAuthentication() {
        if (authentication==null){
            return SecurityContextHolder.getContext().getAuthentication();
        } else {
            return authentication;
        }
    }

    public void setTrustResolver(AuthenticationTrustResolver trustResolver) {
        this.trustResolver = trustResolver;
    }

    public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    void setThis(Object target) {
        this.target = target;
    }

    void setInvocation(MethodInvocation methodInvocation){
        this.methodInvocation = methodInvocation;
    }

    @Override
    public void setFilterObject(Object obj) {
        this.filterObject = obj;
    }

    @Override
    public void setReturnObject(Object obj) {
        this.returnObject = obj;
    }

    public String getUsername() {
        return (String) getAuthentication().getPrincipal();
    }

    public UserInfo getUserInfo() {
        return (UserInfo) getAuthentication().getDetails();
    }

    public void setDefaultRolePrefix(String defaultRolePrefix) {
        this.defaultRolePrefix = defaultRolePrefix;
    }

    private static String getRoleWithDefaultPrefix(String defaultRolePrefix, String role) {
        if (role == null) {
            return role;
        }
        if (defaultRolePrefix == null || defaultRolePrefix.length() == 0) {
            return role;
        }
        if (role.startsWith(defaultRolePrefix)) {
            return role;
        }
        return defaultRolePrefix + role;
    }


}


