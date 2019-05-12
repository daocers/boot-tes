package co.bugu.tes.role.enums;

/**
 * 角色类型， 1 系统预设，不允许修改code和授权
 * @Author daocers
 * @Date 2019/5/12:09:57
 * @Description:
 */
public enum RoleTypeEnum {
    SYSTEM(1, "系统预设"), CUSTOM(2, "自定义");
    private int type;
    private String name;

    RoleTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
