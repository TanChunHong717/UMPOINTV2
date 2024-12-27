package my.edu.um.umpoint.modules.security.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class UserDetail implements Serializable{

    @Serial
    private static final long serialVersionUID = 8470528520565069794L;

    //General
    private Long id;
    private String username;
    private String password;
    private String mobile;
    private String email;

    //Admin
    private String realName;
    private String headUrl;
    private Integer gender;
    private Long deptId;
    private Integer status;
    private Integer superAdmin;
    private List<Long> deptIdList;

    //Client
    private String type;
    private Integer spacePermission;
    private Integer servicePermission;
    private Integer accommodationPermission;

    //Mailing
    private Integer emailValidated;
    private Integer receiveEmail;
}