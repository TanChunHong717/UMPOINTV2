package my.edu.um.umpoint.modules.client.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * UserToken
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-01
 */
@Data
@TableName("cli_token")
public class CliTokenEntity {

    /**
     * ID
     */
	@TableId
	private Long id;
	/**
	* User ID
	*/
	private Long userId;
	/**
	* token
	*/
	private String token;
	/**
	* Expired date
	*/
	private Date expireDate;
	/**
	* Update date
	*/
	private Date updateDate;

}