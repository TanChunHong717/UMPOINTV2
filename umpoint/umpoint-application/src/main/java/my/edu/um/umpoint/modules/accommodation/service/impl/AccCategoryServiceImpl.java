package my.edu.um.umpoint.modules.accommodation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.accommodation.dao.AccCategoryDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccCategoryDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccCategoryEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccCategoryService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Accommodation Category
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Service
public class AccCategoryServiceImpl extends CrudServiceImpl<AccCategoryDao, AccCategoryEntity, AccCategoryDTO> implements AccCategoryService {

    @Override
    public QueryWrapper<AccCategoryEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AccCategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}