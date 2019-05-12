package co.bugu.tes.role.agent;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.role.domain.Role;
import co.bugu.tes.role.service.IRoleService;
import co.bugu.tes.userRoleX.domain.UserRoleX;
import co.bugu.tes.userRoleX.service.IUserRoleXService;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author daocers
 * @Date 2018/11/30:15:36
 * @Description:
 */
@Service
public class RoleAgent {
    private static Logger logger = LoggerFactory.getLogger(RoleAgent.class);
    @Autowired
    IRoleService roleService;
    @Autowired
    IUserRoleXService userRoleXService;


    /**
     * 获取用户的角色列表
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/12 13:50
     */
    public List<Role> getRoleList(Long userId) {
        UserRoleX x = new UserRoleX();
        x.setIsDel(DelFlagEnum.NO.getCode());
        x.setUserId(userId);
        List<UserRoleX> xList = userRoleXService.findByCondition(x);
        if (CollectionUtils.isEmpty(xList)) {
            logger.info("用户： {} 没有角色", userId);
            return new ArrayList<>();
        }
        List<Role> roles = Lists.transform(xList, item -> roleService.findById(item.getRoleId()));

        return roles;

    }


}
