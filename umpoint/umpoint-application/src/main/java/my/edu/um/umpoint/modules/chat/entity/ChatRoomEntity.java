package my.edu.um.umpoint.modules.chat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Chat room
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-30
 */
@Data
@TableName("chat_room")
public class ChatRoomEntity {
	/**
	* Room ID
	*/
	@TableId
	private Long id;
	/**
	* Room name
	*/
	private String name;
	/**
	* Status: 0:Created(No admin), 1:Open(Admin assigned), 2:Closed, 3:Resolved, 4:Reported
	*/
	private Integer status;
	/**
	* Should chatbot auto reply
	*/
	private Integer autoChatbotReply;
	/**
	* Type: 0:Space, 1:Service, 2:Accommodation
	*/
	private Integer facilityType;
	/**
	* Facility ID
	*/
	private Long facilityId;
	/**
	* Department ID for the facility
	*/
	private Long facilityDepartmentId;
	/**
	* User ID that created room
	*/
	private Long initiateUserId;
	/**
	* Admin user ID
	*/
	private Long assignedAdminId;
	/**
	 * Creator ID
	 */
	private Long creator;
	/**
	* Creation Date
	*/
	private Date createdAt;

	/**
	 * Last message in room
	 */
	@TableField(exist = false)
	private ChatMessageEntity lastMessageEntity;
}