package my.edu.um.umpoint.modules.space.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Space Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
@TableName("spc_availability")
public class AvailabilityEntity {

    /**
     * ID
     */
	@TableId
	private Long id;
    /**
     * Space ID
     */
	private Long spaceId;
    /**
     * Year
     */
	private BigDecimal year;
    /**
     * Availability of space, consist of 366*24 bit, 1 represent available in specific hour in one year
     */
	private byte[] availability;
}