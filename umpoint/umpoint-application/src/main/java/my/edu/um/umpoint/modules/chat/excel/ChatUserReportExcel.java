package my.edu.um.umpoint.modules.chat.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * Report user in chat
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-29
 */
@Data
public class ChatUserReportExcel {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "User ID that is reported")
    private Long reportedUserId;
    @ExcelProperty(value = "Reported chat room ID")
    private Long chatRoomId;
    @ExcelProperty(value = "Reported message ID")
    private Long messageId;
    @ExcelProperty(value = "Reason")
    private String reason;
    @ExcelProperty(value = "User ID that send this report")
    private Long reportedBy;
    @ExcelProperty(value = "Creation Date")
    private Date createdAt;

}