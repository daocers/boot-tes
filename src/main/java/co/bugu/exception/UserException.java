package co.bugu.exception;

/**
 * @Author daocers
 * @Date 2018/12/3:10:48
 * @Description:
 */

public class UserException extends Exception {
    //    用户未登录
    public static final int NO_LOGIN = -1;
    //    登陆失败
    public static final int ERR_LOGIN = -2;
    //    无效session
    public static final int INVALID_TOKEN = -3;
    //    未知异常
    public static final int ERR_UNKNOW = -4;

    private int code;

    public int getCode() {
        return code;
    }

    public UserException() {
        super();
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, int code) {
        super(message);
        this.code = code;
    }

}
