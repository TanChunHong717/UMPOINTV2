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
@Schema(name = "Space Booking Technician")
public class AccBookingTechnicianDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7265623310868313849L;

    @SchemaProperty(name = "ID")
    private Long id;

    @SchemaProperty(name = "Booking ID")
    private Long bookingId;

    @SchemaProperty(name = "Technician ID")
    private Long technicianId;

    @SchemaProperty(name = "Technician name")
    private String technicianName;
}
