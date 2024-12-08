package my.edu.um.umpoint.modules.client.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * User
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-01
 */
@Data
@TableName("cli_user")
public class CliUserEntity {
    /**
     * ID
     */
	@TableId
	private Long id;
	/**
	 * Status, 0:Disable, 1:Enabled
	 */
	private Integer status;
	/**
	* Username
	*/
	private String username;
	/**
	* Mobile
	*/
	private String mobile;
	/**
	* Password
	*/
	private String password;
	/**
	* Email
	*/
	private String email;
	/**
	 * Type
	 */
	private String type;
	/**
	 * Have permission to book space
	 */
	private Integer spacePermission;
	/**
	 * Have permission to book space
	 */
	private Integer servicePermission;
	/**
	 * Have permission to book accommodation
	 */
	private Integer accommodationPermission;
	/**
	* Create date
	*/
	private Date createDate;

}