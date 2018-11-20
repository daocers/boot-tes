package co.bugu.tes.permission.api;

import co.bugu.common.RespDto;
import co.bugu.tes.permission.domain.Permission;
import co.bugu.tes.permission.service.IPermissionService;
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
 * @create 2018-11-20 17:15
 */
@RestController
@RequestMapping("/permission/api")
public class PermissionApi {
    private Logger logger = LoggerFactory.getLogger(PermissionApi.class);

    @Autowired
    IPermissionService permissionService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<Permission>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Permission permission) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(permission, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<Permission> list = permissionService.findByCondition(pageNum, pageSize, permission);
            PageInfo<Permission> pageInfo = new PageInfo<>(list);
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
     * @param permission
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> savePermission(@RequestBody Permission permission) {
        try {
            Long permissionId = permission.getId();
            if(null == permissionId){
                logger.debug("保存， savePermission, 参数： {}", JSON.toJSONString(permission, true));
                permissionId = permissionService.add(permission);
                logger.info("新增 成功， id: {}", permissionId);
            }else{
                permissionService.updateById(permission);
                logger.debug("更新成功", JSON.toJSONString(permission, true));
            }
            return RespDto.success(permissionId != null);
        } catch (Exception e) {
            logger.error("保存 permission 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.permission.domain.Permission>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findById")
    public RespDto<Permission> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Permission permission = permissionService.findById(id);
            return RespDto.success(permission);
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
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = permissionService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

