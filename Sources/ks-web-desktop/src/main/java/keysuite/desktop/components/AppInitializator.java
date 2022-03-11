package keysuite.desktop.components;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;
import it.kdm.orchestratore.session.Session;
import keysuite.freemarker.ResourceTemplateLoader;
import javax.annotation.PostConstruct;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.desktop.DesktopApplication;
import keysuite.desktop.DesktopResourceLoader;
import keysuite.desktop.DesktopUtils;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.desktop.freemarker.FreeMarkerViewExtended;
import keysuite.desktop.htmlview.HtmlViewResolver;
import keysuite.docer.client.User;
import keysuite.docer.sdk.APIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.List;
import java.util.Map;

@Component
public class AppInitializator implements EnvironmentAware {

    //public static final String STATIC_ROOT = "static";
    //public static final String TEMPLATES_ROOT = "templates";
    //public static final String RESOURCES_PATH = "/resources";
    //public static final String MODELS_ROOT = "models";

    @Autowired
    private Environment env;

    @Autowired
    GenericApplicationContext applicationContext;

    @Bean
    public DesktopResourceLoader resourceLoader(){
        return DesktopApplication.loader;
    }

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource=new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasenames("messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setDefaultLocale(null);
        return messageSource;
    }

    /*@Bean
    public ProxyFactoryBean templateProxyEngine() throws ScriptException {
        ProxyFactoryBean pfb = new ProxyFactoryBean();
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        //engine.eval(new InputStreamReader(ResourceUtils.getResourceNoExc(STATIC_ROOT+"/vue-nashorn.js")));
        //engine.eval(new InputStreamReader(ResourceUtils.getResourceNoExc(STATIC_ROOT+"/renderContext.js")));

        pfb.setTarget(engine);
        pfb.setInterfaces(ScriptEngine.class, Compilable.class, Invocable.class);
        return  pfb;
    }*/

    /*@Autowired
    ProxyFactoryBean templateProxyEngine;

    @Bean
    ScriptEngine templateEngine(){
        return (ScriptEngine) templateProxyEngine.getObject();
    }*/

    @Bean
    HtmlViewResolver viewResolver(){
        HtmlViewResolver resolver = new HtmlViewResolver(env);

        resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);

        resolver.setSuffix( env.getProperty("spring.vue.suffix",".html") );
        resolver.setPrefix( env.getProperty("spring.vue.prefix","templates/") );
        //resolver.defaultConfiguration().setDefaultPreRender( Boolean.parseBoolean(env.getProperty("spring.vue.default-prerender","true")) );
        //resolver.defaultConfiguration().setDefaultClientInject( Boolean.parseBoolean(env.getProperty("spring.vue.default-client-inject","true")) );
        //resolver.defaultConfiguration().setDefaultMasterView( env.getProperty("spring.vue.default-view","master.ftl") );
        //resolver.defaultConfiguration().useCache( Boolean.parseBoolean(env.getProperty("spring.vue.cache","true")) );
        //resolver.defaultConfiguration().usePreRender( Boolean.parseBoolean(env.getProperty("spring.vue.prerender","true")) );

        return resolver;
    }

    /*@Bean
    VueView.Configuration vueConfiguration(){
        VueView.Configuration conf = new VueView.Configuration(env);
        //conf.setDefaultPreRender(true);
        //conf.setDefaultClientInject(true);
        //conf.setDefaultMasterView("master.ftl");
        return conf;
    }*/

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(){

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_0);

        ResourceTemplateLoader ml = new ResourceTemplateLoader( "templates/", cfg);

        String masterTemplateName = env.getProperty("spring.freemarker.masterTemplate","master.ftl");

        try {
            cfg.setSharedVariable("masterTemplate",masterTemplateName);
        } catch (TemplateModelException e) {
            throw new RuntimeException(e);
        }

        cfg.setTemplateLoader(ml);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setInterpolationSyntax(Configuration.DOLLAR_INTERPOLATION_SYNTAX);


        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();


        //freeMarkerConfigurer.setTemplateLoaderPath("classpath:/templates"); //defines the classpath location of the freemarker templates

        freeMarkerConfigurer.setConfiguration(cfg);

        freeMarkerConfigurer.setDefaultEncoding("UTF-8"); // Default encoding of the template files
        return freeMarkerConfigurer;
    }

    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
        viewResolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
        viewResolver.setCache(false);
        viewResolver.setPrefix("");
        viewResolver.setSuffix(".ftl");
        viewResolver.setContentType("text/html; charset=utf-8");
        viewResolver.setViewClass(FreeMarkerViewExtended.class);
        return viewResolver;
    }

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new ErrorPageRegistrar() {
            @Override
            public void registerErrorPages(ErrorPageRegistry registry) {
                ErrorPage errorPage = new ErrorPage(env.getProperty("server.error.path","/error"));
                registry.addErrorPages(errorPage);
            }
        };
    }

    @Autowired
    DispatcherServlet servlet;

    @Autowired
    DesktopResourceLoader resourceLoader;

    @PostConstruct
    private void init() {
        servlet.setDetectAllViewResolvers(false);
    }

    private APIClient getAdminClient(String codAoo){
        String jwtToken = ClientCacheAuthUtils.getInstance().simpleJWTToken(codAoo,"admin");
        return new APIClient(jwtToken);
    }

    @Override
    public void setEnvironment(Environment environment) {

        System.setProperty("docer.url",env.getProperty("zuul.routes.docer.url"));
        //System.setProperty("noDocer", env.getProperty("spring.noDocer","true") );

        ClientCacheAuthUtils.setThreadBearerResolver( () -> Session.getRequest().getHeader("Authorization"));

        ClientCacheAuthUtils.setSyncHelpers(
                (User user) -> getAdminClient(user.getCodAoo()).utenti().create(user),
                (User user) -> getAdminClient(user.getCodAoo()).utenti().update(user)
        );

        DesktopUtils.setEnv(environment);
    }
}
