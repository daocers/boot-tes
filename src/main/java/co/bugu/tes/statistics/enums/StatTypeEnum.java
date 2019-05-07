package co.bugu.tes.statistics.enums;

import lombok.Getter;

/**
 * @Author daocers
 * @Date 2019/5/7:14:35
 * @Description:
 */
@Getter
public enum StatTypeEnum {
    DAILY(1, "天"), WEEKLY(2, "周"), MONTHLY(3, "月");
    private int code;
    private String name;

    StatTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
