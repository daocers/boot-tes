package co.bugu.tes.paper.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author daocers
 * @Date 2018/11/28:11:21
 * @Description:
 */
@Data
public class PaperDto {
    private Long id;
    private String userName;

    private String name;
    private String sceneName;
    private String sceneCode;

    private String code;

    private Integer answerFlag;

    private Date beginTime;

    private Date endTime;

    private Double originalScore;

    private Double score;

    private Long sceneId;

    private Long userId;

    private Integer status;

}
