package my.edu.um.umpoint.modules.accommodation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.accommodation.dao.AccAccommodationTagDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccAccommodationTagDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccAccommodationTagEntity;
import my.edu.um.umpoint.modules.accommodation.entity.AccImageEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationTagService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Accommodation tag relationship
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Service
public class AccAccommodationTagServiceImpl extends CrudServiceImpl<AccAccommodationTagDao, AccAccommodationTagEntity, AccAccommodationTagDTO> implements AccAccommodationTagService {

    @Override
    public QueryWrapper<AccAccommodationTagEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AccAccommodationTagEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public void deleteByAccommodationId(Long accommodationId) {
        baseDao.delete(new QueryWrapper<AccAccommodationTagEntity>().eq("accommodation_id", accommodationId));
    }
}