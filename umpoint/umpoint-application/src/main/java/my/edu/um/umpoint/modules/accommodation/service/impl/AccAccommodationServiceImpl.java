package my.edu.um.umpoint.modules.accommodation.service.impl;

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
import cn.hutool.core.util.StrUtil;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationTagService;
import my.edu.um.umpoint.modules.accommodation.service.AccBookingRuleService;
import my.edu.um.umpoint.modules.accommodation.service.AccImageService;
import my.edu.um.umpoint.modules.space.dto.SpcBookingRuleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    @Override
    public QueryWrapper<AccAccommodationEntity> getWrapper(Map<String, Object> params){
        String name = (String)params.get("name");

        QueryWrapper<AccAccommodationEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), "name", name);

        return wrapper;
    }

    @Override
    @DataFilter(tableAlias = "a")
    public PageData<AccAccommodationDTO> page(Map<String, Object> params) {
        paramsToLike(params, "name");

        IPage<AccAccommodationEntity> page = getPage(params, Constant.CREATE_DATE, false);

        List<AccAccommodationEntity> list = baseDao.getList(params);

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
        updateBookingRule(dto);
        super.update(dto);
        updateAccommodationImage(dto);
        updateAccommodationTag(dto);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyDefaultBookingRule(Long[] ids) {
        List<AccAccommodationEntity> accAccommodationEntities = baseDao.selectBatchIds(Arrays.asList(ids));
        List<Long> bookingRuleIds = new ArrayList<>();

        for (AccAccommodationEntity accAccommodationEntity : accAccommodationEntities) {
            Long bookingRuleId = accAccommodationEntity.getBookingRuleId();
            if (bookingRuleId != null && bookingRuleId != 0)
                bookingRuleIds.add(bookingRuleId);
            accAccommodationEntity.setBookingRuleId(0L);
        }

        if (!bookingRuleIds.isEmpty())
            accBookingRuleService.deleteBatchIds(bookingRuleIds);
        updateBatchById(accAccommodationEntities);
    }

    private void updateBookingRule(AccAccommodationDTO dto) {
        AccBookingRuleDTO accBookingRuleDTO = dto.getAccBookingRuleDTO();
        if (accBookingRuleDTO != null) {
            AccBookingRuleDTO defaultBookingRuleDTO = accBookingRuleService.get(0L);
            if (dto.getBookingRuleId() == null) {
                //Have no booking rule yet
                if (accBookingRuleDTO.equals(defaultBookingRuleDTO)) {
                    //Apply default booking rule
                    dto.setBookingRuleId(0L);
                } else {
                    //Create new booking rule
                    accBookingRuleService.save(accBookingRuleDTO);
                    dto.setBookingRuleId(accBookingRuleDTO.getId());
                }
            } else if (dto.getBookingRuleId() == 0) {
                //Using default booking rule before
                if (!accBookingRuleDTO.equals(defaultBookingRuleDTO)) {
                    //Change booking rule, need to create new one
                    accBookingRuleService.save(accBookingRuleDTO);
                    dto.setBookingRuleId(accBookingRuleDTO.getId());
                }
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