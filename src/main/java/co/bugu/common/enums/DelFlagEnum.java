package co.bugu.common.enums;

/**
 *      删除标识枚举类
 * @author daocers
 * @date 2017/11/14 11:40
 * @param
 * @return
 */
public enum DelFlagEnum {
    YES(2, "已删除"),
    NO(1, "未删除")

    ;
    private Integer code;
    private String value;

    DelFlagEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
