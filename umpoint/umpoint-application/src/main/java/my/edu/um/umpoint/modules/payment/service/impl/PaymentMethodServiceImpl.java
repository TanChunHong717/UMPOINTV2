package my.edu.um.umpoint.modules.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.payment.dao.PaymentMethodDao;
import my.edu.um.umpoint.modules.payment.dto.PaymentMethodDTO;
import my.edu.um.umpoint.modules.payment.entity.PaymentMethodEntity;
import my.edu.um.umpoint.modules.payment.service.PaymentMethodService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Payment Method
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-10
 */
@Service
public class PaymentMethodServiceImpl extends CrudServiceImpl<PaymentMethodDao, PaymentMethodEntity, PaymentMethodDTO> implements PaymentMethodService {

    @Override
    public QueryWrapper<PaymentMethodEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<PaymentMethodEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}