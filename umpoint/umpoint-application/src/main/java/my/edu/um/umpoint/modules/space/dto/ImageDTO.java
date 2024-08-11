package my.edu.um.umpoint.modules.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * Space Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
@Schema(name = "Space Image")
public class ImageDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -3389885882502784485L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Space ID")
	private Long spaceId;

	@SchemaProperty(name = "Image url")
	private String imageUrl;


}
