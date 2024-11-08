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
import java.util.List;
import java.util.Optional;

@Data
@Schema(name = "Space Booking")
public class SpcClientBookingDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2657852531901294966L;

    @Schema(title = "Space ID")
    @NotNull(message = "{id.require}", groups = {DefaultGroup.class})
    private Long spaceId;

    @Schema(title = "Description of the event or purpose for the booking")
    @NotEmpty(groups = {DefaultGroup.class})
    private String event;

    @Schema(title = "Start day of booking")
    @NotNull(groups = {DefaultGroup.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDay;

    @Schema(title = "End day of booking")
    @NotNull(groups = {DefaultGroup.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDay;

    @Schema(title = "Start time of booking in a day")
    @NotNull(groups = {DefaultGroup.class})
    private Time startTime;

    @Schema(title = "End time of booking in a day")
    @NotNull(groups = {DefaultGroup.class})
    private Time endTime;

    @Schema(title = "Number of additional technician(s) if required")
    @PositiveOrZero(groups = {DefaultGroup.class}) // Null element is consider valid
    private Integer technicianNumber;

    @Schema(title = "Attachment dto list")
    private List<SpcBookingAttachmentDTO> attachments;
}
