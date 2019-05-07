package co.bugu.tes.paperPolicyCondition.domain;

import java.util.Date;

public class PaperPolicyCondition {
    private Long id;

    private Long bankId;

    private Long paperPolicyId;

    private Integer status;

    private String paperPolicyName;

    private String singleInfo;

    private String multiInfo;

    private String judgeInfo;

    private Integer isDel;

    private Date updateTime;

    private Long updateUserId;

    private Long createUserId;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public Long getPaperPolicyId() {
        return paperPolicyId;
    }

    public void setPaperPolicyId(Long paperPolicyId) {
        this.paperPolicyId = paperPolicyId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPaperPolicyName() {
        return paperPolicyName;
    }

    public void setPaperPolicyName(String paperPolicyName) {
        this.paperPolicyName = paperPolicyName;
    }

    public String getSingleInfo() {
        return singleInfo;
    }

    public void setSingleInfo(String singleInfo) {
        this.singleInfo = singleInfo;
    }

    public String getMultiInfo() {
        return multiInfo;
    }

    public void setMultiInfo(String multiInfo) {
        this.multiInfo = multiInfo;
    }

    public String getJudgeInfo() {
        return judgeInfo;
    }

    public void setJudgeInfo(String judgeInfo) {
        this.judgeInfo = judgeInfo;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
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
}