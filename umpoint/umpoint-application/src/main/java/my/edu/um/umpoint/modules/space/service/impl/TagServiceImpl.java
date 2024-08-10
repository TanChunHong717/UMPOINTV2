package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.space.dao.TagDao;
import my.edu.um.umpoint.modules.space.dto.SpaceDTO;
import my.edu.um.umpoint.modules.space.dto.TagDTO;
import my.edu.um.umpoint.modules.space.entity.CategoryEntity;
import my.edu.um.umpoint.modules.space.entity.TagEntity;
import my.edu.um.umpoint.modules.space.service.TagService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Space Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Service
public class TagServiceImpl extends CrudServiceImpl<TagDao, TagEntity, TagDTO> implements TagService {

    @Override
    public QueryWrapper<TagEntity> getWrapper(Map<String, Object> params){
        String name = (String)params.get("name");

        QueryWrapper<TagEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(name), "name", name);

        return wrapper;
    }

    @Override
    public PageData<TagDTO> page(Map<String, Object> params) {
        paramsToLike(params, "tagName");

        IPage<TagEntity> page = getPage(params, "tagName", true);

        List<TagEntity> list = baseDao.listWithCount();

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    public List<TagDTO> list(Map<String, Object> params) {
        if (params.get("filter") != null && (boolean)params.get("filter")) {
            List<TagEntity> entityList = baseDao.filterList();
            return ConvertUtils.sourceToTarget(entityList, currentDtoClass());
        }

        return super.list(params);
    }
}