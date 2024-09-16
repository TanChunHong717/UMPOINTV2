package my.edu.um.umpoint.modules.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * Space Occupied Event
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Data
@Schema(name = "Space Occupied Event")
public class SpcEventDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 1L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Space ID")
	private Long spaceId;

	@SchemaProperty(name = "Booking ID")
	private Long bookingId;

	@SchemaProperty(name = "Closure ID")
	private Long closureId;

	@SchemaProperty(name = "Type: 0:Booking, 1:Close after booking, 2:Closure period")
	private Integer type;

	@SchemaProperty(name = "Event start time")
	private Date startTime;

	@SchemaProperty(name = "Event end time")
	private Date endTime;
}
