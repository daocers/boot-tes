package co.bugu.tes.exam.dto;

/**
 * 通用试题dto
 * @Author daocers
 * @Date 2018/11/25:20:10
 * @Description:
 */
public class QuestionDto {
    private Long id;
    private Integer type;
    private String title;
    private String content;
    private String answer;
    private String realAnswer;
    private String leftTimeInfo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
