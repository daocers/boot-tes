package co.bugu.tes.statistics.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author daocers
 * @Date 2019/5/8:15:01
 * @Description:
 */
@Data
public class UserStatDto {
    private String dateStr;
    private Date date;
    private Integer loginCount;
    private Integer joinCount;
//    统计类型， 日，周，月
    private Integer statType;
}
