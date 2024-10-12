package my.edu.um.umpoint.modules.space.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Space Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-11
 */
@Data
@TableName("spc_booking_technician")
public class SpcBookingTechnicianEntity {
    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * Booking ID
     */
    private Long bookingId;
    /**
     * Technician ID
     */
    private Long technicianId;
    /**
     * Technician name
     */
    @TableField(exist = false)
    private String technicianName;
}
