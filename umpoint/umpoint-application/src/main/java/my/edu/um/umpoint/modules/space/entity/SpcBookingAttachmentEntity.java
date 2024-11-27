package my.edu.um.umpoint.modules.space.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Space Booking Attachment
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-11-08
 */
@Data
@TableName("spc_booking_attachment")
public class SpcBookingAttachmentEntity {

    /**
     * Attachment ID
     */
		@TableId
		private Long id;
    /**
     * Booking ID
     */
		private Long bookingId;
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