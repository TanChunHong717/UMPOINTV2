package my.edu.um.umpoint.modules.chat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Report user in chat
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-29
 */
@Data
@TableName("chat_user_report")
public class ChatUserReportEntity {

    /**
     * ID
     */
		@TableId
		private Long id;
    /**
     * User ID that is reported
     */
		private Long reportedUserId;
    /**
     * Reported chat room ID
     */
		private Long chatRoomId;
    /**
     * Reported message ID
     */
		private Long messageId;
    /**
     * Reason
     */
		private String reason;
    /**
     * User ID that send this report
     */
		private Long reportedBy;
    /**
     * Creation Date
     */
		private Date createdAt;
}