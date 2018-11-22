package co.bugu.tes.permission.dto;

import java.util.List;

/**
 * 权限树状组件信息
 *
 * @author daocers
 * @createTime 2017/12/5
 */
public class PermissionTreeDto {
    private Long id;
    private String name;
    private String code;
    private String memo;
    private Integer no;
    private Integer type;
    private String url;
    private Long superiorId;
    private List<PermissionTreeDto> children;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<PermissionTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<PermissionTreeDto> children) {
        this.children = children;
    }
}
