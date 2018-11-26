package co.bugu.tes.exam.dto;

/**
 * 通用试题dto
 *
 * @Author daocers
 * @Date 2018/11/25:20:10
 * @Description:
 */
public class QuestionDto {
    private Long answerId;
    private Integer questionType;
    private String title;
    private String content;
    private String answer;
    private String realAnswer;
    private String leftTimeInfo;

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }


    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getRealAnswer() {
        return realAnswer;
    }

    public void setRealAnswer(String realAnswer) {
        this.realAnswer = realAnswer;
    }

    public String getLeftTimeInfo() {
        return leftTimeInfo;
    }

    public void setLeftTimeInfo(String leftTimeInfo) {
        this.leftTimeInfo = leftTimeInfo;
    }
}
