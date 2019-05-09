package co.bugu.tes.paperPolicy.agent;

import co.bugu.common.enums.BaseStatusEnum;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.judge.domain.Judge;
import co.bugu.tes.judge.service.IJudgeService;
import co.bugu.tes.multi.domain.Multi;
import co.bugu.tes.multi.service.IMultiService;
import co.bugu.tes.paperPolicy.domain.PaperPolicy;
import co.bugu.tes.paperPolicy.dto.ItemDto;
import co.bugu.tes.paperPolicy.dto.PaperPolicyCheckDto;
import co.bugu.tes.paperPolicy.service.IPaperPolicyService;
import co.bugu.tes.paperPolicyCondition.domain.PaperPolicyCondition;
import co.bugu.tes.paperPolicyCondition.service.IPaperPolicyConditionService;
import co.bugu.tes.questionBank.domain.QuestionBank;
import co.bugu.tes.questionBank.service.IQuestionBankService;
import co.bugu.tes.single.domain.Single;
import co.bugu.tes.single.service.ISingleService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author daocers
 * @Date 2019/5/6:14:46
 * @Description:
 */
@Service
public class PaperPolicyAgent {
    private Logger logger = LoggerFactory.getLogger(PaperPolicyAgent.class);
    @Autowired
    IPaperPolicyService paperPolicyService;
    @Autowired
    ISingleService singleService;
    @Autowired
    IMultiService multiService;
    @Autowired
    IJudgeService judgeService;

    @Autowired
    IQuestionBankService questionBankService;
    @Autowired
    IPaperPolicyConditionService paperPolicyConditionService;


    public PaperPolicyCheckDto checkPolicy(Long paperPolicyId, Long bankId) throws Exception {
        PaperPolicyCheckDto res = new PaperPolicyCheckDto();
        res.setValid(true);
        res.setSingles(new ArrayList<>());
        res.setMulties(new ArrayList<>());
        res.setJudges(new ArrayList<>());
        PaperPolicy paperPolicy = paperPolicyService.findById(paperPolicyId);
        if (null == paperPolicy) {
            throw new Exception("试卷策略不存在");
        }
        List<ItemDto> singles = JSON.parseArray(paperPolicy.getSingleInfo(), ItemDto.class);
        List<ItemDto> multies = JSON.parseArray(paperPolicy.getMultiInfo(), ItemDto.class);
        List<ItemDto> judges = JSON.parseArray(paperPolicy.getJudgeInfo(), ItemDto.class);

        for (ItemDto item : singles) {
            Integer busiType = item.getBusiType();
            Integer diff = item.getDifficulty();
            Integer count = item.getCount();

            Single query = new Single();
            query.setBankId(bankId);
            query.setAttr1(busiType);
            query.setAttr2(diff);
            PageInfo<Single> singlePageInfo = singleService.findByConditionWithPage(1, 1, query);
            if (singlePageInfo.getTotal() < count) {
                res.setValid(false);
            }
            item.setRealCount((int) singlePageInfo.getTotal());
            res.getSingles().add(item);
        }
        for (ItemDto item : multies) {
            Integer busiType = item.getBusiType();
            Integer diff = item.getDifficulty();
            Integer count = item.getCount();
            Multi query = new Multi();
            query.setBankId(bankId);
            query.setAttr1(busiType);
            query.setAttr2(diff);
            PageInfo<Multi> multiPageInfo = multiService.findByConditionWithPage(1, 1, query);
            if (multiPageInfo.getTotal() < count) {
                res.setValid(false);
            }
            item.setRealCount((int) multiPageInfo.getTotal());
            res.getMulties().add(item);
        }
        for (ItemDto item : judges) {
            Integer busiType = item.getBusiType();
            Integer diff = item.getDifficulty();
            Integer count = item.getCount();
            Judge query = new Judge();
            query.setBankId(bankId);
            query.setAttr1(busiType);
            query.setAttr2(diff);
            PageInfo<Judge> judgePageInfo = judgeService.findByConditionWithPage(1, 1, query);
            if (judgePageInfo.getTotal() < count) {
                res.setValid(false);
            }
            item.setRealCount((int) judgePageInfo.getTotal());
            res.getJudges().add(item);
        }
        res.setId(paperPolicyId);
        res.setName(paperPolicy.getName());
        res.setBankId(bankId);

//        更新试卷策略校验数据到库
        PaperPolicyCondition condition = new PaperPolicyCondition();
        condition.setBankId(bankId);
        condition.setPaperPolicyId(paperPolicyId);
        condition.setStatus(res.getValid() ? BaseStatusEnum.ENABLE.getCode() : BaseStatusEnum.DISABLE.getCode());
        condition.setPaperPolicyName(paperPolicy.getName());
        condition.setIsDel(DelFlagEnum.NO.getCode());
        condition.setSingleInfo(JSON.toJSONString(res.getSingles()));
        condition.setMultiInfo(JSON.toJSONString(res.getMulties()));
        condition.setJudgeInfo(JSON.toJSONString(res.getJudges()));
        condition.setCreateUserId(0L);
        condition.setUpdateUserId(0L);
        condition.setPaperPolicyName(paperPolicy.getName());

        List<PaperPolicyCondition> conditions = paperPolicyConditionService.findByCondition(condition);
        if (CollectionUtils.isEmpty(conditions)) {
            paperPolicyConditionService.add(condition);
        } else {
            condition.setId(conditions.get(0).getId());
            paperPolicyConditionService.updateById(condition);
        }
        return res;
    }

    /**
     * 校验基本试卷策略
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/6 16:34
     */
    public PaperPolicyCheckDto checkSimple(Integer singleCount, Integer multiCount, Integer judgeCount, Long questionBankId) {
        PaperPolicyCheckDto res = new PaperPolicyCheckDto();
        res.setValid(true);
        Single query = new Single();
        query.setBankId(questionBankId);

        PageInfo<Single> singlePageInfo = singleService.findByConditionWithPage(1, 1, query);
        if (singlePageInfo.getTotal() < singleCount) {
            res.setValid(false);
        }
        res.setSingleCount(singleCount);
        res.setRealSingleCount((int) singlePageInfo.getTotal());

        Multi multi = new Multi();
        multi.setBankId(questionBankId);
        PageInfo<Multi> multiPageInfo = multiService.findByConditionWithPage(1, 1, multi);
        if (multiPageInfo.getTotal() < multiCount) {
            res.setValid(false);
        }
        res.setMultiCount(multiCount);
        res.setRealMultiCount((int) multiPageInfo.getTotal());

        Judge judge = new Judge();
        judge.setBankId(questionBankId);
        PageInfo<Judge> judgePageInfo = judgeService.findByConditionWithPage(1, 1, judge);
        if (judgePageInfo.getTotal() < judgeCount) {
            res.setValid(false);
        }
        res.setJudgeCount(judgeCount);
        res.setRealJudgeCount((int) judgePageInfo.getTotal());

        return res;
    }


    /**
     * 检查所有的试题策略，并更新数据到库
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/7 10:17
     */
    @Transactional
    public void checkAllPolicyAndRefreshRecord() throws Exception {
        QuestionBank questionBank = new QuestionBank();
        questionBank.setIsDel(DelFlagEnum.NO.getCode());
        List<QuestionBank> questionBanks = questionBankService.findByCondition(questionBank);
        if (CollectionUtils.isEmpty(questionBanks)) {
            logger.info("没有题库信息");
            return;
        }

        PaperPolicy paperPolicy = new PaperPolicy();
        paperPolicy.setIsDel(DelFlagEnum.NO.getCode());
        List<PaperPolicy> paperPolicies = paperPolicyService.findByCondition(paperPolicy);
        if (CollectionUtils.isEmpty(paperPolicies)) {
            logger.info("没有试卷策略信息");
            return;
        }
        List<PaperPolicyCondition> conditionList = new ArrayList<>();
        for (QuestionBank bank : questionBanks) {
            Long bankId = bank.getId();
            for (PaperPolicy policy : paperPolicies) {
                Long paperPolicyId = policy.getId();
                PaperPolicyCheckDto dto = checkPolicy(paperPolicyId, bankId);
                PaperPolicyCondition condition = new PaperPolicyCondition();
                condition.setBankId(bankId);
                condition.setPaperPolicyId(paperPolicyId);
                condition.setIsDel(DelFlagEnum.NO.getCode());
                condition.setStatus(dto.getValid() ? BaseStatusEnum.ENABLE.getCode() : BaseStatusEnum.DISABLE.getCode());
                condition.setSingleInfo(JSON.toJSONString(dto.getSingles()));
                condition.setMultiInfo(JSON.toJSONString(dto.getMulties()));
                condition.setJudgeInfo(JSON.toJSONString(dto.getJudges()));
                condition.setCreateUserId(0L);
                condition.setUpdateUserId(0L);
                condition.setPaperPolicyName(policy.getName());
                conditionList.add(condition);
            }
        }

//        先删除现有数据
        paperPolicyConditionService.deleteAll();
//        再批量添加
        paperPolicyConditionService.batchAdd(conditionList);
    }
}
