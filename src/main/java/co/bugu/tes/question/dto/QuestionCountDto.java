package co.bugu.tes.question.dto;

import lombok.Data;

/**
 * @Author daocers
 * @Date 2019/5/12:11:30
 * @Description:
 */
@Data
public class QuestionCountDto {
    private Integer singleCount;
    private Integer judgeCount;
    private Integer multiCount;
    private Integer  total;
}
