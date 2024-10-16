package my.edu.um.umpoint.modules.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.payment.dao.SvcPaymentItemDao;
import my.edu.um.umpoint.modules.payment.dto.SvcPaymentItemDTO;
import my.edu.um.umpoint.modules.payment.entity.SvcPaymentItemEntity;
import my.edu.um.umpoint.modules.payment.service.SvcPaymentItemService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service Payment Itemized
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-16
 */
@Service
public class SvcPaymentItemServiceImpl extends CrudServiceImpl<SvcPaymentItemDao, SvcPaymentItemEntity, SvcPaymentItemDTO> implements SvcPaymentItemService {

    @Override
    public QueryWrapper<SvcPaymentItemEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SvcPaymentItemEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}