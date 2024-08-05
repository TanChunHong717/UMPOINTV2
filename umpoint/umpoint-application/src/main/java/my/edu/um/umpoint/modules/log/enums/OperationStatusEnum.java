package my.edu.um.umpoint.modules.log.enums;

public enum OperationStatusEnum {
    FAIL(0),
    SUCCESS(1);

    private int value;

    OperationStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}