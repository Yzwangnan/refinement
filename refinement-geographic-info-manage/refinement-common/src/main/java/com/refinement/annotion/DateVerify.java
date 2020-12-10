package com.refinement.annotion;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateVerifyValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateVerify {

    String message() default "日期格式不正确";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
