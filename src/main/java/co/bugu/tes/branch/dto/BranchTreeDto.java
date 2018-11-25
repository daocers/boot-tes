package co.bugu.tes.branch.dto;

import java.util.List;

/**
 * 树状组件dto
 *
 * @author daocers
 * @create 2017/12/5 22:23
 */
public class BranchTreeDto {
    private Long id;
    private String name;
    private Long superiorId;
    private List<BranchTreeDto> children;
    private String code;

    private String address;

    private Integer level;


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

    public Long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }

    public List<BranchTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<BranchTreeDto> children) {
        this.children = children;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
