package my.edu.um.umpoint.modules.booking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.booking.dao.SvcPaymentDao;
import my.edu.um.umpoint.modules.booking.dto.SvcPaymentDTO;
import my.edu.um.umpoint.modules.booking.entity.SvcPaymentEntity;
import my.edu.um.umpoint.modules.booking.service.SvcPaymentService;
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
public class SvcPaymentServiceImpl extends CrudServiceImpl<SvcPaymentDao, SvcPaymentEntity, SvcPaymentDTO> implements SvcPaymentService {

    @Override
    public QueryWrapper<SvcPaymentEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SvcPaymentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}