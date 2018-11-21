package co.bugu.tes.single;

/**
 * 公开标志  1 公开， 2 私有
 *
 * @Author daocers
 * @Date 2018/11/21:14:35
 * @Description:
 */
public enum PublicFlagEnum {
    PUBLIC(1, "公开"), PRIVATE(2, "私有");
    private int code;
    private String name;

    PublicFlagEnum(int code, String name) {
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
