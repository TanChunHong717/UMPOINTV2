package my.edu.um.umpoint.modules.security.user;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class SecurityUser {

    public static Subject getSubject() {
        try {
            return SecurityUtils.getSubject();
        }catch (Exception e){
            return null;
        }
    }

    public static UserDetail getUser() {
        Subject subject = getSubject();
        if(subject == null){
            return new UserDetail();
        }

        UserDetail user = (UserDetail)subject.getPrincipal();
        if(user == null){
            return new UserDetail();
        }

        return user;
    }

    public static Long getUserId() {
        return getUser().getId();
    }

    public static Long getDeptId() {
        return getUser().getDeptId();
    }
}