package my.edu.um.umpoint.common.constant;

import lombok.Getter;

public interface ChatConstant{
    @Getter
    enum RoomStatus{
        CREATED(0),
        OPEN(1),
        CLOSED(2),
        RESOLVED(3),
        REPORTED(4);

        private final int value;

        RoomStatus(int value){
            this.value = value;
        }
    }

    @Getter
    enum UserType{
        SYSTEN(0),
        BOT(1),
        USER(2),
        ADMIN(3),
        BOT_REPLY_OPTIONS(4);

        private final int value;

        UserType(int value){
            this.value = value;
        }
    }

    @Getter
    enum FacilityType{
        SPACE(0),
        SERVICE(1),
        ACCOMMODATION(2);

        private final int value;

        FacilityType(int value){
            this.value = value;
        }

        public static FacilityType fromString(String type){
            return switch (type.toLowerCase()) {
                case "space" -> SPACE;
                case "service" -> SERVICE;
                case "accommodation" -> ACCOMMODATION;
                default -> throw new IllegalArgumentException("Unknown facility type");
            };
        }

        public static String toString(FacilityType type){
            return type.name().toLowerCase();
        }
    }

    @Getter
    enum AutoReply  {
        DISABLE(0),
        ENABLED(1);

        private final int value;

        AutoReply (int value) {
            this.value = value;
        }
    }

}
