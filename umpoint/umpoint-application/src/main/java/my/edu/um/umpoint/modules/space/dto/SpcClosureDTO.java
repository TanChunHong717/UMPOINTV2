package my.edu.um.umpoint.modules.space.dto;

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
import java.sql.Time;
import java.util.Date;

/**
 * Space Closure Period
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Data
@Schema(name = "Space Closure Period")
public class SpcClosureDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 1L;

	@SchemaProperty(name = "ID")
	@Null(groups = {AddGroup.class})
	@NotNull(groups = {UpdateGroup.class})
	private Long id;

	@SchemaProperty(name = "Space ID")
	@NotNull(groups = {DefaultGroup.class})
	private Long spaceId;

	@SchemaProperty(name = "Start day of booking")
	@NotNull(groups = {DefaultGroup.class})
	private Date startDay;

	@SchemaProperty(name = "End day of booking")
	@NotNull(groups = {DefaultGroup.class})
	private Date endDay;

	@SchemaProperty(name = "Start time of booking in a day")
	@NotNull(groups = {DefaultGroup.class})
	private Time startTime;

	@SchemaProperty(name = "End time of booking in a day")
	@NotNull(groups = {DefaultGroup.class})
	private Time endTime;

	@SchemaProperty(name = "Recur on Monday, 0:No, 1:Yes")
	@NotNull(groups = {DefaultGroup.class})
	private Integer recurMonday;

	@SchemaProperty(name = "Recur on Tuesday, 0:No, 1:Yes")
	@NotNull(groups = {DefaultGroup.class})
	private Integer recurTuesday;

	@SchemaProperty(name = "Recur on Wednesday, 0:No, 1:Yes")
	@NotNull(groups = {DefaultGroup.class})
	private Integer recurWednesday;

	@SchemaProperty(name = "Recur on Thursday, 0:No, 1:Yes")
	@NotNull(groups = {DefaultGroup.class})
	private Integer recurThursday;

	@SchemaProperty(name = "Recur on Friday, 0:No, 1:Yes")
	@NotNull(groups = {DefaultGroup.class})
	private Integer recurFriday;

	@SchemaProperty(name = "Recur on Saturday, 0:No, 1:Yes")
	@NotNull(groups = {DefaultGroup.class})
	private Integer recurSaturday;

	@SchemaProperty(name = "Recur on Sunday, 0:No, 1:Yes")
	@NotNull(groups = {DefaultGroup.class})
	private Integer recurSunday;
}
