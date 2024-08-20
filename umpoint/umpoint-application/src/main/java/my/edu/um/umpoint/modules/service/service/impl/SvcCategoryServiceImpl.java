package my.edu.um.umpoint.modules.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.service.dao.SvcCategoryDao;
import my.edu.um.umpoint.modules.service.dto.SvcCategoryDTO;
import my.edu.um.umpoint.modules.service.entity.SvcCategoryEntity;
import my.edu.um.umpoint.modules.service.service.SvcCategoryService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service Category
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Service
public class SvcCategoryServiceImpl extends CrudServiceImpl<SvcCategoryDao, SvcCategoryEntity, SvcCategoryDTO> implements SvcCategoryService {

    @Override
    public QueryWrapper<SvcCategoryEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SvcCategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}