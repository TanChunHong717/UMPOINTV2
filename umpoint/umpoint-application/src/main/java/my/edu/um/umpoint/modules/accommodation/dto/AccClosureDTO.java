package my.edu.um.umpoint.modules.accommodation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "Accommodation Closure Period")
public class AccClosureDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -4395228089775543219L;

	@Schema(title = "ID")
	@Null(groups = {AddGroup.class})
	@NotNull(groups = {UpdateGroup.class})
	private Long id;

	@Schema(title = "Accommodation ID")
	@NotNull(groups = {DefaultGroup.class})
	private Long accommodationId;

	@Schema(title = "Start day of booking")
	@NotNull(groups = {DefaultGroup.class})
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date startDay;

	@Schema(title = "End day of booking")
	@NotNull(groups = {DefaultGroup.class})
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date endDay;

	@Schema(title = "Accommodation name")
	private String name;
}
