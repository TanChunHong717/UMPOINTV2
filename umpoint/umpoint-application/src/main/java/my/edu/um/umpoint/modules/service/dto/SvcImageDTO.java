package my.edu.um.umpoint.modules.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Service Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Data
@Schema(name = "Service Image")
public class SvcImageDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -1763475943198748177L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Service ID")
	private Long serviceId;

	@SchemaProperty(name = "Image url")
	private String imageUrl;


}
