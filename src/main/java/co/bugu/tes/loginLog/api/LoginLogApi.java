package co.bugu.tes.loginLog.api;

import co.bugu.common.RespDto;
import co.bugu.tes.loginLog.domain.LoginLog;
import co.bugu.tes.loginLog.service.ILoginLogService;
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
 * @create 2018-12-04 10:20
 */
@RestController
@RequestMapping("/loginLog/api")
public class LoginLogApi {
    private Logger logger = LoggerFactory.getLogger(LoginLogApi.class);

    @Autowired
    ILoginLogService loginLogService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-12-04 10:20
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<LoginLog>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody LoginLog loginLog) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(loginLog, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<LoginLog> list = loginLogService.findByCondition(pageNum, pageSize, loginLog);
            PageInfo<LoginLog> pageInfo = new PageInfo<>(list);
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
     * @param loginLog
     * @return co.bugu.boot.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-12-04 10:20
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveLoginLog(@RequestBody LoginLog loginLog) {
        try {
            Long loginLogId = loginLog.getId();
            if (null == loginLogId) {
                logger.debug("保存， saveLoginLog, 参数： {}", JSON.toJSONString(loginLog, true));
                loginLogId = loginLogService.add(loginLog);
                logger.info("新增 成功， id: {}", loginLogId);
            } else {
                loginLogService.updateById(loginLog);
                logger.debug("更新成功", JSON.toJSONString(loginLog, true));
            }
            return RespDto.success(loginLogId != null);
        } catch (Exception e) {
            logger.error("保存 loginLog 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.boot.common.RespDto<co.bugu.boot.tes.loginLog.domain.LoginLog>
     * @author daocers
     * @date 2018-12-04 10:20
     */
    @RequestMapping(value = "/findById")
    public RespDto<LoginLog> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            LoginLog loginLog = loginLogService.findById(id);
            return RespDto.success(loginLog);
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
     * @date 2018-12-04 10:20
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = loginLogService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

