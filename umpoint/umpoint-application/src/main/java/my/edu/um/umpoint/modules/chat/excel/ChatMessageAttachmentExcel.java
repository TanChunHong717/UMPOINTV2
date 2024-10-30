package my.edu.um.umpoint.modules.chat.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * Chat message attachment
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
@Data
public class ChatMessageAttachmentExcel {
    @ExcelProperty(value = "Attachment ID")
    private Long id;
    @ExcelProperty(value = "Message ID")
    private Long messageId;
    @ExcelProperty(value = "Attachment type")
    private String type;
    @ExcelProperty(value = "Image url")
    private String url;

}