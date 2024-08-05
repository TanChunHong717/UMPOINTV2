package my.edu.um.umpoint.modules.log.enums;

public enum LoginStatusEnum {

    FAIL(0),
    SUCCESS(1),
    LOCK(2);

    private int value;

    LoginStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
