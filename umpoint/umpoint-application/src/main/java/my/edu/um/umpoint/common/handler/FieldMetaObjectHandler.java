package my.edu.um.umpoint.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class FieldMetaObjectHandler implements MetaObjectHandler {
    private final static String CREATE_DATE = "createDate";
    private final static String CREATOR = "creator";
    private final static String UPDATE_DATE = "updateDate";
    private final static String UPDATER = "updater";
    private final static String DEPT_ID = "deptId";

    @Override
    public void insertFill(MetaObject metaObject) {
        UserDetail user = SecurityUser.getUser();
        Date date = new Date();

        strictInsertFill(metaObject, CREATOR, Long.class, user.getId());
        strictInsertFill(metaObject, CREATE_DATE, Date.class, date);
        strictInsertFill(metaObject, DEPT_ID, Long.class, user.getDeptId());
        strictInsertFill(metaObject, UPDATER, Long.class, user.getId());
        strictInsertFill(metaObject, UPDATE_DATE, Date.class, date);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, UPDATER, Long.class, SecurityUser.getUserId());
        strictUpdateFill(metaObject, UPDATE_DATE, Date.class, new Date());
    }
}