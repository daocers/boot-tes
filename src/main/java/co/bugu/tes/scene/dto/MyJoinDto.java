package co.bugu.tes.scene.dto;

import co.bugu.tes.scene.domain.Scene;
import lombok.Data;

import java.util.Date;

/**
 * @Author daocers
 * @Date 2018/12/2:22:33
 * @Description:
 */
@Data
public class MyJoinDto {
    private Long id;
    private Long userId;
    //    private String name;
//    private String code;
    private Date beginTime;
    private Date endTime;
    private Double commonScore;
    private Double receiptScore;
    private Double score;
    private Double originalScore;
    //    private Integer sceneStatus;
    private Integer paperStatus;

    private Scene scene;

}
