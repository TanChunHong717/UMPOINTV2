package my.edu.um.umpoint.modules.accommodation.dto;

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
 * Accommodation Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@Schema(name = "Accommodation Booking Rule")
@EqualsAndHashCode
public class AccBookingRuleDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 9187174069158066308L;

	@SchemaProperty(name = "ID")
	@Null(groups = {AddGroup.class})
	@NotNull(groups = {UpdateGroup.class})
	private Long id;

	@SchemaProperty(name = "0:Automatic approve 1: Require admin approve")
	@NotNull(groups = {DefaultGroup.class})
	private Integer approvalRequired;

	@SchemaProperty(name = "0:Staff not allow to book 1:Staff allow to book")
	@NotNull(groups = {DefaultGroup.class})
	private Integer openForStaff;

	@SchemaProperty(name = "0:Student not allow to book 1:Student allow to book")
	@NotNull(groups = {DefaultGroup.class})
	private Integer openForStudent;

	@SchemaProperty(name = "0:Automatic approve 1: Require admin approve")
	@NotNull(groups = {DefaultGroup.class})
	private Integer openForPublic;

	@SchemaProperty(name = "Availability in weekend, 1: Available, 0: Close")
	@NotNull(groups = {DefaultGroup.class})
	private Integer holidayAvailable;

	@SchemaProperty(name = "Days open prior booking")
	@NotNull(groups = {DefaultGroup.class})
	private Integer openDaysPriorBooking;

	@SchemaProperty(name = "Days close before booking")
	@NotNull(groups = {DefaultGroup.class})
	private Integer closeDaysBeforeBooking;

	@SchemaProperty(name = "Maximum reservation days")
	@NotNull(groups = {DefaultGroup.class})
	private Integer maxReservationDays;

	@SchemaProperty(name = "Minimum booking days")
	@NotNull(groups = {DefaultGroup.class})
	private Integer minBookingDays;

	@SchemaProperty(name = "Maximum number of technician")
	@NotNull(groups = {DefaultGroup.class})
	private Integer maxTechnicianNumber;
}
