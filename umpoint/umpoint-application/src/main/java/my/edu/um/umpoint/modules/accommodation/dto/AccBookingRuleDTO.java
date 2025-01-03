package my.edu.um.umpoint.modules.accommodation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.BatchUpdateGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Accommodation Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@Schema(title = "Accommodation Booking Rule")
@EqualsAndHashCode
public class AccBookingRuleDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 9187174069158066308L;

	@Schema(title = "ID")
	@Null(groups = {AddGroup.class, BatchUpdateGroup.class})
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

	@Schema(title = "Availability in weekend, 1: Available, 0: Close")
	@NotNull(groups = {DefaultGroup.class})
	private Integer holidayAvailable;

	@Schema(title = "Max booking advance day")
	@NotNull(groups = {DefaultGroup.class})
	private Integer maxBookingAdvanceDay;

	@Schema(title = "Min booking advance day")
	@NotNull(groups = {DefaultGroup.class})
	private Integer minBookingAdvanceDay;

	@Schema(title = "Maximum reservation day")
	@NotNull(groups = {DefaultGroup.class})
	private Integer maxReservationDay;

	@Schema(title = "Minimum reservation day")
	@NotNull(groups = {DefaultGroup.class})
	private Integer minReservationDay;

	@Schema(title = "Maximum number of technician")
	@NotNull(groups = {DefaultGroup.class})
	private Integer maxTechnicianNumber;

	@Schema(title = "Price per technician")
	@NotNull(groups = {DefaultGroup.class})
	private BigDecimal technicianPrice;
}
