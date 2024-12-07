package my.edu.um.umpoint.modules.chat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Chat message attachment
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
@Data
@TableName("chat_message_attachment")
public class ChatMessageAttachmentEntity {
	/**
	* Attachment ID
	*/
	@TableId
	private Long id;
	/**
	* Message ID
	*/
	private Long messageId;
	/**
	* Attachment name
	*/
	private String name;
	/**
	* Attachment type
	*/
	private String type;
	/**
	* Image url
	*/
	private String url;
}