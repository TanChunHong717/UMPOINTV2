package my.edu.um.umpoint.modules.space.dto;

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
import java.sql.Time;
import java.util.Date;

/**
 * Space Closure Period
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Data
@Schema(title = "Space Closure Period")
public class SpcClosureDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -2702607703982735056L;

	@Schema(title = "ID")
	@Null(groups = {AddGroup.class})
	@NotNull(groups = {UpdateGroup.class})
	private Long id;

	@Schema(title = "Space ID")
	@NotNull(groups = {DefaultGroup.class})
	private Long spaceId;

	@Schema(title = "Start day of booking")
	@NotNull(groups = {DefaultGroup.class})
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date startDay;

	@Schema(title = "End day of booking")
	@NotNull(groups = {DefaultGroup.class})
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date endDay;

	@Schema(title = "Start time of booking in a day")
	@NotNull(groups = {DefaultGroup.class})
	private Time startTime;

	@Schema(title = "End time of booking in a day")
	@NotNull(groups = {DefaultGroup.class})
	private Time endTime;

	@Schema(title = "Recur on Monday, 0:No, 1:Yes")
	@NotNull(groups = {DefaultGroup.class})
	private Integer recurMonday;

	@Schema(title = "Recur on Tuesday, 0:No, 1:Yes")
	@NotNull(groups = {DefaultGroup.class})
	private Integer recurTuesday;

	@Schema(title = "Recur on Wednesday, 0:No, 1:Yes")
	@NotNull(groups = {DefaultGroup.class})
	private Integer recurWednesday;

	@Schema(title = "Recur on Thursday, 0:No, 1:Yes")
	@NotNull(groups = {DefaultGroup.class})
	private Integer recurThursday;

	@Schema(title = "Recur on Friday, 0:No, 1:Yes")
	@NotNull(groups = {DefaultGroup.class})
	private Integer recurFriday;

	@Schema(title = "Recur on Saturday, 0:No, 1:Yes")
	@NotNull(groups = {DefaultGroup.class})
	private Integer recurSaturday;

	@Schema(title = "Recur on Sunday, 0:No, 1:Yes")
	@NotNull(groups = {DefaultGroup.class})
	private Integer recurSunday;

	@Schema(title = "Space name")
	private String name;
}
