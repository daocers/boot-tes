package co.bugu.tes.scene.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author daocers
 * @Date 2019/5/11:12:02
 * @Description:
 */
@Data
public class SceneMonitorDto {
    private Integer paperCount;
    private Integer paperCommitCount;
    private Integer paperNoneCommitCount;

    private Long id;

    private String code;

    private String name;

    private Date openTime;

    private Date closeTime;

    //    交卷百分点
    private Integer commitRate;

    private Integer status;

    private String createUserName;
    private Long createUserId;

}
