package co.bugu.tes.statistics.dto;

import lombok.Data;

/**
 * 用于接收数据库查询数据
 * @Author daocers
 * @Date 2019/5/8:15:41
 * @Description:
 */
@Data
public class QuestionStat {
    private Integer count;
    private Integer busiType;
    private Integer difficulty;
    private String questionType;
    private Long bankId;
}
