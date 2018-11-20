package co.bugu.tes.questionBank.api;

import co.bugu.common.RespDto;
import co.bugu.tes.questionBank.domain.QuestionBank;
import co.bugu.tes.questionBank.service.IQuestionBankService;
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
@RequestMapping("/questionBank/api")
public class QuestionBankApi {
    private Logger logger = LoggerFactory.getLogger(QuestionBankApi.class);

    @Autowired
    IQuestionBankService questionBankService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<QuestionBank>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody QuestionBank questionBank) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(questionBank, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<QuestionBank> list = questionBankService.findByCondition(pageNum, pageSize, questionBank);
            PageInfo<QuestionBank> pageInfo = new PageInfo<>(list);
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
     * @param questionBank
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveQuestionBank(@RequestBody QuestionBank questionBank) {
        try {
            Long questionBankId = questionBank.getId();
            if(null == questionBankId){
                logger.debug("保存， saveQuestionBank, 参数： {}", JSON.toJSONString(questionBank, true));
                questionBankId = questionBankService.add(questionBank);
                logger.info("新增 成功， id: {}", questionBankId);
            }else{
                questionBankService.updateById(questionBank);
                logger.debug("更新成功", JSON.toJSONString(questionBank, true));
            }
            return RespDto.success(questionBankId != null);
        } catch (Exception e) {
            logger.error("保存 questionBank 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.questionBank.domain.QuestionBank>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findById")
    public RespDto<QuestionBank> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            QuestionBank questionBank = questionBankService.findById(id);
            return RespDto.success(questionBank);
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
            int count = questionBankService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

