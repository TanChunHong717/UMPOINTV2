package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.space.dao.SpcImageDao;
import my.edu.um.umpoint.modules.space.dto.SpcImageDTO;
import my.edu.um.umpoint.modules.space.entity.SpcImageEntity;
import my.edu.um.umpoint.modules.space.service.SpcImageService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Space Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Service
public class SpcImageServiceImpl extends CrudServiceImpl<SpcImageDao, SpcImageEntity, SpcImageDTO> implements SpcImageService {

    @Override
    public QueryWrapper<SpcImageEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpcImageEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public void deleteBySpaceId(Long spaceId) {
        baseDao.delete(new QueryWrapper<SpcImageEntity>().eq("space_id", spaceId));
    }
}