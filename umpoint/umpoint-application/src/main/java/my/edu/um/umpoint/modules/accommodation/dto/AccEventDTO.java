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
@Schema(name = "Accommodation Occupied Event")
public class AccEventDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 1L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Accommodation ID")
	private Long accommodationId;

	@SchemaProperty(name = "Booking ID")
	private Long bookingId;

	@SchemaProperty(name = "Closure ID")
	private Long closureId;

	@SchemaProperty(name = "Type: 0:Booking, 1:Closure period")
	private Integer type;

	@SchemaProperty(name = "Event start time")
	private Date startTime;

	@SchemaProperty(name = "Event end time")
	private Date endTime;
}
