package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.space.dao.SpcBookingAttachmentDao;
import my.edu.um.umpoint.modules.space.dto.SpcBookingAttachmentDTO;
import my.edu.um.umpoint.modules.space.entity.SpcBookingAttachmentEntity;
import my.edu.um.umpoint.modules.space.service.SpcBookingAttachmentService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Space Booking Attachment
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-11-08
 */
@Service
public class SpcBookingAttachmentServiceImpl extends CrudServiceImpl<SpcBookingAttachmentDao, SpcBookingAttachmentEntity, SpcBookingAttachmentDTO> implements SpcBookingAttachmentService {

    @Override
    public QueryWrapper<SpcBookingAttachmentEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpcBookingAttachmentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}