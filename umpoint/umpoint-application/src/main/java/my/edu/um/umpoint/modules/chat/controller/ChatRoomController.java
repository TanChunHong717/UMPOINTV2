package my.edu.um.umpoint.modules.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.ChatConstant;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.exception.BadHttpRequestException;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.ExcelUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.accommodation.dto.AccAccommodationDTO;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
import my.edu.um.umpoint.modules.chat.dto.ChatRoomDTO;
import my.edu.um.umpoint.modules.chat.excel.ChatRoomExcel;
import my.edu.um.umpoint.modules.chat.service.ChatMessageService;
import my.edu.um.umpoint.modules.chat.service.ChatRoomService;
import my.edu.um.umpoint.modules.service.dto.SvcServiceDTO;
import my.edu.um.umpoint.modules.service.service.SvcServiceService;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.dao.InvalidResourceUsageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Chat room
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
@RestController
@RequestMapping("chat/room")
@Tag(name = "Chat room")
public class ChatRoomController{
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private SpcSpaceService spcSpaceService;
    @Autowired
    private SvcServiceService svcServiceService;
    @Autowired
    private AccAccommodationService accAccommodationService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters(
        {
            @Parameter(
                name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY,
                required = true, ref = "int"
            ),
            @Parameter(
                name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,
                required = true, ref = "int"
            ),
            @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(
                name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY,
                ref = "String"
            )
        }
    )
    @RequiresPermissions("chat:room:page")
    public Result<PageData<ChatRoomDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<ChatRoomDTO> page = chatRoomService.page(params);

        return new Result<PageData<ChatRoomDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("chat:room:info")
    public Result<ChatRoomDTO> get(@PathVariable("id") Long id){
        ChatRoomDTO data = chatRoomService.get(id);

        return new Result<ChatRoomDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Get Room ID")
    @LogOperation("Get Room ID")
    @RequiresPermissions("chat:room:getroom")
    @Parameters(
        {
            @Parameter(
                name = "facilityType", description = "Facility type, one of space/accommodation/service",
                in = ParameterIn.QUERY, required = true, ref = "String"
            ),
            @Parameter(
                name = "facilityId", description = "Facility ID",
                in = ParameterIn.QUERY, required = true, ref = "Long"
            )
        }
    )
    public Result getRoom(@RequestBody Map<String, Object> request) throws InvalidResourceUsageException{
        // param validation
        if (
            request.get("facilityType") == null ||
            request.get("facilityId") == null
        )
            throw new InvalidResourceUsageException("Missing parameter");
        if (
            !(request.get("facilityType") instanceof String) ||
            Arrays.stream(ChatConstant.FacilityType.values()).noneMatch((type) ->
                request.get("facilityType").toString().toUpperCase().equals(type.name())
            )
        )
            throw new BadHttpRequestException(400, "Invalid facility type");

        String facilityType = request.get("facilityType").toString();
        ChatConstant.FacilityType facilityEnum = ChatConstant.FacilityType.fromString(facilityType);
        Long facilityId;
        try {
            facilityId = Long.parseLong(request.get("facilityId").toString());
            switch (facilityEnum) {
                case SPACE -> {
                    SpcSpaceDTO space = spcSpaceService.get(facilityId);
                    if (space == null) throw new IllegalArgumentException();
                }
                case SERVICE -> {
                    SvcServiceDTO service = svcServiceService.get(facilityId);
                    if (service == null) throw new IllegalArgumentException();
                }
                case ACCOMMODATION -> {
                    AccAccommodationDTO acco = accAccommodationService.get(facilityId);
                    if (acco == null) throw new IllegalArgumentException();
                }
            }
        } catch (Exception e) {
            throw new BadHttpRequestException(400, "Invalid facility ID");
        }

        Long roomId = chatRoomService.getRoomByFacilityId(facilityEnum, facilityId);

        return new Result<Long>().ok(roomId);
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("chat:room:update")
    public Result update(@RequestBody ChatRoomDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        chatRoomService.update(dto);

        return new Result();
    }

    @GetMapping("export")
    @Operation(summary = "Export")
    @LogOperation("Export")
    @RequiresPermissions("chat:room:export")
    public void export(
        @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response
    ) throws Exception{
        List<ChatRoomDTO> list = chatRoomService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "Chat room", list, ChatRoomExcel.class);
    }

}
