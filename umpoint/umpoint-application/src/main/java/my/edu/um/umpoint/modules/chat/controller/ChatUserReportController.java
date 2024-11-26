package my.edu.um.umpoint.modules.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.exception.BadHttpRequestException;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.ExcelUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.chat.dto.ChatUserReportDTO;
import my.edu.um.umpoint.modules.chat.excel.ChatUserReportExcel;
import my.edu.um.umpoint.modules.chat.service.ChatMessageService;
import my.edu.um.umpoint.modules.chat.service.ChatUserReportService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * Report user in chat
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-11-25
 */
@RestController
@RequestMapping("chat/report")
@Tag(name = "Report user in chat")
public class ChatUserReportController{
    @Autowired
    private ChatUserReportService chatUserReportService;
    @Autowired
    private ChatMessageController chatMessageController;

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
    @RequiresPermissions("chat:report:page")
    public Result<PageData<ChatUserReportDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<ChatUserReportDTO> page = chatUserReportService.page(params);

        return new Result<PageData<ChatUserReportDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("chat:report:info")
    public Result<ChatUserReportDTO> get(@PathVariable("id") Long id){
        ChatUserReportDTO data = chatUserReportService.get(id);

        return new Result<ChatUserReportDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("chat:report:save")
    public Result save(@RequestBody ChatUserReportDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        if (dto.getMessageId() != null &&
            !chatMessageController.validateChatMessageId(dto.getChatRoomId(), dto.getMessageId())) {
            throw new BadHttpRequestException(400, "Message is not in chat room");
        }

        chatUserReportService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("chat:report:update")
    public Result update(@RequestBody ChatUserReportDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        chatUserReportService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("chat:report:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        chatUserReportService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @Operation(summary = "Export")
    @LogOperation("Export")
    @RequiresPermissions("chat:report:export")
    public void export(
        @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response
    ) throws Exception{
        List<ChatUserReportDTO> list = chatUserReportService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "Report user in chat", list, ChatUserReportExcel.class);
    }

}
