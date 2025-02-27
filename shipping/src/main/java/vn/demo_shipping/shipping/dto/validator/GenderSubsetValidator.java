package vn.demo_shipping.shipping.dto.validator;

import java.util.Arrays;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.demo_shipping.shipping.util.Gender;

public class GenderSubsetValidator implements ConstraintValidator<GenderSubset, Gender> {

    private Gender[] genders;

    @Override
    public void initialize(GenderSubset constraintAnnotation) {
        this.genders = constraintAnnotation.anyOf();
    }

    @Override
    public boolean isValid(Gender value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(genders).contains(value);
    }

}
