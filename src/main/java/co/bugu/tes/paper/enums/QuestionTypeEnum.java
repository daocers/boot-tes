package co.bugu.tes.paper.enums;

/**
 * @Author daocers
 * @Date 2018/11/25:20:45
 * @Description:
 */
public enum QuestionTypeEnum {
    SINGLE(1, "单选"), MULTI(2, "多选"), JUDGE(3, "判断");
    private int code;
    private String name;

    QuestionTypeEnum(int code, String name) {
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
