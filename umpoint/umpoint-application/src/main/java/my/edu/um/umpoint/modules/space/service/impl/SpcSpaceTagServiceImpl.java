package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.space.dao.SpcSpaceTagDao;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceTagDTO;
import my.edu.um.umpoint.modules.space.entity.SpcSpaceTagEntity;
import my.edu.um.umpoint.modules.space.service.SpcSpaceTagService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Space tag relationship
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Service
public class SpcSpaceTagServiceImpl extends CrudServiceImpl<SpcSpaceTagDao, SpcSpaceTagEntity, SpcSpaceTagDTO> implements SpcSpaceTagService {

    @Override
    public QueryWrapper<SpcSpaceTagEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpcSpaceTagEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public void deleteBySpaceId(Long spaceId) {
        baseDao.delete(new QueryWrapper<SpcSpaceTagEntity>().eq("space_id", spaceId));
    }
}