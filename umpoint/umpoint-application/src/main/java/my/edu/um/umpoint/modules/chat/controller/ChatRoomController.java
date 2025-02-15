package my.edu.um.umpoint.modules.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.ChatConstant;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.exception.BadHttpRequestException;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.JsonUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.accommodation.dto.AccAccommodationDTO;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
import my.edu.um.umpoint.modules.chat.dto.ChatRoomDTO;
import my.edu.um.umpoint.modules.chat.service.ChatRoomService;
import my.edu.um.umpoint.modules.client.service.CliUserService;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.service.dto.SvcServiceDTO;
import my.edu.um.umpoint.modules.service.service.SvcServiceService;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.dao.InvalidResourceUsageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
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
    private SpcSpaceService spcSpaceService;

    @Autowired
    private SvcServiceService svcServiceService;

    @Autowired
    private AccAccommodationService accAccommodationService;

    @Autowired
    private CliUserService cliUserService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private ChatMessageController chatMessageController;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters(
        {
            @Parameter(
                name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY,
                schema = @Schema(type = "int")
            ),
            @Parameter(
                name = "type", description = "Type of page to use (user/dept), only for admin", in = ParameterIn.QUERY,
                required = false, schema = @Schema(type = "string")
            )
        }
    )
    @RequiresPermissions("chat:room:page")
    public Result<PageData<ChatRoomDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<ChatRoomDTO> page;
        int pageNum = Integer.parseInt((String) params.getOrDefault(Constant.PAGE, "1"));
        if (pageNum < 0) pageNum = 1;

        Map<String, Object> queryParams = new HashMap<>(Map.of(
            Constant.PAGE, Integer.toString(pageNum),
            Constant.LIMIT, "20"
        ));

        UserDetail user = SecurityUser.getUser();
        if (user.getSuperAdmin() != null) {
            // admin
            page = switch (params.getOrDefault("type", "user").toString()) {
                case "user" -> {
                    queryParams.put("adminId", user.getId());
                    yield chatRoomService.listUserRoomPage(queryParams);
                }
                case "dept" -> chatRoomService.listAdminDepartmentRoomPage(queryParams);
                default -> throw new BadHttpRequestException("Invalid search room type");
            };
        } else {
            // user
            queryParams.put("userId", user.getId());
            page = chatRoomService.listUserRoomPage(queryParams);
        }

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
                in = ParameterIn.QUERY, required = true, schema = @Schema(type = "string")
            ),
            @Parameter(
                name = "facilityId", description = "Facility ID",
                in = ParameterIn.QUERY, required = true, schema = @Schema(type = "int")
            ),
            @Parameter(
                name = "userId", description = "User ID",
                in = ParameterIn.QUERY, schema = @Schema(type = "int")
            )
        }
    )
    public Result getRoomByFacilityId(@RequestBody Map<String, Object> request) throws InvalidResourceUsageException{
        // param validation
        if (
            request.get("facilityType") == null ||
            request.get("facilityId") == null
        )
            throw new BadHttpRequestException(400, "Missing parameter");
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
                default -> throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            throw new BadHttpRequestException(400, "Invalid facility ID");
        }

        UserDetail user = SecurityUser.getUser();

        Long roomId;
        if (user.getSuperAdmin() != null) {
            // validate client user ID
            if (
                request.get("userId") == null ||
                cliUserService.get((Long) request.get("userId")) == null
            )
                throw new BadHttpRequestException(400, "Missing parameter");

            roomId =
                chatRoomService.getRoomByFacilityId((Long) request.get("userId"), user.getId(), facilityEnum,
                    facilityId
                );
        } else {
            roomId = chatRoomService.getRoomByFacilityId(user.getId(), facilityEnum, facilityId);
        }

        return new Result<Long>().ok(roomId);
    }

    @PostMapping("{id}/assign")
    @Operation(summary = "Assign room to current admin")
    @LogOperation("Assign room to current admin")
    @RequiresPermissions("chat:room:assignAdmin")
    public Result<ChatRoomDTO> assignAdmin(@PathVariable("id") Long roomId){
        UserDetail user = SecurityUser.getUser();
        if (user.getSuperAdmin() == null) {
            throw new BadHttpRequestException(401, "Invalid permission");
        }

        chatRoomService.assignAdminId(roomId);
        chatRoomService.stopRoomAutoReply(roomId);
        messagingTemplate.convertAndSend(
            "/queue/room/" + roomId,
            JsonUtils.toJsonStringWithStringId(
                chatMessageController.buildSystemMessage(
                    roomId,
                    "This chat is handed over to human agent."
                )
            )
        );

        return new Result();
    }

    @PostMapping("{id}/close")
    @Operation(summary = "Close room")
    @LogOperation("Close room")
    @RequiresPermissions("chat:room:info")
    public Result<ChatRoomDTO> closeRoom(@PathVariable("id") Long id){
        if (!chatRoomService.canChatInRoom(id)) {
            throw new BadHttpRequestException(401, "Cannot close already closed room");
        }

        chatRoomService.closeRoom(id);

        return new Result();
    }

    @PostMapping("{id}/resolve")
    @Operation(summary = "Resolve room")
    @LogOperation("Resolve room")
    @RequiresPermissions("chat:room:info")
    public Result<ChatRoomDTO> resolveRoom(@PathVariable("id") Long id){
        if (!chatRoomService.canChatInRoom(id)) {
            throw new BadHttpRequestException(401, "Cannot resolve already closed room");
        }

        chatRoomService.resolveRoom(id);

        return new Result();
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

    public static boolean validateUserInChat(ChatRoomDTO chatRoomDTO, UserDetail user){
        if (user.getId() == null) return false;

        if (user.getSuperAdmin() == null) {
            // check user
            return chatRoomDTO.getInitiateUserId().equals(user.getId());
        } else {
            // TODO: check facility department is same as admin department
            return true; //chatRoomDTO.getFacilityId().equals(user.getDeptId());
        }
    }
}
