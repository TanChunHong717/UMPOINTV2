package my.edu.um.umpoint.modules.client.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.client.dto.CliUserDTO;
import my.edu.um.umpoint.modules.client.entity.CliUserEntity;

/**
 * User
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-01
 */
public interface CliUserService extends CrudService<CliUserEntity, CliUserDTO> {

    CliUserDTO getByUsername(String username);

}