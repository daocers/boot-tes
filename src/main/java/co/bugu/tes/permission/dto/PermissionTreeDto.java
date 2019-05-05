package co.bugu.tes.permission.dto;

import lombok.Data;

import java.util.List;

/**
 * 权限树状组件信息
 *
 * @author daocers
 * @createTime 2017/12/5
 */
@Data
public class PermissionTreeDto {
    private Long id;
    private String name;
    private String code;
    private String memo;
    private Integer no;
    private Integer type;
    private String url;
    private Long superiorId;
    private Integer status;
    private List<PermissionTreeDto> children;

}
