package my.edu.um.umpoint.modules.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Service Category
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Data
@Schema(name = "Service Category")
public class SvcCategoryDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 2857536342370451285L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Name")
	private String name;
}
