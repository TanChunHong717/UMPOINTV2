package my.edu.um.umpoint.modules.booking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.booking.dao.PaymentDao;
import my.edu.um.umpoint.modules.booking.dto.PaymentDTO;
import my.edu.um.umpoint.modules.booking.entity.PaymentEntity;
import cn.hutool.core.util.StrUtil;
import my.edu.um.umpoint.modules.booking.service.PaymentService;
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
public class PaymentServiceImpl extends CrudServiceImpl<PaymentDao, PaymentEntity, PaymentDTO> implements PaymentService {

    @Override
    public QueryWrapper<PaymentEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<PaymentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public PageData<PaymentDTO> page(Map<String, Object> params) {
        IPage<PaymentEntity> page = getPage(params, "create_date", false);
        List<PaymentEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    public void refund(Long id) {
        PaymentEntity entity = new PaymentEntity();
        entity.setStatus(BookingConstant.PaymentStatus.REFUNDED.getValue());

        baseDao.update(entity, new QueryWrapper<PaymentEntity>().eq("id", id));
    }
}