package co.bugu.tes.statistics.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.judge.domain.Judge;
import co.bugu.tes.judge.service.IJudgeService;
import co.bugu.tes.multi.domain.Multi;
import co.bugu.tes.multi.service.IMultiService;
import co.bugu.tes.questionBank.domain.QuestionBank;
import co.bugu.tes.questionBank.service.IQuestionBankService;
import co.bugu.tes.single.domain.Single;
import co.bugu.tes.single.service.ISingleService;
import co.bugu.tes.statistics.dao.StatisticsDao;
import co.bugu.tes.statistics.dto.*;
import co.bugu.tes.statistics.service.IStatService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author daocers
 * @Date 2019/5/8:15:18
 * @Description:
 */
@Service
public class StatServiceImpl implements IStatService {
    @Autowired
    ISingleService singleService;
    @Autowired
    IMultiService multiService;
    @Autowired
    IJudgeService judgeService;

    @Autowired
    IQuestionBankService questionBankService;

    @Autowired
    StatisticsDao statisticsDao;


    private Logger logger = LoggerFactory.getLogger(StatServiceImpl.class);

    @Override
    public List<SceneQuestionStatDto> getSceneQuestionStat(Integer type, Integer size) {
        return null;
    }

    @Override
    public List<QuestionBankStatDto> getQuestionBankStat() {
        List<QuestionBankStatDto> res = new ArrayList<>();

        QuestionBank query = new QuestionBank();
        query.setIsDel(DelFlagEnum.NO.getCode());
        List<QuestionBank> banks = questionBankService.findByCondition(query);
        if (CollectionUtils.isEmpty(banks)) {
            return res;
        }
        for (QuestionBank bank : banks) {
            QuestionBankStatDto dto = new QuestionBankStatDto();
            Long bankId = bank.getId();
            dto.setBankId(bankId);
            dto.setName(bank.getName());


            Long total = 0L;
            Date last = null;

            Single singleQuery = new Single();
            singleQuery.setBankId(bankId);
            singleQuery.setIsDel(DelFlagEnum.NO.getCode());

            PageInfo<Single> singlePageInfo = singleService.findByConditionWithPage(1, 1, singleQuery);
            if (CollectionUtils.isNotEmpty(singlePageInfo.getList())) {
                total += singlePageInfo.getTotal();
                last = singlePageInfo.getList().get(0).getUpdateTime();
            }

            Multi multiQuery = new Multi();
            multiQuery.setBankId(bankId);
            multiQuery.setIsDel(DelFlagEnum.NO.getCode());
            PageInfo<Multi> multiPageInfo = multiService.findByConditionWithPage(1, 1, multiQuery);
            if (CollectionUtils.isNotEmpty(multiPageInfo.getList())) {
                total += multiPageInfo.getTotal();
                Date tmp = multiPageInfo.getList().get(0).getUpdateTime();
                if (last == null || last.before(tmp)) {
                    last = tmp;
                }
            }

            Judge judge = new Judge();
            judge.setBankId(bankId);
            judge.setIsDel(DelFlagEnum.NO.getCode());
            PageInfo<Judge> judgePageInfo = judgeService.findByConditionWithPage(1, 1, judge);
            if (CollectionUtils.isNotEmpty(judgePageInfo.getList())) {
                total += judgePageInfo.getTotal();
                Date tmp = judgePageInfo.getList().get(0).getUpdateTime();
                if (last == null || last.before(tmp)) {
                    last = tmp;
                }
            }

            dto.setQuestionCount(total.intValue());
            dto.setLastUpdateTime(last);
            res.add(dto);
        }
        return res;
    }

    @Override
    public QuestionDistributeDto getQuestionPropStat(Long bankId) {
        QuestionDistributeDto questionDistributeDto = new QuestionDistributeDto();
        List<QuestionStat> singleStats = getSingleStat(bankId);
        List<QuestionStat> multiStats = getMultiStat(bankId);
        List<QuestionStat> judgeStats = getJudgeStat(bankId);

        List<QuestionStat> all = new ArrayList<>();
        if (null != singleStats) {
            all.addAll(singleStats);
        }
        if (null != multiStats) {
            all.addAll(multiStats);
        }
        if (null != judgeStats) {
            all.addAll(judgeStats);
        }

        List<BusiTypeStatDto> res = new ArrayList<>();
        if (CollectionUtils.isEmpty(all)) {
            logger.warn("还没有试题信息呢");
            return questionDistributeDto;
        }
        Map<Integer, Integer> busiMap = new HashMap<>();
        Map<Integer, Integer> diffMap = new HashMap<>();
        for (QuestionStat stat : all) {
            int busiType = stat.getBusiType();
            int diff = stat.getDifficulty();
            int count = stat.getCount();
            if (!busiMap.containsKey(busiType)) {
                busiMap.put(busiType, 0);
            }
            if (!diffMap.containsKey(diff)) {
                diffMap.put(diff, 0);
            }
            busiMap.put(busiType, busiMap.get(busiType) + count);
            diffMap.put(diff, diffMap.get(diff) + count);
        }

        Set<Integer> busiKeys = busiMap.keySet();
        Set<Integer> diffKeys = diffMap.keySet();
        for (Integer busi : busiKeys) {
            BusiTypeStatDto dto = new BusiTypeStatDto();
            dto.setBusiType(busi);
            dto.setCount(busiMap.get(busi));
            dto.setBankId(bankId);
            res.add(dto);
        }

        List<DifficultyStatDto> diffs = new ArrayList<>();
        for (Integer diff : diffKeys) {
            DifficultyStatDto dto = new DifficultyStatDto();
            dto.setBankId(bankId);
            dto.setCount(diffMap.get(diff));
            dto.setDifficulty(diff);
            diffs.add(dto);
        }
        questionDistributeDto.setBusiTypeStatDtoList(res);
        questionDistributeDto.setDifficultyStatDtoList(diffs);
        return questionDistributeDto;
    }

    @Override
    public List<DifficultyStatDto> getDifficultyStat(Long bankId) {
        return null;
    }

    @Override
    public List<QuestionStat> getSingleStat(Long bankId) {
        return statisticsDao.getSingleStat(bankId);
    }

    @Override
    public List<QuestionStat> getMultiStat(Long bankId) {
        return statisticsDao.getMultiStat(bankId);
    }

    @Override
    public List<QuestionStat> getJudgeStat(Long bankId) {
        return statisticsDao.getJudgeStat(bankId);
    }
}
