package my.edu.um.umpoint.modules.accommodation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.accommodation.availability.AccommodationAvailability;
import my.edu.um.umpoint.modules.accommodation.dao.AccAvailabilityDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccAvailabilityDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccAvailabilityEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccAvailabilityService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Accommodation Availability
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Service
public class AccAvailabilityServiceImpl extends CrudServiceImpl<AccAvailabilityDao, AccAvailabilityEntity, AccAvailabilityDTO> implements AccAvailabilityService {

    @Override
    public QueryWrapper<AccAvailabilityEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AccAvailabilityEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<AccAvailabilityEntity> getByAccommodationId(Long accommodationId) {
        return baseDao.selectList(new QueryWrapper<AccAvailabilityEntity>().eq("accommodation_id", accommodationId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long accommodationId, AccommodationAvailability accommodationAvailability) {
        Map<Integer, byte[]> availabilityMap = accommodationAvailability.getAvailabilityMap();

        List<AccAvailabilityEntity> availabilityEntityList = getByAccommodationId(accommodationId);
        for (AccAvailabilityEntity availabilityEntity : availabilityEntityList) {
            Integer year = availabilityEntity.getYear();
            byte[] availability = availabilityMap.remove(year);
            availabilityEntity.setAvailability(availability);
        }
        updateBatchById(availabilityEntityList);

        List<AccAvailabilityEntity> newAvailableEntityList = new ArrayList<>();
        if (!availabilityMap.isEmpty()) {
            availabilityMap.keySet().forEach(key -> {
                AccAvailabilityEntity accAvailabilityEntity = new AccAvailabilityEntity();

                accAvailabilityEntity.setAccommodationId(accommodationId);
                accAvailabilityEntity.setYear(key);
                accAvailabilityEntity.setAvailability(availabilityMap.get(key));

                newAvailableEntityList.add(accAvailabilityEntity);
            });
        }
        insertBatch(newAvailableEntityList);
    }
}