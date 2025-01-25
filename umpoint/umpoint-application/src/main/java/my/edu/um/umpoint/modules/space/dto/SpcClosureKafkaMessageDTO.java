package my.edu.um.umpoint.modules.space.dto;

import lombok.Data;
import my.edu.um.umpoint.modules.security.user.UserDetail;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SpcClosureKafkaMessageDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4149924656306375692L;

    private SpcClosureDTO spcClosureDTO;

    private UserDetail userDetail;

    private Boolean isLast;
}
