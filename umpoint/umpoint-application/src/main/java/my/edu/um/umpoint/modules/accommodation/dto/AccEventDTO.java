package my.edu.um.umpoint.modules.accommodation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * Accommodation Occupied Event
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Data
@Schema(title = "Accommodation Occupied Event")
public class AccEventDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 1L;

	@Schema(title = "ID")
	private Long id;

	@Schema(title = "Accommodation ID")
	private Long accommodationId;

	@Schema(title = "Booking ID")
	private Long bookingId;

	@Schema(title = "Closure ID")
	private Long closureId;

	@Schema(title = "Type: 0:Booking, 1:Closure period")
	private Integer type;

	@Schema(title = "Event start time")
	private Date startTime;

	@Schema(title = "Event end time")
	private Date endTime;
}
