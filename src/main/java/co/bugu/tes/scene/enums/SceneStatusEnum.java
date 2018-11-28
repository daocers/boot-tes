package co.bugu.tes.scene.enums;

/**
 * @Author daocers
 * @Date 2018/11/28:14:03
 * @Description:
 */
public enum SceneStatusEnum {
    READY(1, "就绪，可以参加考试"), ON(2, "考试中"), CLOSED(3, "已封场"), CANCELED(4, "取消/作废");
    private int code;
    private String name;

    SceneStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
