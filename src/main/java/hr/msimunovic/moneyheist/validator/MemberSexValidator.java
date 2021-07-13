package hr.msimunovic.moneyheist.validator;

import hr.msimunovic.moneyheist.common.enums.MemberSexEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class MemberSexValidator implements ConstraintValidator<MemberSex, MemberSexEnum> {

    private MemberSexEnum[] subset;

    @Override
    public void initialize(MemberSex constraintAnnotation) {
        this.subset = constraintAnnotation.anyOf();
    }

    @Override
    public boolean isValid(MemberSexEnum value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}
