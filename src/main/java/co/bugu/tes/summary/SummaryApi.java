package co.bugu.tes.summary;

import co.bugu.common.RespDto;
import co.bugu.exception.UserException;
import co.bugu.tes.loginLog.domain.LoginLog;
import co.bugu.tes.loginLog.service.ILoginLogService;
import co.bugu.util.UserUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author daocers
 * @Date 2018/12/4:21:53
 * @Description:
 */
@RestController
@RequestMapping("/summary/api")
public class SummaryApi {
    private Logger logger = LoggerFactory.getLogger(SummaryApi.class);

    @Autowired
    ILoginLogService loginLogService;

    /**
     * 获取在线人数
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/4 21:58
     */
    @RequestMapping(value = "/getOnlineCount")
    public RespDto<Long> getOnlineCount() {
        long count = UserUtil.getTokenCount();
        return RespDto.success(count);
    }

    /**
     * 获取上次登录信息
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/4 22:02
     */
    @RequestMapping(value = "/getLastLoginInfo")
    public RespDto<LoginLog> getLastLoginInfo() throws UserException {
        Long userId = UserUtil.getCurrentUser().getId();
        LoginLog query = new LoginLog();
        query.setUserId(userId);
        List<LoginLog> list = loginLogService.findByCondition(1, 1, query);
        if (CollectionUtils.isNotEmpty(list)) {
            query = list.get(0);
        } else {
            query = new LoginLog();
        }
        return RespDto.success(query);
    }
}
