package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.annotation.DataFilter;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.space.dao.SpcSpaceDao;
import my.edu.um.umpoint.modules.space.dto.SpcBookingRuleDTO;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.space.entity.SpcImageEntity;
import my.edu.um.umpoint.modules.space.entity.SpcSpaceEntity;
import my.edu.um.umpoint.modules.space.entity.SpcSpaceTagEntity;
import my.edu.um.umpoint.modules.space.service.SpcBookingRuleService;
import my.edu.um.umpoint.modules.space.service.SpcImageService;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import cn.hutool.core.util.StrUtil;
import my.edu.um.umpoint.modules.space.service.SpcSpaceTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    @Override
    public QueryWrapper<SpcSpaceEntity> getWrapper(Map<String, Object> params){
        String name = (String)params.get("name");

        QueryWrapper<SpcSpaceEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), "name", name);

        return wrapper;
    }

    @Override
    @DataFilter(tableAlias = "s")
    public PageData<SpcSpaceDTO> page(Map<String, Object> params) {
        paramsToLike(params, "name");

        IPage<SpcSpaceEntity> page = getPage(params, Constant.CREATE_DATE, false);

        List<SpcSpaceEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
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
        updateBookingRule(dto);
        super.update(dto);
        updateSpaceImage(dto);
        updateSpaceTag(dto);
    }

    private void updateBookingRule(SpcSpaceDTO dto) {
        SpcBookingRuleDTO spcBookingRuleDTO = dto.getSpcBookingRuleDTO();
        if (spcBookingRuleDTO != null) {
            if (dto.getBookingRuleId() == null) {
                spcBookingRuleService.save(spcBookingRuleDTO);
                dto.setBookingRuleId(spcBookingRuleDTO.getId());
            } else
                spcBookingRuleService.update(spcBookingRuleDTO);
        }
    }

    private void updateSpaceImage(SpcSpaceDTO dto) {
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

    private void updateSpaceTag(SpcSpaceDTO dto) {
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