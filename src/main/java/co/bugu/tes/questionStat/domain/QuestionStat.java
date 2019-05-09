package co.bugu.tes.questionStat.domain;

import lombok.Data;

import java.util.Date;

@Data
public class QuestionStat {
    private Long id;

    private Long bankId;

    private Integer questionType;

    private Long questionId;

    private String rightAnswer;

    private String answer;

    private String answerRecord;

    private Integer total;

    private Integer rightCount;

    private Integer wrongCount;

    private Double wrongRate;

    private Long lastAnswerId;

    private Integer isDel;

    private Date updateTime;

    private Long updateUserId;

    private Long createUserId;

    private Date createTime;

}