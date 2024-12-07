package my.edu.um.umpoint.modules.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * Report user in chat
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-11-25
 */
@Data
@Schema(title = "Report user in chat")
public class ChatUserReportDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 1362746937523177165L;

	@Schema(title = "ID")
	private Long id;

	@Schema(title = "Reported chat room ID")
	private Long chatRoomId;

	@Schema(title = "Reported message ID")
	private Long messageId;

	@Schema(title = "Reason")
	private String reason;

	@Schema(title = "User ID that send this report")
	private Long reportedBy;

	@Schema(title = "User Type for Id that send this report")
	private Integer reportedByType;

	@Schema(title = "Creation Date")
	private Date createdAt;
}
