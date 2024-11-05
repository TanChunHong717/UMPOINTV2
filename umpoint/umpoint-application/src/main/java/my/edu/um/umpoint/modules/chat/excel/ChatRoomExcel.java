package my.edu.um.umpoint.modules.chat.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * Chat room
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
@Data
public class ChatRoomExcel {
    @ExcelProperty(value = "Room ID")
    private Long id;
    @ExcelProperty(value = "Room name")
    private String name;
    @ExcelProperty(value = "Status: 0:Created(No admin), 1:Open(Admin assigned), 2:Closed, 3:Resolved, 4:Reported")
    private Integer status;
    @ExcelProperty(value = "Type: 0:Space, 1:Service, 2:Accommodation")
    private Integer facilityType;
    @ExcelProperty(value = "Facility ID")
    private Long facilityId;
    @ExcelProperty(value = "User ID that created room")
    private Long initiateUserId;
    @ExcelProperty(value = "Admin user ID")
    private Long assignedAdminId;
    @ExcelProperty(value = "Creation Date")
    private Date createdAt;

}