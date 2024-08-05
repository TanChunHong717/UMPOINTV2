package my.edu.um.umpoint.modules.log.enums;

public enum LoginOperationEnum {
    LOGIN(0),
    LOGOUT(1);

    private int value;

    LoginOperationEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}