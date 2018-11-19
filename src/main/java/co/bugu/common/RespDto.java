package co.bugu.common;

import java.util.Collection;

/**
 * 接口返回信息包装类
 *
 * @author daocers
 * @create 2017/10/22 0:04
 */
public class RespDto<T> {
    private static final String SUCCESS_MESSAGE = "成功";
    private static final Integer SUCCESS_CODE = 0;

    private Boolean result;
    private Integer code;
    private String message;


    private T data;

    private RespDto() {

    }

    private RespDto(Boolean result, Integer code, String message, T data) {
        this.result = result;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Boolean getResult() {
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }


    public static <T> RespDto<T> success(T data) {
        return new RespDto<>(true, SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    public static RespDto success() {
        return new RespDto(true, SUCCESS_CODE, SUCCESS_MESSAGE, null);
    }

    public static RespDto fail(Integer code, String message) {
        return new RespDto(false, code, message, null);
    }

    public static RespDto fail() {
        return new RespDto(false, null, null, null);
    }

    public static RespDto fail(String message){
        return new RespDto(false, null, message, null);
    }

}
