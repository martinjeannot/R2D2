package com.geomenum.r2d2.spring.servicelayer;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * A convenience annotation that is itself annotated with {@link org.springframework.stereotype.Component @Component}.
 * <br/>
 * It can be used to register {@link com.geomenum.r2d2.servicelayer.RequestHandler Request Handlers} into the Spring container.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RequestHandler {

    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     * @return the suggested component name, if any
     */
    String value() default "";
}
