package my.edu.um.umpoint.modules.chat.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.chat.dto.ChatUserReportDTO;
import my.edu.um.umpoint.modules.chat.entity.ChatUserReportEntity;

/**
 * Report user in chat
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-11-25
 */
public interface ChatUserReportService extends CrudService<ChatUserReportEntity, ChatUserReportDTO> {
    void resolve(Long id);
}