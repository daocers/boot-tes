package co.bugu.tes.role.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author daocers
 * @Date 2018/12/1:11:48
 * @Description:
 */
@Data
public class RoleDto {
    private Long id;

    private String name;

    private String code;

    private String memo;

    private Integer type;
    private Integer status;

    private Long createUserId;

    private Date createTime;

    private Long updateUserId;

    private Date updateTime;

    private String createUserName;

}
