package my.edu.um.umpoint.modules.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.chat.dao.ChatUserReportDao;
import my.edu.um.umpoint.modules.chat.dto.ChatUserReportDTO;
import my.edu.um.umpoint.modules.chat.entity.ChatUserReportEntity;
import my.edu.um.umpoint.modules.chat.service.ChatUserReportService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Report user in chat
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-29
 */
@Service
public class ChatUserReportServiceImpl extends CrudServiceImpl<ChatUserReportDao, ChatUserReportEntity,
    ChatUserReportDTO> implements ChatUserReportService{

    @Override
    public QueryWrapper<ChatUserReportEntity> getWrapper(Map<String, Object> params){
        String id = (String) params.get("id");

        QueryWrapper<ChatUserReportEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}