package my.edu.um.umpoint.modules.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Space Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-11
 */
@Data
@Schema(title = "Space Booking Technician")
public class SpcBookingTechnicianDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -78769910842793281L;

    @Schema(title = "ID")
    private Long id;

    @Schema(title = "Booking ID")
    private Long bookingId;

    @Schema(title = "Technician ID")
    private Long technicianId;

    @Schema(title = "Technician name")
    private String technicianName;
}
