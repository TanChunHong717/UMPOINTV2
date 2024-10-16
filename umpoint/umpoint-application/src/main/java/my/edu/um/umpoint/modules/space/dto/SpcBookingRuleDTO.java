package my.edu.um.umpoint.modules.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;

import java.io.Serial;
import java.io.Serializable;

import java.math.BigDecimal;
import java.sql.Time;

/**
 * Space Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-11
 */
@Data
@Schema(title = "Space Booking Rule")
@EqualsAndHashCode
public class SpcBookingRuleDTO implements Serializable {
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

	@Schema(title = "Availability in weekend, 1: Available, 0: Close")
	@NotNull(groups = {DefaultGroup.class})
	private Integer holidayAvailable;

	@Schema(title = "Start time in a day when booking is allow,range: (0,24)")
	@NotNull(groups = {DefaultGroup.class})
	private Time startTime;

	@Schema(title = "End time in a day when booking is allow,range: (0,24)")
	@NotNull(groups = {DefaultGroup.class})
	private Time endTime;

	@Schema(title = "Max booking advance day")
	@NotNull(groups = {DefaultGroup.class})
	private Integer maxBookingAdvanceDay;

	@Schema(title = "Min booking advance day")
	@NotNull(groups = {DefaultGroup.class})
	private Integer minBookingAdvanceDay;

	@Schema(title = "Maximum reservation days")
	@NotNull(groups = {DefaultGroup.class})
	private Integer maxReservationDays;

	@Schema(title = "Minimum booking hours per day")
	@NotNull(groups = {DefaultGroup.class})
	private Integer minBookingHours;

	@Schema(title = "Maximum number of technician")
	@NotNull(groups = {DefaultGroup.class})
	private Integer maxTechnicianNumber;

	@Schema(title = "Price per technician")
	@NotNull(groups = {DefaultGroup.class})
	private BigDecimal technicianPrice;
}
