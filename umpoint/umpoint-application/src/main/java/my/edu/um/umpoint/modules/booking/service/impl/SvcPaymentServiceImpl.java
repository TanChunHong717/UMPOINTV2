package my.edu.um.umpoint.modules.booking.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.booking.dao.SvcPaymentDao;
import my.edu.um.umpoint.modules.booking.dto.SvcPaymentDTO;
import my.edu.um.umpoint.modules.booking.entity.SvcPaymentEntity;
import my.edu.um.umpoint.modules.booking.service.SvcPaymentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Payment
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Service
public class SvcPaymentServiceImpl extends CrudServiceImpl<SvcPaymentDao, SvcPaymentEntity, SvcPaymentDTO> implements SvcPaymentService {

    @Override
    public QueryWrapper<SvcPaymentEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SvcPaymentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public PageData<SvcPaymentDTO> page(Map<String, Object> params) {
        IPage<SvcPaymentEntity> page = getPage(params, "create_date", false);
        List<SvcPaymentEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    public void refund(Long id) {
        SvcPaymentEntity entity = new SvcPaymentEntity();
        entity.setStatus(BookingConstant.PaymentStatus.REFUNDED.getValue());

        baseDao.update(entity, new QueryWrapper<SvcPaymentEntity>().eq("id", id));
    }
}