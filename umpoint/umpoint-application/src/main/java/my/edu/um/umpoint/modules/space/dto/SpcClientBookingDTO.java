package my.edu.um.umpoint.modules.space.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.Optional;

@Data
@Schema(name = "Space Booking")
public class SpcClientBookingDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1234L;


    @Schema(title = "Space ID")
    @SchemaProperty(name = "Space ID")
    @NotNull(message = "{id.require}", groups = {DefaultGroup.class})
    private Long spaceId;

    @Schema(title = "Description of the event or purpose for the booking")
    @SchemaProperty(name = "Description of the event or purpose for the booking")
    @NotEmpty(groups = {DefaultGroup.class})
    private String event;

    @Schema(title = "Start day of booking")
    @SchemaProperty(name = "Start day of booking")
    @NotNull(groups = {DefaultGroup.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDay;

    @Schema(title = "End day of booking")
    @SchemaProperty(name = "End day of booking")
    @NotNull(groups = {DefaultGroup.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDay;

    @Schema(title = "Start time of booking in a day")
    @SchemaProperty(name = "Start time of booking in a day")
    @NotNull(groups = {DefaultGroup.class})
    private Time startTime;

    @Schema(title = "End time of booking in a day")
    @SchemaProperty(name = "End time of booking in a day")
    @NotNull(groups = {DefaultGroup.class})
    private Time endTime;

    @Schema(title = "Number of additional technician(s) if required")
    @SchemaProperty(name = "Number of additional technician(s) if required")
    @PositiveOrZero(message = "Number of technicians must be positive value")
    private Optional<Integer> technicianNumber;
}
