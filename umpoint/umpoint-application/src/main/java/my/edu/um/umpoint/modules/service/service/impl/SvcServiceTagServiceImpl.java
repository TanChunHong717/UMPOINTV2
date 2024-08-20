package my.edu.um.umpoint.modules.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.service.dao.SvcServiceTagDao;
import my.edu.um.umpoint.modules.service.dto.SvcServiceTagDTO;
import my.edu.um.umpoint.modules.service.entity.SvcServiceTagEntity;
import my.edu.um.umpoint.modules.service.service.SvcServiceTagService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service tag relationship
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Service
public class SvcServiceTagServiceImpl extends CrudServiceImpl<SvcServiceTagDao, SvcServiceTagEntity, SvcServiceTagDTO> implements SvcServiceTagService {

    @Override
    public QueryWrapper<SvcServiceTagEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SvcServiceTagEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}