package co.bugu.tes.rolePermissionX.api;

import co.bugu.common.RespDto;
import co.bugu.tes.rolePermissionX.domain.RolePermissionX;
import co.bugu.tes.rolePermissionX.service.IRolePermissionXService;
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
@RequestMapping("/rolePermissionX/api")
public class RolePermissionXApi {
    private Logger logger = LoggerFactory.getLogger(RolePermissionXApi.class);

    @Autowired
    IRolePermissionXService rolePermissionXService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<RolePermissionX>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody RolePermissionX rolePermissionX) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(rolePermissionX, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<RolePermissionX> list = rolePermissionXService.findByCondition(pageNum, pageSize, rolePermissionX);
            PageInfo<RolePermissionX> pageInfo = new PageInfo<>(list);
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
     * @param rolePermissionX
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveRolePermissionX(@RequestBody RolePermissionX rolePermissionX) {
        try {
            Long rolePermissionXId = rolePermissionX.getId();
            if(null == rolePermissionXId){
                logger.debug("保存， saveRolePermissionX, 参数： {}", JSON.toJSONString(rolePermissionX, true));
                rolePermissionXId = rolePermissionXService.add(rolePermissionX);
                logger.info("新增 成功， id: {}", rolePermissionXId);
            }else{
                rolePermissionXService.updateById(rolePermissionX);
                logger.debug("更新成功", JSON.toJSONString(rolePermissionX, true));
            }
            return RespDto.success(rolePermissionXId != null);
        } catch (Exception e) {
            logger.error("保存 rolePermissionX 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.rolePermissionX.domain.RolePermissionX>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findById")
    public RespDto<RolePermissionX> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            RolePermissionX rolePermissionX = rolePermissionXService.findById(id);
            return RespDto.success(rolePermissionX);
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
            int count = rolePermissionXService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

