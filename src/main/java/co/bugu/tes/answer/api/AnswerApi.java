package co.bugu.tes.answer.api;

import co.bugu.common.RespDto;
import co.bugu.tes.answer.domain.Answer;
import co.bugu.tes.answer.service.IAnswerService;
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
@RequestMapping("/answer/api")
public class AnswerApi {
    private Logger logger = LoggerFactory.getLogger(AnswerApi.class);

    @Autowired
    IAnswerService answerService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<Answer>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Answer answer) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(answer, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<Answer> list = answerService.findByCondition(pageNum, pageSize, answer);
            PageInfo<Answer> pageInfo = new PageInfo<>(list);
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
     * @param answer
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveAnswer(@RequestBody Answer answer) {
        try {
            Long answerId = answer.getId();
            if(null == answerId){
                logger.debug("保存， saveAnswer, 参数： {}", JSON.toJSONString(answer, true));
                answerId = answerService.add(answer);
                logger.info("新增 成功， id: {}", answerId);
            }else{
                answerService.updateById(answer);
                logger.debug("更新成功", JSON.toJSONString(answer, true));
            }
            return RespDto.success(answerId != null);
        } catch (Exception e) {
            logger.error("保存 answer 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.answer.domain.Answer>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findById")
    public RespDto<Answer> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Answer answer = answerService.findById(id);
            return RespDto.success(answer);
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
            int count = answerService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

