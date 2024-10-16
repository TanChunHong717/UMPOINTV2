package my.edu.um.umpoint.modules.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.payment.dao.AccPaymentItemDao;
import my.edu.um.umpoint.modules.payment.dto.AccPaymentItemDTO;
import my.edu.um.umpoint.modules.payment.entity.AccPaymentItemEntity;
import my.edu.um.umpoint.modules.payment.service.AccPaymentItemService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Accommodation Payment Itemized
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-16
 */
@Service
public class AccPaymentItemServiceImpl extends CrudServiceImpl<AccPaymentItemDao, AccPaymentItemEntity, AccPaymentItemDTO> implements AccPaymentItemService {

    @Override
    public QueryWrapper<AccPaymentItemEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AccPaymentItemEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}