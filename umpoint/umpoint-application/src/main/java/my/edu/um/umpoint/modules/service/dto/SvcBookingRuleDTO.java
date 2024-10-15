package my.edu.um.umpoint.modules.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;

import java.io.Serial;
import java.io.Serializable;

/**
 * Service Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-11
 */
@Data
@Schema(title = "Service Booking Rule")
@EqualsAndHashCode
public class SvcBookingRuleDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 3105155482684743857L;

	@Schema(title = "ID")
	@Null(groups = {AddGroup.class})
	@NotNull(groups = {UpdateGroup.class})
	private Long id;

	@Schema(title = "0:Automatic approve 1: Require admin approve")
	@NotNull(groups = {DefaultGroup.class})
	private Integer approvalRequired;

	@Schema(title = "0:Staff not allow to book 1:Staff allow to book")
	@NotNull(groups = {DefaultGroup.class})
	private Integer openForStaff;

	@Schema(title = "0:Student not allow to book 1:Student allow to book")
	@NotNull(groups = {DefaultGroup.class})
	private Integer openForStudent;

	@Schema(title = "0:Automatic approve 1: Require admin approve")
	@NotNull(groups = {DefaultGroup.class})
	private Integer openForPublic;
}
