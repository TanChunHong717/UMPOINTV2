package my.edu.um.umpoint.modules.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.service.dao.SvcTagDao;
import my.edu.um.umpoint.modules.service.dto.SvcTagDTO;
import my.edu.um.umpoint.modules.service.entity.SvcTagEntity;
import my.edu.um.umpoint.modules.service.service.SvcTagService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Service
public class SvcTagServiceImpl extends CrudServiceImpl<SvcTagDao, SvcTagEntity, SvcTagDTO> implements SvcTagService {

    @Override
    public QueryWrapper<SvcTagEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SvcTagEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}