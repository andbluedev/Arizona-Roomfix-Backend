package com.roomfix.api.common.exceptionhandling.entityExists;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Constraint annotating an argument to a method which must not be null
 * Use it when using a Spring Converter on a Controller to automatically get en Entity from its ID
 */
@Documented
@Constraint(validatedBy = EntityExistsValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityExists {
    String message() default "This entity does not exist";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
