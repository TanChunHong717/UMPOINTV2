package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.space.dao.SpaceTagDao;
import my.edu.um.umpoint.modules.space.dto.SpaceTagDTO;
import my.edu.um.umpoint.modules.space.dto.TagDTO;
import my.edu.um.umpoint.modules.space.entity.SpaceTagEntity;
import my.edu.um.umpoint.modules.space.service.SpaceTagService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Space tag relationship
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Service
public class SpaceTagServiceImpl extends CrudServiceImpl<SpaceTagDao, SpaceTagEntity, SpaceTagDTO> implements SpaceTagService {

    @Override
    public QueryWrapper<SpaceTagEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpaceTagEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public void deleteBySpaceId(Long spaceId) {
        baseDao.delete(new QueryWrapper<SpaceTagEntity>().eq("space_id", spaceId));
    }
}