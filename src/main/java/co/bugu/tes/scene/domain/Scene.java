package co.bugu.tes.scene.domain;

import java.util.Date;

public class Scene {
    private Long id;

    private String code;

    private String name;

    private Integer ownerType;

    private Long ownerId;

    private String authCode;

    private Integer changePaper;

    private Integer delayTime;

    private Integer duration;

    private Long paperPolicyId;

    private String cancelReason;

    private Integer paperGenerateType;

    private String remark;

    private Long questionBankId;

    private Integer userChoiceType;

    private Double totalScore;

    private Integer percentable;

    private String metaScoreInfo;

    private Integer status;

    private Date openTime;

    private Date closeTime;

    private Integer isDel;

    private Date createTime;

    private Long createUserId;

    private Date updateTime;

    private Long updateUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Integer ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Integer getChangePaper() {
        return changePaper;
    }

    public void setChangePaper(Integer changePaper) {
        this.changePaper = changePaper;
    }

    public Integer getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getPaperPolicyId() {
        return paperPolicyId;
    }

    public void setPaperPolicyId(Long paperPolicyId) {
        this.paperPolicyId = paperPolicyId;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Integer getPaperGenerateType() {
        return paperGenerateType;
    }

    public void setPaperGenerateType(Integer paperGenerateType) {
        this.paperGenerateType = paperGenerateType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getQuestionBankId() {
        return questionBankId;
    }

    public void setQuestionBankId(Long questionBankId) {
        this.questionBankId = questionBankId;
    }

    public Integer getUserChoiceType() {
        return userChoiceType;
    }

    public void setUserChoiceType(Integer userChoiceType) {
        this.userChoiceType = userChoiceType;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getPercentable() {
        return percentable;
    }

    public void setPercentable(Integer percentable) {
        this.percentable = percentable;
    }

    public String getMetaScoreInfo() {
        return metaScoreInfo;
    }

    public void setMetaScoreInfo(String metaScoreInfo) {
        this.metaScoreInfo = metaScoreInfo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
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
}