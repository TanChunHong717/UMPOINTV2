package my.edu.um.umpoint.common.annotation;

import java.lang.annotation.*;

/**
 * Can be configured at method of any components that have first param as map
 * Add a sql statement that add filter based on current user department
 * The sql statement will automatically use by DataFilterInterceptor to generate new sql statement
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFilter {

    String tableAlias() default "";

    String userId() default "creator";

    String deptId() default "dept_id";

}