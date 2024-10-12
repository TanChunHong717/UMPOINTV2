package my.edu.um.umpoint.modules.space.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Space Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-11
 */
@Data
@Schema(name = "Space Booking Technician")
public class SpcBookingTechnicianDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -78769910842793281L;

    @SchemaProperty(name = "ID")
    private Long id;

    @SchemaProperty(name = "Booking ID")
    private Long bookingId;

    @SchemaProperty(name = "Technician ID")
    private Long technicianId;

    @SchemaProperty(name = "Technician name")
    private String technicianName;
}
