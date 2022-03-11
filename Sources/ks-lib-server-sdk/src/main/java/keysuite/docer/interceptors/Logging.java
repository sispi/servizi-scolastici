package keysuite.docer.interceptors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {

    //public boolean onlyDebug() default false;
    public String section() default "";
    public String group() default "";
    //public String sidArg() default "";
}
