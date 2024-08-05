package my.edu.um.umpoint.modules.security.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class UserDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 8470528520565069794L;

    private Long id;
    private String username;
    private String realName;
    private String headUrl;
    private Integer gender;
    private String email;
    private String mobile;
    private Long deptId;
    private String password;
    private Integer status;
    private Integer superAdmin;

    private List<Long> deptIdList;
}