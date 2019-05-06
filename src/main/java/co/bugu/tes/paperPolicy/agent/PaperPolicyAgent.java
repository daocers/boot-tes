package co.bugu.tes.paperPolicy.agent;

import co.bugu.tes.judge.domain.Judge;
import co.bugu.tes.judge.service.IJudgeService;
import co.bugu.tes.multi.domain.Multi;
import co.bugu.tes.multi.service.IMultiService;
import co.bugu.tes.paperPolicy.domain.PaperPolicy;
import co.bugu.tes.paperPolicy.dto.ItemDto;
import co.bugu.tes.paperPolicy.dto.PaperPolicyCheckDto;
import co.bugu.tes.paperPolicy.service.IPaperPolicyService;
import co.bugu.tes.single.domain.Single;
import co.bugu.tes.single.service.ISingleService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author daocers
 * @Date 2019/5/6:14:46
 * @Description:
 */
@Service
public class PaperPolicyAgent {
    @Autowired
    IPaperPolicyService paperPolicyService;
    @Autowired
    ISingleService singleService;
    @Autowired
    IMultiService multiService;
    @Autowired
    IJudgeService judgeService;


    public PaperPolicyCheckDto checkPolciy(Long paperPolicyId, Long bankId) throws Exception {
        PaperPolicyCheckDto res = new PaperPolicyCheckDto();
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
            if (singlePageInfo.getSize() < count) {
                res.setValid(false);
            }
            item.setRealCount(singlePageInfo.getSize());
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
            if (multiPageInfo.getSize() < count) {
                res.setValid(false);
            }
            item.setRealCount(multiPageInfo.getSize());
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
            if (judgePageInfo.getSize() < count) {
                res.setValid(false);
            }
            item.setRealCount(judgePageInfo.getSize());
            res.getJudges().add(item);
        }
        res.setId(paperPolicyId);
        res.setName(paperPolicy.getName());
        res.setBankId(bankId);
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
        Single query = new Single();
        query.setBankId(questionBankId);

        PageInfo<Single> singlePageInfo = singleService.findByConditionWithPage(1, 1, query);
        if (singlePageInfo.getSize() < singleCount) {
            res.setValid(false);
        }
        res.setSingleCount(singleCount);
        res.setRealSingleCount(singlePageInfo.getSize());

        Multi multi = new Multi();
        multi.setBankId(questionBankId);
        PageInfo<Multi> multiPageInfo = multiService.findByConditionWithPage(1, 1, multi);
        if (multiPageInfo.getSize() < multiCount) {
            res.setValid(false);
        }
        res.setMultiCount(multiCount);
        res.setRealMultiCount(multiPageInfo.getSize());

        Judge judge = new Judge();
        judge.setBankId(questionBankId);
        PageInfo<Judge> judgePageInfo = judgeService.findByConditionWithPage(1, 1, judge);
        if (judgePageInfo.getSize() < judgeCount) {
            res.setValid(false);
        }
        res.setJudgeCount(judgeCount);
        res.setRealJudgeCount(judgePageInfo.getSize());

        return res;
    }
}
