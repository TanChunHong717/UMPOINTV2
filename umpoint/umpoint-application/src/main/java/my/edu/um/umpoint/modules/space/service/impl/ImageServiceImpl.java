package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.space.dao.ImageDao;
import my.edu.um.umpoint.modules.space.dto.ImageDTO;
import my.edu.um.umpoint.modules.space.entity.ImageEntity;
import my.edu.um.umpoint.modules.space.service.ImageService;
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
public class ImageServiceImpl extends CrudServiceImpl<ImageDao, ImageEntity, ImageDTO> implements ImageService {

    @Override
    public QueryWrapper<ImageEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ImageEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}