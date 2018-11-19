package co.bugu.tes.userRoleX.api;

import co.bugu.common.RespDto;
import co.bugu.tes.userRoleX.domain.UserRoleX;
import co.bugu.tes.userRoleX.service.IUserRoleXService;
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
@RequestMapping("/userRoleX/api")
public class UserRoleXApi {
    private Logger logger = LoggerFactory.getLogger(UserRoleXApi.class);

    @Autowired
    IUserRoleXService userRoleXService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-19 19:29
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<UserRoleX>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody UserRoleX userRoleX) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(userRoleX, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<UserRoleX> list = userRoleXService.findByCondition(pageNum, pageSize, userRoleX);
            PageInfo<UserRoleX> pageInfo = new PageInfo<>(list);
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
     * @param userRoleX
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-19 19:29
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveUserRoleX(@RequestBody UserRoleX userRoleX) {
        try {
            Long userRoleXId = userRoleX.getId();
            if(null == userRoleXId){
                logger.debug("保存， saveUserRoleX, 参数： {}", JSON.toJSONString(userRoleX, true));
                userRoleXId = userRoleXService.add(userRoleX);
                logger.info("新增 成功， id: {}", userRoleXId);
            }else{
                userRoleXService.updateById(userRoleX);
                logger.debug("更新成功", JSON.toJSONString(userRoleX, true));
            }
            return RespDto.success(userRoleXId != null);
        } catch (Exception e) {
            logger.error("保存 userRoleX 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.userRoleX.domain.UserRoleX>
     * @author daocers
     * @date 2018-11-19 19:29
     */
    @RequestMapping(value = "/findById")
    public RespDto<UserRoleX> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            UserRoleX userRoleX = userRoleXService.findById(id);
            return RespDto.success(userRoleX);
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
            int count = userRoleXService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

