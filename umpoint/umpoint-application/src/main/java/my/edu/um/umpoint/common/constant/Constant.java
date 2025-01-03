package my.edu.um.umpoint.common.constant;

import lombok.Getter;

public interface Constant {
    int SUCCESS = 1;
    int FAIL = 0;
    Long MENU_ROOT = 0L;
    Long DEPT_ROOT = 0L;
    String ASC = "asc";
    String DESC = "desc";
    String CREATE_DATE = "create_date";

    String SQL_FILTER = "sqlFilter";
    String PAGE = "page";
    String LIMIT = "limit";
    String ORDER_FIELD = "orderField";
    String ORDER = "order";
    String TOKEN_HEADER = "token";

    String START_TIME = "startTime";
    String END_TIME = "endTime";

    String ID = "id";
    String SPACE = "space";
    String SPACE_ID = "spaceId";
    String SPACE_ID_LIST = "spaceIdList";
    String SERVICE = "service";
    String SERVICE_ID = "serviceId";
    String ACCOMMODATION = "accommodation";
    String ACCOMMODATION_ID = "accommodationId";
    String ACCOMMODATION_ID_LIST = "accommodationIdList";
    String DEPT_ID = "deptId";
    String CAT_ID = "catId";
    String TAG_ID = "tagId";

    String NAME = "name";
    String STATUS = "status";
    String EVENT = "event";
    String APPROVAL_REQUIRED = "approvalRequire";

    String CLOUD_STORAGE_CONFIG_KEY = "CLOUD_STORAGE_CONFIG_KEY";


    enum ScheduleStatus {
        PAUSE(0),
        NORMAL(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    enum CloudService {

        QINIU(1),

        ALIYUN(2),

        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    @Getter
    enum EmailStatus{
        NOT_VALIDATED(0),
        VALIDATED(1);

        private final int value;

        EmailStatus(int value){
            this.value = value;
        }
    }

    @Getter
    enum EmailNotification{
        DO_NOT_RECEIVE(0),
        RECEIVE(1);

        private final int value;

        EmailNotification(int value){
            this.value = value;
        }
    }
}