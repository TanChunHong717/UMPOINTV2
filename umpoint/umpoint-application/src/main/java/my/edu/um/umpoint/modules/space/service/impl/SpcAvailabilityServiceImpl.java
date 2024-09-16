package my.edu.um.umpoint.modules.space.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.space.availability.SpaceAvailability;
import my.edu.um.umpoint.modules.space.dao.SpcAvailabilityDao;
import my.edu.um.umpoint.modules.space.dto.SpcAvailabilityDTO;
import my.edu.um.umpoint.modules.space.entity.SpcAvailabilityEntity;
import my.edu.um.umpoint.modules.space.service.SpcAvailabilityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Space Availability
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Service
public class SpcAvailabilityServiceImpl extends CrudServiceImpl<SpcAvailabilityDao, SpcAvailabilityEntity, SpcAvailabilityDTO> implements SpcAvailabilityService {

    @Override
    public QueryWrapper<SpcAvailabilityEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpcAvailabilityEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public List<SpcAvailabilityEntity> getBySpaceId(Long spaceId) {
        return baseDao.selectList(new QueryWrapper<SpcAvailabilityEntity>().eq("space_id", spaceId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long spaceId, SpaceAvailability spaceAvailability) {
        Map<Integer, byte[]> availabilityMap = spaceAvailability.getAvailability();

        List<SpcAvailabilityEntity> availabilityEntityList = getBySpaceId(spaceId);
        for (SpcAvailabilityEntity availabilityEntity : availabilityEntityList) {
            Integer year = availabilityEntity.getYear();
            byte[] availability = availabilityMap.remove(year);
            availabilityEntity.setAvailability(availability);
        }
        updateBatchById(availabilityEntityList);

        List<SpcAvailabilityEntity> newAvailabilityEntityList = new ArrayList<>();
        if (!availabilityMap.isEmpty()) {
            availabilityMap.keySet().forEach(key -> {
                SpcAvailabilityEntity spcAvailabilityEntity = new SpcAvailabilityEntity();

                spcAvailabilityEntity.setSpaceId(spaceId);
                spcAvailabilityEntity.setYear(key);
                spcAvailabilityEntity.setAvailability(availabilityMap.get(key));

                newAvailabilityEntityList.add(spcAvailabilityEntity);
            });
        }
        insertBatch(newAvailabilityEntityList);
    }
}