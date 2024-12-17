package my.edu.um.umpoint.modules.client.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
	 * Real Name
	 */
	private String realName;
	/**
	 * Password
	 */
	private String password;
	/**
	 * Type
	 */
	private String type;
	/**
	* Mobile
	*/
	private String mobile;
	/**
	* Email
	*/
	private String email;
	/**
	 * Address
	 */
	private String address;
	/**
	 * Matric Number
	 */
	private String matricNumber;
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
	@TableField(fill = FieldFill.INSERT)
	private Date createDate;

}