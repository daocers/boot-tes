package co.bugu.common.enums;

/**
 * @Author daocers
 * @Date 2018/12/1:10:30
 * @Description:
 */
public enum BaseStatusEnum {
    ENABLE(1, "可用"), DISABLE(2, "禁用");
    private int code;
    private String name;

    BaseStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
