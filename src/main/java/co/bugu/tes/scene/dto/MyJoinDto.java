package co.bugu.tes.scene.dto;

import co.bugu.tes.scene.domain.Scene;

import java.util.Date;

/**
 * @Author daocers
 * @Date 2018/12/2:22:33
 * @Description:
 */
public class MyJoinDto {
    private Long id;
    private Long userId;
//    private String name;
//    private String code;
    private Date beginTime;
    private Date endTime;
    private Double score;
    private Double originalScore;
//    private Integer sceneStatus;
    private Integer paperStatus;

    private Scene scene;

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

//    public Integer getSceneStatus() {
//        return sceneStatus;
//    }
//
//    public void setSceneStatus(Integer sceneStatus) {
//        this.sceneStatus = sceneStatus;
//    }

    public Integer getPaperStatus() {
        return paperStatus;
    }

    public void setPaperStatus(Integer paperStatus) {
        this.paperStatus = paperStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getOriginalScore() {
        return originalScore;
    }

    public void setOriginalScore(Double originalScore) {
        this.originalScore = originalScore;
    }
}
