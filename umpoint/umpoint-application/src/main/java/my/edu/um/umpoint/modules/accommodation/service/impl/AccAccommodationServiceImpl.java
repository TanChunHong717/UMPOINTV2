package my.edu.um.umpoint.modules.accommodation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.annotation.DataFilter;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.accommodation.dao.AccAccommodationDao;
import my.edu.um.umpoint.modules.accommodation.dao.AccTagDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccAccommodationDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccAccommodationEntity;
import my.edu.um.umpoint.modules.accommodation.entity.AccAccommodationTagEntity;
import my.edu.um.umpoint.modules.accommodation.entity.AccImageEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
import cn.hutool.core.util.StrUtil;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationTagService;
import my.edu.um.umpoint.modules.accommodation.service.AccImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private AccTagDao accTagDao;

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
        updateAccommodationTag(dto);
        updateAccommodationImage(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AccAccommodationDTO dto) {
        super.update(dto);
        updateAccommodationTag(dto);
        updateAccommodationImage(dto);
    }

    private void updateAccommodationImage(AccAccommodationDTO dto) {
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

    private void updateAccommodationTag(AccAccommodationDTO dto) {
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