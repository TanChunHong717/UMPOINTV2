package my.edu.um.umpoint.modules.sys.redis;

import my.edu.um.umpoint.common.redis.RedisKeys;
import my.edu.um.umpoint.common.redis.RedisUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SysParamsRedis {
    private final RedisUtils redisUtils;

    public void delete(Object[] paramCodes) {
        String key = RedisKeys.getSysParamsKey();
        redisUtils.hDel(key, paramCodes);
    }

    public void set(String paramCode, String paramValue) {
        if (paramValue == null) {
            return;
        }
        String key = RedisKeys.getSysParamsKey();
        redisUtils.hSet(key, paramCode, paramValue);
    }

    public String get(String paramCode) {
        String key = RedisKeys.getSysParamsKey();
        return (String) redisUtils.hGet(key, paramCode);
    }

}
