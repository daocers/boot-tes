package co.bugu.exception;

/**
 * @Author daocers
 * @Date 2018/12/3:10:48
 * @Description:
 */

public class UserException extends Exception {
    //    无效session
    public static final int TOKEN_ERR = -1;

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
