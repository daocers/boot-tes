package co.bugu.tes.permission;

/**
 * 权限类型
 * @Author daocers
 * @Date 2018/11/30:10:47
 * @Description:
 */
public enum PermissionTypeEnum {
    MENU(1, "菜单权限"), ACTION(2, "操作权限");
    private int code;
    private String name;

    PermissionTypeEnum(int code, String name) {
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
