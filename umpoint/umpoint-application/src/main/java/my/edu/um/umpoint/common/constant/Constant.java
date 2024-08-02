package my.edu.um.umpoint.common.constant;

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

    String CLOUD_STORAGE_CONFIG_KEY = "CLOUD_STORAGE_CONFIG_KEY";

    enum ScheduleStatus {
        /**
         * 暂停
         */
        PAUSE(0),
        /**
         * 正常
         */
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
}