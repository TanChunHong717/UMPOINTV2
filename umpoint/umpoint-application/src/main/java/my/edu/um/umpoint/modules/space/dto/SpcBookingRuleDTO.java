package my.edu.um.umpoint.modules.space.dto;

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

import java.sql.Time;

/**
 * Space Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-11
 */
@Data
@Schema(name = "Space Booking Rule")
@EqualsAndHashCode
public class SpcBookingRuleDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 3105155482684743857L;

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

	@SchemaProperty(name = "Start time in a day when booking is allow,range: (0,24)")
	@NotNull(groups = {DefaultGroup.class})
	private Time startTime;

	@SchemaProperty(name = "End time in a day when booking is allow,range: (0,24)")
	@NotNull(groups = {DefaultGroup.class})
	private Time endTime;

	@SchemaProperty(name = "Days open prior booking")
	@NotNull(groups = {DefaultGroup.class})
	private Integer openDaysPriorBooking;

	@SchemaProperty(name = "Days close after booking")
	@NotNull(groups = {DefaultGroup.class})
	private Integer closeDaysAfterBooking;

	@SchemaProperty(name = "Maximum reservation days")
	@NotNull(groups = {DefaultGroup.class})
	private Integer maxReservationDays;

	@SchemaProperty(name = "Minimum booking hours per day")
	@NotNull(groups = {DefaultGroup.class})
	private Integer minBookingHours;
}
