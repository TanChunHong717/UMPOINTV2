package my.edu.um.umpoint.common.redis;

public class RedisKeys {
    public static String getSysParamsKey(){
        return "sys:params";
    }

    public static String getCaptchaKey(String uuid){
        return "sys:captcha:" + uuid;
    }

    public static String getSecurityUserKey(Long id){
        return "sys:security:user:" + id;
    }

    public static String getSysLogKey(){
        return "sys:log";
    }

    public static String getSysResourceKey(){
        return  "sys:resource";
    }

    public static String getUserMenuNavKey(Long userId){
        return "sys:user:nav:" + userId;
    }

    public static String getUserPermissionsKey(Long userId){
        return "sys:user:permissions:" + userId;
    }
}
