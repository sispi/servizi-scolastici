package it.filippetti.ks.api.payment.payment;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 *
 * @author Raffaele Dell'Aversana
 * @since 14 Jul 2018
 */
@Documented
@Target(ElementType.METHOD)
public
@interface PayParams {
    PayParam[] value();
}
