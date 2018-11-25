package co.bugu.tes.ws;

/**
 * Created by daocers on 2017/3/25.
 */
public enum MessageEnum {
    GET_QUESTION(0), COMMIT_QUESTION(1), COMMIT_PAPER(2), FORSE_COMMIT_PAPER(3);
    private Integer type;

    MessageEnum(Integer type) {
        this.type = type;
    }

    public Integer getType(){
        return this.type;
    }
}
