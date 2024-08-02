package my.edu.um.umpoint.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFilter {

    String tableAlias() default "";

    String userId() default "creator";

    String deptId() default "dept_id";

}