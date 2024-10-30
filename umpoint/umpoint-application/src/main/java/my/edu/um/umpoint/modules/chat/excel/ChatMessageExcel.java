package my.edu.um.umpoint.modules.chat.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * Chat message
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
@Data
public class ChatMessageExcel {
    @ExcelProperty(value = "Message ID")
    private Long id;
    @ExcelProperty(value = "Chat room ID")
    private Long chatRoomId;
    @ExcelProperty(value = "Sender user type: 0:System, 1:Bot, 2:User, 3:Admin")
    private Integer senderType;
    @ExcelProperty(value = "Sender user ID")
    private Long userId;
    @ExcelProperty(value = "Sender admin ID")
    private Long adminId;
    @ExcelProperty(value = "Message body")
    private String message;
    @ExcelProperty(value = "Message ID that is responding to")
    private Long replyMessageId;
    @ExcelProperty(value = "Creation Date")
    private Date createdAt;

}