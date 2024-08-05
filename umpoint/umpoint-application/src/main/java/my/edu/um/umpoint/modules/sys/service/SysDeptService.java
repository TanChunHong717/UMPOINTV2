package my.edu.um.umpoint.modules.sys.service;

import my.edu.um.umpoint.common.service.BaseService;
import my.edu.um.umpoint.modules.sys.dto.SysDeptDTO;
import my.edu.um.umpoint.modules.sys.entity.SysDeptEntity;

import java.util.List;
import java.util.Map;

public interface SysDeptService extends BaseService<SysDeptEntity> {

	List<SysDeptDTO> list(Map<String, Object> params);

	SysDeptDTO get(Long id);

	void save(SysDeptDTO dto);

	void update(SysDeptDTO dto);

	void delete(Long id);

	List<Long> getSubDeptIdList(Long id);
}