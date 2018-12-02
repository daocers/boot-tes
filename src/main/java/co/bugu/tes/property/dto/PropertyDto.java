package co.bugu.tes.property.dto;

import co.bugu.tes.propertyItem.domain.PropertyItem;

import java.util.Date;
import java.util.List;

/**
 * @Author daocers
 * @Date 2018/12/2:12:48
 * @Description:
 */
public class PropertyDto {

    private Long id;

    private String name;

    private String memo;

    private Integer no;

    private Integer questionType;

    private Integer required;

    private Integer status;

    private Long createUserId;

    private Date createTime;

    private String createUserName;

    private List<PropertyItem> itemList;


    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
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

    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public Integer getRequired() {
        return required;
    }

    public void setRequired(Integer required) {
        this.required = required;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<PropertyItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<PropertyItem> itemList) {
        this.itemList = itemList;
    }
}
