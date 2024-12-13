package my.edu.um.umpoint.modules.chat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import my.edu.um.umpoint.modules.payment.entity.SpcPaymentEntity;

import java.util.Date;
import java.util.List;

/**
 * Chat message
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
@Data
@TableName("chat_message")
public class ChatMessageEntity {
	/**
	* Message ID
	*/
	@TableId
	private Long id;
	/**
	* Chat room ID
	*/
	private Long chatRoomId;
	/**
	* Sender user type: 0:System, 1:Bot, 2:User, 3:Admin
	*/
	private Integer senderType;
	/**
	* Sender user ID
	*/
	private Long userId;
	/**
	* Sender admin ID
	*/
	private Long adminId;
	/**
	* Message body
	*/
	private String message;
	/**
	* Message ID that is responding to
	*/
	private Long replyMessageId;
	/**
	* Creation Date
	*/
	private Date createdAt;
	/**
	* Attachment entity list
	*/
	@TableField(exist = false)
	private List<ChatMessageAttachmentEntity> chatMessageAttachmentEntityList;
	/**
	 * Reply message
	 */
	@TableField(exist = false)
	private ChatMessageEntity replyMessageEntity;


	/**
	 * Message Count (for pagination)
	 */
	@TableField(exist = false)
	private Long messageCount;
}