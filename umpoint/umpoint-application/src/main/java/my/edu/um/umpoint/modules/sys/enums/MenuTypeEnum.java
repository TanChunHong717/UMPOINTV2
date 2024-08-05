package my.edu.um.umpoint.modules.sys.enums;

public enum MenuTypeEnum {
    MENU(0),
    BUTTON(1);

    private int value;

    MenuTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
