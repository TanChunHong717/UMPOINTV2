package my.edu.um.umpoint.modules.accommodation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.accommodation.dao.AccTagDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccTagDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccTagEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccTagService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Accommodation Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Service
public class AccTagServiceImpl extends CrudServiceImpl<AccTagDao, AccTagEntity, AccTagDTO> implements AccTagService {

    @Override
    public QueryWrapper<AccTagEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AccTagEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}