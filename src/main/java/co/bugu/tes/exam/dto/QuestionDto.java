package co.bugu.tes.exam.dto;

import lombok.Data;

/**
 * 通用试题dto
 *
 * @Author daocers
 * @Date 2018/11/25:20:10
 * @Description:
 */
@Data
public class QuestionDto {
    private Long answerId;
    private Integer questionType;
    private String title;
    private String content;
    private String answer;
    //    实际答案
    private String realAnswer;
    private String leftTimeInfo;
    private Integer timeUsed;


    //    正确答案
    private String rightAnswer;

    // 做题人id
    private Long userId;
}
