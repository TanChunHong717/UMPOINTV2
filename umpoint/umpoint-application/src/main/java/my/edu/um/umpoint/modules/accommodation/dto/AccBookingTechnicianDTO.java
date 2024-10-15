package my.edu.um.umpoint.modules.accommodation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Accommodation Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-11
 */
@Data
@Schema(title = "Space Booking Technician")
public class AccBookingTechnicianDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7265623310868313849L;

    @Schema(title = "ID")
    private Long id;

    @Schema(title = "Booking ID")
    private Long bookingId;

    @Schema(title = "Technician ID")
    private Long technicianId;

    @Schema(title = "Technician name")
    private String technicianName;
}
