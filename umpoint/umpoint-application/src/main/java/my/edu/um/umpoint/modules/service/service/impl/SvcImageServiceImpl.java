package my.edu.um.umpoint.modules.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.service.dao.SvcImageDao;
import my.edu.um.umpoint.modules.service.dto.SvcImageDTO;
import my.edu.um.umpoint.modules.service.entity.SvcImageEntity;
import my.edu.um.umpoint.modules.service.service.SvcImageService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Service
public class SvcImageServiceImpl extends CrudServiceImpl<SvcImageDao, SvcImageEntity, SvcImageDTO> implements SvcImageService {

    @Override
    public QueryWrapper<SvcImageEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SvcImageEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}