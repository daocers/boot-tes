package co.bugu.tes.statistics.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author daocers
 * @Date 2019/5/8:15:04
 * @Description:
 */
@Data
public class SceneQuestionStatDto {
    private Long sceneId;
    private String sceneCode;
    private Date openTime;
    private Integer total;
    private Integer wrong;
    private Integer rightRate;

}
