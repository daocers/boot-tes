package co.bugu.tes.role.api;

import co.bugu.common.RespDto;
import co.bugu.tes.role.domain.Role;
import co.bugu.tes.role.service.IRoleService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据api
 *
 * @author daocers
 * @create 2018-11-19 17:51
 */
@RestController
@RequestMapping("/role/api")
public class RoleApi {
    private Logger logger = LoggerFactory.getLogger(RoleApi.class);

    @Autowired
    IRoleService roleService;


    /**
     * 登录，成功后返回用户token
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/19 17:54
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RespDto<String> login(String username, String password){
        return RespDto.success("");
    }

    /**
     * 退出登录，退出成功前端跳回首页
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/19 17:55
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public RespDto<Boolean> logout(Long id, String token){
        return RespDto.success(true);
    }

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-19 17:51
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<Role>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Role role) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(role, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<Role> list = roleService.findByCondition(pageNum, pageSize, role);
            PageInfo<Role> pageInfo = new PageInfo<>(list);
            logger.info("查询到数据： {}", JSON.toJSONString(pageInfo, true));
            return RespDto.success(pageInfo);
        } catch (Exception e) {
            logger.error("findByCondition  失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 保存
     *
     * @param role
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-19 17:51
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveRole(@RequestBody Role role) {
        try {
            Long roleId = role.getId();
            if(null == roleId){
                logger.debug("保存， saveRole, 参数： {}", JSON.toJSONString(role, true));
                roleId = roleService.add(role);
                logger.info("新增 成功， id: {}", roleId);
            }else{
                roleService.updateById(role);
                logger.debug("更新成功", JSON.toJSONString(role, true));
            }
            return RespDto.success(roleId != null);
        } catch (Exception e) {
            logger.error("保存 role 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.role.domain.Role>
     * @author daocers
     * @date 2018-11-19 17:51
     */
    @RequestMapping(value = "/findById")
    public RespDto<Role> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Role role = roleService.findById(id);
            return RespDto.success(role);
        } catch (Exception e) {
            logger.error("获取详情失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 删除，软删除，更新数据库删除标志
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-19 17:51
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = roleService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

