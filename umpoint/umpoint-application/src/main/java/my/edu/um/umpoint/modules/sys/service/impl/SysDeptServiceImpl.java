package my.edu.um.umpoint.modules.sys.service.impl;

import com.qiniu.util.StringUtils;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.exception.ErrorCode;
import my.edu.um.umpoint.common.exception.RenException;
import my.edu.um.umpoint.common.service.impl.BaseServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.common.utils.TreeUtils;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.sys.dao.SysDeptDao;
import my.edu.um.umpoint.modules.sys.dao.SysUserDao;
import my.edu.um.umpoint.modules.sys.dto.SysDeptDTO;
import my.edu.um.umpoint.modules.sys.entity.SysDeptEntity;
import my.edu.um.umpoint.modules.sys.enums.SuperAdminEnum;
import my.edu.um.umpoint.modules.sys.service.SysDeptService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {
    private final SysUserDao sysUserDao;

    @Override
    public List<SysDeptDTO> list(Map<String, Object> params) {
        UserDetail user = SecurityUser.getUser();
        if (!params.containsKey("public") && user.getSuperAdmin() == SuperAdminEnum.NO.value()) {
            params.put("deptIdList", getSubDeptIdList(user.getDeptId()));
        }

        List<SysDeptEntity> entityList = baseDao.getList(params);

        List<SysDeptDTO> dtoList = ConvertUtils.sourceToTarget(entityList, SysDeptDTO.class);

        return TreeUtils.build(dtoList);
    }

    @Override
    public SysDeptDTO get(Long id) {
        if (id == null) {
            return null;
        }

        SysDeptEntity entity = baseDao.getById(id);

        return ConvertUtils.sourceToTarget(entity, SysDeptDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysDeptDTO dto) {
        SysDeptEntity entity = ConvertUtils.sourceToTarget(dto, SysDeptEntity.class);

        entity.setPids(getPidList(entity.getPid()));
        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysDeptDTO dto) {
        SysDeptEntity entity = ConvertUtils.sourceToTarget(dto, SysDeptEntity.class);

        if (entity.getId().equals(entity.getPid())) {
            throw new RenException(ErrorCode.SUPERIOR_DEPT_ERROR);
        }

        List<Long> subDeptList = getSubDeptIdList(entity.getId());
        if (subDeptList.contains(entity.getPid())) {
            throw new RenException(ErrorCode.SUPERIOR_DEPT_ERROR);
        }

        entity.setPids(getPidList(entity.getPid()));
        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        List<Long> subList = getSubDeptIdList(id);
        if (subList.size() > 1) {
            throw new RenException(ErrorCode.DEPT_SUB_DELETE_ERROR);
        }

        int count = sysUserDao.getCountByDeptId(id);
        if (count > 0) {
            throw new RenException(ErrorCode.DEPT_USER_DELETE_ERROR);
        }

        baseDao.deleteById(id);
    }

    @Override
    public List<Long> getSubDeptIdList(Long id) {
        List<Long> deptIdList = baseDao.getSubDeptIdList("%" + id + "%");
        deptIdList.add(id);

        return deptIdList;
    }

    private String getPidList(Long pid) {
        if (Constant.DEPT_ROOT.equals(pid)) {
            return Constant.DEPT_ROOT + "";
        }

        List<SysDeptEntity> deptList = baseDao.getIdAndPidList();

        Map<Long, SysDeptEntity> map = new HashMap<>(deptList.size());
        for (SysDeptEntity entity : deptList) {
            map.put(entity.getId(), entity);
        }

        List<Long> pidList = new ArrayList<>();
        getPidTree(pid, map, pidList);

        return StringUtils.join(pidList, ",");
    }

    private void getPidTree(Long pid, Map<Long, SysDeptEntity> map, List<Long> pidList) {
        if (Constant.DEPT_ROOT.equals(pid)) {
            return;
        }

        SysDeptEntity parent = map.get(pid);
        if (parent != null) {
            getPidTree(parent.getPid(), map, pidList);
        }

        pidList.add(pid);
    }
}
