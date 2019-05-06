package co.bugu.tes.answer.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Answer {
    private Long id;

    private Long paperId;

    private Long questionId;

    private Integer questionType;

    private Long sceneId;

    private String rightAnswer;

    private String answer;

    private Integer timeUsed;

    private String timeLeft;

    private Integer isDel;

    private Long userId;

    private Long createUserId;

    private Date createTime;

    private Long updateUserId;

    private Date updateTime;


}