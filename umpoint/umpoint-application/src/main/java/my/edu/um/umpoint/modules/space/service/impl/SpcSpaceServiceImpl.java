package my.edu.um.umpoint.modules.space.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.annotation.DataFilter;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.space.dao.SpcSpaceDao;
import my.edu.um.umpoint.modules.space.dto.SpcBookingRuleDTO;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.space.entity.SpcImageEntity;
import my.edu.um.umpoint.modules.space.entity.SpcSpaceEntity;
import my.edu.um.umpoint.modules.space.entity.SpcSpaceTagEntity;
import my.edu.um.umpoint.modules.space.service.SpcBookingRuleService;
import my.edu.um.umpoint.modules.space.service.SpcImageService;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import my.edu.um.umpoint.modules.space.service.SpcSpaceTagService;
import my.edu.um.umpoint.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Space
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Service
public class SpcSpaceServiceImpl extends CrudServiceImpl<SpcSpaceDao, SpcSpaceEntity, SpcSpaceDTO> implements SpcSpaceService {

    @Autowired
    private SpcSpaceTagService spcSpaceTagService;

    @Autowired
    private SpcImageService spcImageService;

    @Autowired
    private SpcBookingRuleService spcBookingRuleService;

    @Autowired
    private SysDeptService sysDeptService;

    @Override
    public QueryWrapper<SpcSpaceEntity> getWrapper(Map<String, Object> params){
        paramsToLike(params, "name");
        String name = (String)params.get("name");

        QueryWrapper<SpcSpaceEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), "name", name);

        return wrapper;
    }

    @Override
    @DataFilter(tableAlias = "s")
    public PageData<SpcSpaceDTO> spacePage(Map<String, Object> params) {
        paramsToLike(params, "name");
        if (params.containsKey(Constant.DEPT_ID))
            params.put("deptIdList", sysDeptService.getSubDeptIdList(Long.parseLong((String) params.get(Constant.DEPT_ID))));

        IPage<SpcSpaceEntity> page = getPage(params, Constant.CREATE_DATE, false);

        List<SpcSpaceEntity> list = baseDao.getSpaceList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    @DataFilter(tableAlias = "s")
    public PageData<SpcSpaceDTO> bookingRulePage(Map<String, Object> params) {
        paramsToLike(params, "name");

        IPage<SpcSpaceEntity> page = getPage(params, Constant.CREATE_DATE, false);

        List<SpcSpaceEntity> list = baseDao.getBookingRuleList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    public List<SpcSpaceDTO> listWithBookingRule() {
        List<Long> deptIdList = SecurityUser.getUser().getDeptIdList();

        QueryWrapper<SpcSpaceEntity> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("booking_rule_id");
        wrapper.in((deptIdList != null && !deptIdList.isEmpty()), "dept_id", deptIdList);
        wrapper.select("id", "name");

        return ConvertUtils.sourceToTarget(baseDao.selectList(wrapper), currentDtoClass());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SpcSpaceDTO dto) {
        super.save(dto);
        updateSpaceImage(dto);
        updateSpaceTag(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SpcSpaceDTO dto) {
        if (Objects.requireNonNull(SecurityUser.getSubject()).isPermitted("space:booking-rule:update"))
            updateBookingRule(dto);
        super.update(dto);
        updateSpaceImage(dto);
        updateSpaceTag(dto);
    }

    private void updateBookingRule(SpcSpaceDTO dto) {
        SpcBookingRuleDTO spcBookingRuleDTO = dto.getSpcBookingRuleDTO();
        if (spcBookingRuleDTO != null) {
            if (spcBookingRuleDTO.getId() == null)
                spcBookingRuleService.save(spcBookingRuleDTO);
            else
                spcBookingRuleService.update(spcBookingRuleDTO);
            dto.setBookingRuleId(spcBookingRuleDTO.getId());
        }
    }

    private void updateSpaceImage(SpcSpaceDTO dto) {
        if (dto.getSpcImageDTOList() != null) {
            List<SpcImageEntity> imageEntityList = dto.getSpcImageDTOList()
                    .stream()
                    .map((spcImageDTO) -> {
                        SpcImageEntity entity = new SpcImageEntity();
                        entity.setId(spcImageDTO.getId());
                        entity.setSpaceId(dto.getId());
                        entity.setImageUrl(spcImageDTO.getImageUrl());
                        return entity;
                    }).toList();

            spcImageService.deleteBySpaceId(dto.getId());
            spcImageService.insertBatch(imageEntityList);
        }
    }

    private void updateSpaceTag(SpcSpaceDTO dto) {
        if (dto.getSpcTagDTOList() != null) {
            List<SpcSpaceTagEntity> tagEntityList = dto.getSpcTagDTOList()
                    .stream()
                    .map((spcTagDao) -> {
                        SpcSpaceTagEntity entity = new SpcSpaceTagEntity();
                        entity.setSpaceId(dto.getId());
                        entity.setTagId(spcTagDao.getId());
                        return entity;
                    }).toList();

            spcSpaceTagService.deleteBySpaceId(dto.getId());
            spcSpaceTagService.insertBatch(tagEntityList);
        }
    }
}