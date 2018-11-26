package co.bugu.tes.paper.enums;

/**
 * @Author daocers
 * @Date 2018/11/26:10:20
 * @Description:
 */
public enum AnswerFlagEnum {
    NO(1, "未作答"), YES(2, "已做答");
    private int code;
    private String name;

    AnswerFlagEnum(int code, String name) {
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
