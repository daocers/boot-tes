package co.bugu.tes.questionStat.api;

import co.bugu.common.RespDto;
import co.bugu.tes.questionStat.domain.QuestionStat;
import co.bugu.tes.questionStat.service.IQuestionStatService;
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
 * @create 2019-05-09 10:49
 */
@RestController
@RequestMapping("/questionStat/api")
public class QuestionStatApi {
    private static Logger logger = LoggerFactory.getLogger(QuestionStatApi.class);

    @Autowired
    IQuestionStatService questionStatService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2019-05-09 10:49
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<QuestionStat>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody QuestionStat questionStat) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(questionStat, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<QuestionStat> list = questionStatService.findByCondition(pageNum, pageSize, questionStat);
            PageInfo<QuestionStat> pageInfo = new PageInfo<>(list);
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
     * @param questionStat
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2019-05-09 10:49
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveQuestionStat(@RequestBody QuestionStat questionStat) {
        try {
            logger.debug("保存， saveQuestionStat, 参数： {}", JSON.toJSONString(questionStat, true));
            Long questionStatId = questionStatService.add(questionStat);
            logger.info("新增 成功， id: {}", questionStatId);

            return RespDto.success(questionStatId != null);
        } catch (Exception e) {
            logger.error("保存 questionStat 失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 更新指定id的记录
     *
     * @param questionStat
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2019-05-09 10:49
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RespDto<Boolean> updateById(@RequestBody QuestionStat questionStat) {
        try {
            logger.debug("更新，updateById, 参数: {}", JSON.toJSONString(questionStat, true));
            Preconditions.checkArgument(questionStat != null, "questionStat不能为空");
            Preconditions.checkArgument(null != questionStat.getId(), "id不能为空");
            int res = questionStatService.updateById(questionStat);

            return RespDto.success(res == 1);

        } catch (Exception e) {
            logger.error("更新 questionStat 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.questionStat.domain.QuestionDistributeStat>
     * @author daocers
     * @date 2019-05-09 10:49
     */
    @RequestMapping(value = "/getDetail")
    public RespDto<QuestionStat> getDetail(Long id) {
        try {
            logger.info("获取详情，getDetail, id： {}", id);
            QuestionStat questionStat = questionStatService.findById(id);
            return RespDto.success(questionStat);
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
     * @date 2019-05-09 10:49
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = questionStatService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

