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
 * @since 1.0.0 2024-10-29
 */
@Data
@Schema(title = "Report user in chat")
public class ChatUserReportDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 669L;

	@Schema(title = "ID")
	private Long id;

	@Schema(title = "User ID that is reported")
	private Long reportedUserId;

	@Schema(title = "Reported chat room ID")
	private Long chatRoomId;

	@Schema(title = "Reported message ID")
	private Long messageId;

	@Schema(title = "Reason")
	private String reason;

	@Schema(title = "User ID that send this report")
	private Long reportedBy;

	@Schema(title = "Creation Date")
	private Date createdAt;


}
