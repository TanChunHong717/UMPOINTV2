package my.edu.um.umpoint.modules.accommodation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * Accommodation Availability
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@Schema(name = "Accommodation Availability")
public class AccAvailabilityDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -8082424951656266853L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Accommodation ID")
	private Long accommodationId;

	@SchemaProperty(name = "Year")
	private Integer year;

	@SchemaProperty(name = "Availability of accommodation, consist of 366*24 bit, 1 represent available in specific day in one year")
	private byte[] availability;
}
