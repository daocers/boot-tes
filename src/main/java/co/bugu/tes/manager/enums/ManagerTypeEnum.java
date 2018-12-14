package co.bugu.tes.manager.enums;

/**
 * @Author daocers
 * @Date 2018/12/14:09:18
 * @Description:
 */
public enum ManagerTypeEnum {
    DEPARTMENT(1, "部门"), BRANCH(2, "机构"), STATION(3, "岗位");
    private int code;
    private String name;

    ManagerTypeEnum(int code, String name) {
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
