package hr.msimunovic.moneyheist.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MemberSkillValidator.class)
public @interface MemberSkill {

    String message() default "{hr.msimunovic.moneyheist.validator.MemberSkills.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
