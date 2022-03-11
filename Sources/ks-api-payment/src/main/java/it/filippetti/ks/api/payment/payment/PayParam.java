package it.filippetti.ks.api.payment.payment;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/**
 * Annotations for payment parameters
 *
 * @author Raffaele Dell'Aversana
 * @since 14 Jul 2018
 */
@Documented
@Target(ElementType.METHOD)
@Repeatable(PayParams.class)
public @interface PayParam {
    /**
     * enumerate list of payment types
     */
    public enum Service {
        PMPAY, CPAY, BNL
    }

    Service service();
    String param();
    String note() default "";
}
