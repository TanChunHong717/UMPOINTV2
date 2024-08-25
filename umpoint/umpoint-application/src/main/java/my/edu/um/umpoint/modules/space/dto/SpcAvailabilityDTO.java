package my.edu.um.umpoint.modules.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import java.math.BigDecimal;

/**
 * Space Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
@Schema(name = "Space Image")
public class SpcAvailabilityDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -7799356344051686910L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Space ID")
	private Long spaceId;

	@SchemaProperty(name = "Year")
	private BigDecimal year;

	@SchemaProperty(name = "Availability of space, consist of 366*24 bit, 1 represent available in specific hour in one year")
	private byte[] availability;
}
