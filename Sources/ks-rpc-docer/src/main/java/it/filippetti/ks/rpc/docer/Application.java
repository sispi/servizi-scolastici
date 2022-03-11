package it.filippetti.ks.rpc.docer;

import it.kdm.doctoolkit.utils.Utils;
import java.io.File;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(exclude = {
    SecurityAutoConfiguration.class,
    ManagementWebSecurityAutoConfiguration.class,
    UserDetailsServiceAutoConfiguration.class
})
//@EnableAutoConfiguration
public class Application {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        System.out.println(ANSI_GREEN);
        System.out.println("spring.profiles.active:"+ ctx.getEnvironment().getProperty("spring.profiles.active"));
        System.out.println("docer.url:"+System.getProperty("docer.url"));
        System.out.println("upload folder:"+System.getProperty("tempfiles.upload" , new File(Utils.getConfigHome() , "upload" ).getAbsolutePath()));
        System.out.println("server.port: "+ ctx.getEnvironment().getProperty("server.port"));
        System.out.println(ANSI_RESET);

    }
}
