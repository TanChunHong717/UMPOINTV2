package my.edu.um.umpoint.modules.space.service.impl;

import my.edu.um.umpoint.modules.space.service.SpcClosedSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Space Closed Space
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2025-01-26
 */
@Service
public class SpcClosedSpaceServiceImpl implements SpcClosedSpaceService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final String START_ZSET = "close_spaces:start";
    private static final String END_ZSET = "close_spaces:end";

    @Override
    public void addSpace(Long startTime, Long endTime, Long spaceId) {
        redisTemplate.opsForZSet().add(START_ZSET, spaceId + ":" + startTime, startTime);
        redisTemplate.opsForZSet().add(END_ZSET, spaceId + ":" + startTime, endTime);
    }

    @Override
    public void removeSpace(Long startTime, Long endTime, Long spaceId) {
        redisTemplate.opsForZSet().remove(START_ZSET, spaceId + ":" + startTime);
        redisTemplate.opsForZSet().remove(END_ZSET, spaceId + ":" + startTime);
    }

    @Override
    public List<Long> getClosedSpace(Long startTime, Long endTime) {
        String tempStartSet = "close_spaces:temp_start";
        String tempEndSet = "close_spaces:temp_end";

        redisTemplate.opsForZSet().intersectAndStore(START_ZSET, START_ZSET, tempStartSet);
        redisTemplate.opsForZSet().removeRangeByScore(tempStartSet,endTime + 1, Double.POSITIVE_INFINITY);

        redisTemplate.opsForZSet().intersectAndStore(END_ZSET, END_ZSET, tempEndSet);
        redisTemplate.opsForZSet().removeRangeByScore(tempEndSet, Double.NEGATIVE_INFINITY, startTime - 1);

        Set<String> resultIds = redisTemplate.opsForZSet().intersect(tempStartSet, tempEndSet);
        redisTemplate.delete(tempStartSet);
        redisTemplate.delete(tempEndSet);

        if (resultIds != null)
            return resultIds.stream().map(Long::parseLong).toList();
        return Collections.emptyList();
    }

    @Override
    public void clean() {
        long now = new Date().getTime();

        redisTemplate.opsForZSet().remove(START_ZSET, redisTemplate.opsForZSet().rangeByScore(END_ZSET, Double.NEGATIVE_INFINITY, now));
        redisTemplate.opsForZSet().removeRangeByScore(END_ZSET, Double.NEGATIVE_INFINITY, now);
    }
}
