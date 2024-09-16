package my.edu.um.umpoint.modules.accommodation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Accommodation Closure Period
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Data
@Schema(name = "Accommodation Closure Period")
public class AccClosureDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 1L;

	@SchemaProperty(name = "ID")
	@Null(groups = {AddGroup.class})
	@NotNull(groups = {UpdateGroup.class})
	private Long id;

	@SchemaProperty(name = "Accommodation ID")
	@NotNull(groups = {DefaultGroup.class})
	private Long accommodationId;

	@SchemaProperty(name = "Start day of booking")
	@NotNull(groups = {DefaultGroup.class})
	private Date startDay;

	@SchemaProperty(name = "End day of booking")
	@NotNull(groups = {DefaultGroup.class})
	private Date endDay;
}
