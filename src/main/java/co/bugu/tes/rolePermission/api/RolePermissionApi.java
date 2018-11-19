package co.bugu.tes.rolePermission.api;

import co.bugu.common.RespDto;
import co.bugu.tes.rolePermission.domain.RolePermission;
import co.bugu.tes.rolePermission.service.IRolePermissionService;
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
 * @create 2018-11-19 19:29
 */
@RestController
@RequestMapping("/rolePermission/api")
public class RolePermissionApi {
    private Logger logger = LoggerFactory.getLogger(RolePermissionApi.class);

    @Autowired
    IRolePermissionService rolePermissionService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-19 19:29
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<RolePermission>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody RolePermission rolePermission) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(rolePermission, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<RolePermission> list = rolePermissionService.findByCondition(pageNum, pageSize, rolePermission);
            PageInfo<RolePermission> pageInfo = new PageInfo<>(list);
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
     * @param rolePermission
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-19 19:29
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveRolePermission(@RequestBody RolePermission rolePermission) {
        try {
            Long rolePermissionId = rolePermission.getId();
            if(null == rolePermissionId){
                logger.debug("保存， saveRolePermission, 参数： {}", JSON.toJSONString(rolePermission, true));
                rolePermissionId = rolePermissionService.add(rolePermission);
                logger.info("新增 成功， id: {}", rolePermissionId);
            }else{
                rolePermissionService.updateById(rolePermission);
                logger.debug("更新成功", JSON.toJSONString(rolePermission, true));
            }
            return RespDto.success(rolePermissionId != null);
        } catch (Exception e) {
            logger.error("保存 rolePermission 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.rolePermission.domain.RolePermission>
     * @author daocers
     * @date 2018-11-19 19:29
     */
    @RequestMapping(value = "/findById")
    public RespDto<RolePermission> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            RolePermission rolePermission = rolePermissionService.findById(id);
            return RespDto.success(rolePermission);
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
     * @date 2018-11-19 19:29
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = rolePermissionService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

