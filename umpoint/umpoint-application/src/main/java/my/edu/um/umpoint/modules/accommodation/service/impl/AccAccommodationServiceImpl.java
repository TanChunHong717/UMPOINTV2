package my.edu.um.umpoint.modules.accommodation.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.annotation.DataFilter;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.accommodation.dao.AccAccommodationDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccAccommodationDTO;
import my.edu.um.umpoint.modules.accommodation.dto.AccBookingRuleDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccAccommodationEntity;
import my.edu.um.umpoint.modules.accommodation.entity.AccAccommodationTagEntity;
import my.edu.um.umpoint.modules.accommodation.entity.AccImageEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationTagService;
import my.edu.um.umpoint.modules.accommodation.service.AccBookingRuleService;
import my.edu.um.umpoint.modules.accommodation.service.AccImageService;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Accommodation
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Service
public class AccAccommodationServiceImpl extends CrudServiceImpl<AccAccommodationDao, AccAccommodationEntity, AccAccommodationDTO> implements AccAccommodationService {

    @Autowired
    private AccAccommodationTagService accAccommodationTagService;

    @Autowired
    private AccImageService accImageService;

    @Autowired
    private AccBookingRuleService accBookingRuleService;

    @Autowired
    private SysDeptService sysDeptService;

    @Override
    public QueryWrapper<AccAccommodationEntity> getWrapper(Map<String, Object> params){
        paramsToLike(params, "name");
        String name = (String)params.get("name");

        QueryWrapper<AccAccommodationEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), "name", name);

        return wrapper;
    }

    @Override
    @DataFilter(tableAlias = "a")
    public PageData<AccAccommodationDTO> accommodationPage(Map<String, Object> params) {
        paramsToLike(params, "name");
        if (params.containsKey(Constant.DEPT_ID))
            params.put("deptIdList", sysDeptService.getSubDeptIdList(Long.parseLong((String) params.get(Constant.DEPT_ID))));

        IPage<AccAccommodationEntity> page = getPage(params, Constant.CREATE_DATE, false);

        List<AccAccommodationEntity> list = baseDao.getAccommodationList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    @DataFilter(tableAlias = "a")
    public PageData<AccAccommodationDTO> bookingRulePage(Map<String, Object> params) {
        paramsToLike(params, "name");

        IPage<AccAccommodationEntity> page = getPage(params, Constant.CREATE_DATE, false);

        List<AccAccommodationEntity> list = baseDao.getBookingRuleList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AccAccommodationDTO dto) {
        super.save(dto);
        updateAccommodationImage(dto);
        updateAccommodationTag(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AccAccommodationDTO dto) {
        if (Objects.requireNonNull(SecurityUser.getSubject()).isPermitted("accommodation:booking-rule:update"))
            updateBookingRule(dto);
        super.update(dto);
        updateAccommodationImage(dto);
        updateAccommodationTag(dto);
    }

    private void updateBookingRule(AccAccommodationDTO dto) {
        AccBookingRuleDTO accBookingRuleDTO = dto.getAccBookingRuleDTO();
        if (accBookingRuleDTO != null) {
            if (dto.getBookingRuleId() == null) {
                //Have no booking rule yet
                //Create new booking rule
                accBookingRuleService.save(accBookingRuleDTO);
                dto.setBookingRuleId(accBookingRuleDTO.getId());
            } else
                accBookingRuleService.update(accBookingRuleDTO);
        }
    }

    private void updateAccommodationImage(AccAccommodationDTO dto) {
        if (dto.getAccImageDTOList() != null) {
            List<AccImageEntity> imageEntityList = dto.getAccImageDTOList()
                    .stream()
                    .map((accImageDTO) -> {
                        AccImageEntity entity = new AccImageEntity();
                        entity.setId(accImageDTO.getId());
                        entity.setAccommodationId(dto.getId());
                        entity.setImageUrl(accImageDTO.getImageUrl());
                        return entity;
                    }).toList();

            accImageService.deleteByAccommodationId(dto.getId());
            accImageService.insertBatch(imageEntityList);
        }
    }

    private void updateAccommodationTag(AccAccommodationDTO dto) {
        if (dto.getAccTagDTOList() != null) {
            List<AccAccommodationTagEntity> tagEntityList = dto.getAccTagDTOList()
                    .stream()
                    .map((accTagDao) -> {
                        AccAccommodationTagEntity entity = new AccAccommodationTagEntity();
                        entity.setAccommodationId(dto.getId());
                        entity.setTagId(accTagDao.getId());
                        return entity;
                    }).toList();

            accAccommodationTagService.deleteByAccommodationId(dto.getId());
            accAccommodationTagService.insertBatch(tagEntityList);
        }
    }
}