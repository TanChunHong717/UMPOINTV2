package my.edu.um.umpoint.modules.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.payment.dao.SpcPaymentItemDao;
import my.edu.um.umpoint.modules.payment.dto.SpcPaymentItemDTO;
import my.edu.um.umpoint.modules.payment.entity.SpcPaymentItemEntity;
import my.edu.um.umpoint.modules.payment.service.SpcPaymentItemService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Space Payment Itemized
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-15
 */
@Service
public class SpcPaymentItemServiceImpl extends CrudServiceImpl<SpcPaymentItemDao, SpcPaymentItemEntity, SpcPaymentItemDTO> implements SpcPaymentItemService {

    @Override
    public QueryWrapper<SpcPaymentItemEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpcPaymentItemEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}