package my.edu.um.umpoint.modules.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * Chat message
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
@Data
@Schema(title = "Chat message")
public class ChatMessageDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 667L;

	@Schema(title = "Message ID")
	private Long id;

	@Schema(title = "Chat room ID")
	private Long chatRoomId;

	@Schema(title = "Sender user type: 0:System, 1:Bot, 2:User, 3:Admin")
	private Integer senderType;

	@Schema(title = "Sender user ID")
	private Long userId;

	@Schema(title = "Sender admin ID")
	private Long adminId;

	@Schema(title = "Message body")
	private String message;

	@Schema(title = "Message ID that is responding to")
	private Long replyMessageId;

	@Schema(title = "Creation Date")
	private Date createdAt;


}