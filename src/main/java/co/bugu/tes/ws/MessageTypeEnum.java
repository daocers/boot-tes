package co.bugu.tes.ws;

/**
 * Created by daocers on 2017/3/25.
 */
public enum MessageTypeEnum {
    COMMIT_QUESTION(1, "提交试题"), COMMIT_PAPER(2, "交卷"), FORCE_COMMIT_PAPER(3, "强制交卷"), GET_QUESTION(4, "获取试题"), CLIENT_CLOSE(5, "客户端释放");
    private int code;
    private String message;


    MessageTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
