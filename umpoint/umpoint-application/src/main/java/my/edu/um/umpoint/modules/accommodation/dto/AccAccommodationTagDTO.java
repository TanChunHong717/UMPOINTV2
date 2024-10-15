package my.edu.um.umpoint.modules.accommodation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Accommodation tag relationship
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@Schema(title = "Accommodation tag relationship")
public class AccAccommodationTagDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -6174512699449473158L;

	@Schema(title = "Accommodation ID")
	private Long accommodationId;

	@Schema(title = "Tag ID")
	private Long tagId;
}
