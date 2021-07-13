package hr.msimunovic.moneyheist.validator;

import hr.msimunovic.moneyheist.common.enums.MemberSexEnum;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MemberSexValidator.class)
public @interface MemberSex {

    MemberSexEnum[] anyOf();
    String message() default "member sex must be any of {anyOf}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
