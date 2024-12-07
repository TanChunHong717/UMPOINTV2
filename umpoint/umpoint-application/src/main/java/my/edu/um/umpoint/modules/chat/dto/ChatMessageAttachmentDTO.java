package my.edu.um.umpoint.modules.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * Chat message attachment
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
@Data
@Schema(title = "Chat message attachment")
public class ChatMessageAttachmentDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -185363434095746059L;

	@Schema(title = "Attachment ID")
	private Long id;

	@Schema(title = "Message ID")
	private Long messageId;

	@Schema(title = "Attachment name")
	private String name;

	@Schema(title = "Attachment type")
	private String type;

	@Schema(title = "Image url")
	private String url;
}
