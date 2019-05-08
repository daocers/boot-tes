package co.bugu.tes.statistics.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author daocers
 * @Date 2019/5/8:15:08
 * @Description:
 */
@Data
public class QuestionBankStatDto {
    private Long bankId;
    private String name;
    private Integer questionCount;
    //    最后更新时间
    private Date lastUpdateTime;
}
