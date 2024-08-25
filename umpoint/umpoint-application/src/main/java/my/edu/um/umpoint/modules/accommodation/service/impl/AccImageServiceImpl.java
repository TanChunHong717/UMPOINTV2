package my.edu.um.umpoint.modules.accommodation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.accommodation.dao.AccImageDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccImageDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccImageEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccImageService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Accommodation Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Service
public class AccImageServiceImpl extends CrudServiceImpl<AccImageDao, AccImageEntity, AccImageDTO> implements AccImageService {

    @Override
    public QueryWrapper<AccImageEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AccImageEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public void deleteByAccommodationId(Long accommodationId) {
        baseDao.delete(new QueryWrapper<AccImageEntity>().eq("accommodation_id", accommodationId));
    }
}