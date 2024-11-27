package my.edu.um.umpoint.modules.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * Space Booking Attachment
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-11-08
 */
@Data
@Schema(title = "Space Booking Attachment")
public class SpcBookingAttachmentDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 1L;

	@Schema(title = "Attachment ID")
	private Long id;

	@Schema(title = "Booking ID")
	private Long bookingId;

	@Schema(title = "Attachment name")
	private String name;

	@Schema(title = "Attachment type")
	private String type;

	@Schema(title = "Image url")
	private String url;


}
