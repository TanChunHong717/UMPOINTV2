package my.edu.um.umpoint.modules.booking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.booking.dao.AccPaymentDao;
import my.edu.um.umpoint.modules.booking.dto.AccPaymentDTO;
import my.edu.um.umpoint.modules.booking.entity.AccPaymentEntity;
import my.edu.um.umpoint.modules.booking.service.AccPaymentService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

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


}