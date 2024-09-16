package my.edu.um.umpoint.modules.payment.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.payment.dao.AccPaymentDao;
import my.edu.um.umpoint.modules.payment.dto.AccPaymentDTO;
import my.edu.um.umpoint.modules.payment.entity.AccPaymentEntity;
import my.edu.um.umpoint.modules.payment.service.AccPaymentService;
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
public class AccPaymentServiceImpl extends CrudServiceImpl<AccPaymentDao, AccPaymentEntity, AccPaymentDTO> implements AccPaymentService {

    @Override
    public QueryWrapper<AccPaymentEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AccPaymentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public PageData<AccPaymentDTO> page(Map<String, Object> params) {
        IPage<AccPaymentEntity> page = getPage(params, "create_date", false);
        List<AccPaymentEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    public void refund(Long id) {
        AccPaymentEntity entity = new AccPaymentEntity();
        entity.setStatus(BookingConstant.PaymentStatus.REFUNDED.getValue());

        baseDao.update(entity, new QueryWrapper<AccPaymentEntity>().eq("id", id));
    }
}