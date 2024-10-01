package my.edu.um.umpoint.modules.client.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.modules.client.dto.CliTokenDTO;
import my.edu.um.umpoint.modules.client.entity.CliTokenEntity;

/**
 * UserToken
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-01
 */
public interface CliTokenService extends CrudService<CliTokenEntity, CliTokenDTO> {

    Result createToken(Long userId);

    void logout(Long userId);

}