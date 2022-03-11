package keysuite.desktop.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

//@Configuration
@Component
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends DefaultMethodSecurityExpressionHandler {

    @Autowired
    public Environment env;

    @Bean
    CustomSecurityExpression securityExpression(){
        return new CustomSecurityExpression(this);
    }

    //public static class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

        //private Environment env;
        //private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();;

        /*public CustomMethodSecurityExpressionHandler(Environment env){
            this.env = env;
            this.trustResolver = new AuthenticationTrustResolverImpl();
        }*/

    @Override
    protected MethodSecurityExpressionRoot createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {

        CustomSecurityExpression root = new CustomSecurityExpression(authentication,this);
        root.setTrustResolver(getTrustResolver());
        root.setRoleHierarchy(getRoleHierarchy());
        root.setDefaultRolePrefix(getDefaultRolePrefix());
        if (invocation!=null){
            root.setInvocation(invocation);
            root.setThis(invocation.getThis());
        }

        return root;
    }
    //}



    /*@Bean
    MethodSecurityExpressionHandler methodSecurityExpressionHandler(){
        return new CustomMethodSecurityExpressionHandler(env);
    }*/

    /*@Autowired
    MethodSecurityExpressionHandler methodSecurityExpressionHandler;


    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return methodSecurityExpressionHandler;
    }*/
}
