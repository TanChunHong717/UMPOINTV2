package my.edu.um.umpoint.modules.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * Chat room
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-30
 */
@Data
@Schema(title = "Chat room")
public class ChatRoomDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 1L;

	@Schema(title = "Room ID")
	private Long id;

	@Schema(title = "Room name")
	private String name;

	@Schema(title = "Status: 0:Created(No admin), 1:Open(Admin assigned), 2:Closed, 3:Resolved, 4:Reported")
	private Integer status;

	@Schema(title = "Should chatbot auto reply")
	private Integer autoChatbotReply;

	@Schema(title = "Type: 0:Space, 1:Service, 2:Accommodation")
	private Integer facilityType;

	@Schema(title = "Facility ID")
	private Long facilityId;

	@Schema(title = "User ID that created room")
	private Long initiateUserId;

	@Schema(title = "Admin user ID")
	private Long assignedAdminId;

	@Schema(title = "Creation Date")
	private Date createdAt;


}
