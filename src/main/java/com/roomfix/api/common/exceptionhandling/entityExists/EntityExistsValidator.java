package com.roomfix.api.common.exceptionhandling.entityExists;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates the {@link EntityExists} constraint.
 * The annotated Object must not be `null`
 */
public class EntityExistsValidator implements ConstraintValidator<EntityExists, Object> {
    @Override
    public void initialize(EntityExists constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return value != null;
    }
}

