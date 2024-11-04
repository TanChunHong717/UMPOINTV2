package my.edu.um.umpoint.modules.space.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.annotation.DataFilter;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.space.dao.SpcDeptBookingRuleDao;
import my.edu.um.umpoint.modules.space.dto.SpcDeptBookingRuleDTO;
import my.edu.um.umpoint.modules.space.entity.SpcDeptBookingRuleEntity;
import my.edu.um.umpoint.modules.space.service.SpcDeptBookingRuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Space Department Level Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-11-02
 */
@Service
public class SpcDeptBookingRuleServiceImpl extends CrudServiceImpl<SpcDeptBookingRuleDao, SpcDeptBookingRuleEntity, SpcDeptBookingRuleDTO> implements SpcDeptBookingRuleService {

    @Override
    public QueryWrapper<SpcDeptBookingRuleEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpcDeptBookingRuleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    @DataFilter(tableAlias = "d")
    public PageData<SpcDeptBookingRuleDTO> page(Map<String, Object> params) {
        paramsToLike(params, "deptName");

        IPage<SpcDeptBookingRuleEntity> page = getPage(params, Constant.CREATE_DATE, false);

        List<SpcDeptBookingRuleEntity> list = baseDao.list(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }
}