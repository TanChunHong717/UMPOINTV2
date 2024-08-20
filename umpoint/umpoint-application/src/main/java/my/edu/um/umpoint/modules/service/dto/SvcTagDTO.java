package my.edu.um.umpoint.modules.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * Service Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Data
@Schema(name = "Service Tag")
public class SvcTagDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 3478201506629761368L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Tag name")
	private String tagName;


}
