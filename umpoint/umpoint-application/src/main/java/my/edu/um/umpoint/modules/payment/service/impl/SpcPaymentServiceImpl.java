package my.edu.um.umpoint.modules.payment.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.payment.dao.SpcPaymentDao;
import my.edu.um.umpoint.modules.payment.dto.SpcPaymentDTO;
import my.edu.um.umpoint.modules.payment.entity.SpcPaymentEntity;
import my.edu.um.umpoint.modules.payment.service.SpcPaymentService;
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
public class SpcPaymentServiceImpl extends CrudServiceImpl<SpcPaymentDao, SpcPaymentEntity, SpcPaymentDTO> implements SpcPaymentService{

    @Override
    public QueryWrapper<SpcPaymentEntity> getWrapper(Map<String, Object> params){
        String id = (String) params.get("id");

        QueryWrapper<SpcPaymentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public PageData<SpcPaymentDTO> page(Map<String, Object> params){
        IPage<SpcPaymentEntity> page = getPage(params, "create_date", false);
        List<SpcPaymentEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    public SpcPaymentEntity getLatestPayment(Long bookingId){
        QueryWrapper<SpcPaymentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("booking_id", bookingId);
        wrapper.orderByDesc("created_at");

        return baseDao.selectOne(wrapper);
    }

    @Override
    public void pay(Long id){
        SpcPaymentEntity entity = new SpcPaymentEntity();
        entity.setStatus(BookingConstant.PaymentStatus.SUCCESS.getValue());

        baseDao.update(entity, new QueryWrapper<SpcPaymentEntity>().eq("id", id));
    }

    @Override
    public void refund(Long id){
        SpcPaymentEntity entity = new SpcPaymentEntity();
        entity.setStatus(BookingConstant.PaymentStatus.REFUNDED.getValue());

        baseDao.update(entity, new QueryWrapper<SpcPaymentEntity>().eq("id", id));
    }
}