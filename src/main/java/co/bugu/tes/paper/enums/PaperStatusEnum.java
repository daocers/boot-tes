package co.bugu.tes.paper.enums;

/**
 * @Author daocers
 * @Date 2018/11/25:20:17
 * @Description:
 */
public enum PaperStatusEnum {
    OK(1, "正常状态"), COMMITED(2, "已提交"), CANCELED(3, "已取消"), MARKED(4, "已判分"), ;
    private int code;
    private String name;

    PaperStatusEnum(int code, String name) {
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
