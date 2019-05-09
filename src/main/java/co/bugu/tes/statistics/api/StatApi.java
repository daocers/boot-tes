package co.bugu.tes.statistics.api;

import co.bugu.common.RespDto;
import co.bugu.tes.paper.domain.Paper;
import co.bugu.tes.paper.service.IPaperService;
import co.bugu.tes.questionStat.domain.QuestionStat;
import co.bugu.tes.questionStat.service.IQuestionStatService;
import co.bugu.tes.statistics.dto.QuestionBankStatDto;
import co.bugu.tes.statistics.dto.QuestionDistributeDto;
import co.bugu.tes.statistics.dto.SceneQuestionStatDto;
import co.bugu.tes.statistics.service.IStatService;
import co.bugu.tes.statistics.service.IStatisticsService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author daocers
 * @Date 2019/5/8:16:09
 * @Description:
 */
@RestController
@RequestMapping("/stat/api")
public class StatApi {
    private Logger logger = LoggerFactory.getLogger(StatApi.class);

    @Autowired
    IStatService statService;

    @Autowired
    IStatisticsService statisticsService;

    @Autowired
    IQuestionStatService questionStatService;

    @Autowired
    IPaperService paperService;

    @RequestMapping("/getBankStat")
    public RespDto<List<QuestionBankStatDto>> getBankStat() {
        List<QuestionBankStatDto> list = statService.getQuestionBankStat();
        return RespDto.success(list);
    }

    @RequestMapping("/getQuestionPropStat")
    public RespDto<QuestionDistributeDto> getQuesitonPropStat(Long bankId) {
        QuestionDistributeDto dto = statService.getQuestionPropStat(bankId);
        return RespDto.success(dto);
    }

    @RequestMapping("/getUserStat")
    public RespDto<List> getUserStat() {
        return RespDto.success();
    }


    /**
     * 指定场次的试题情况
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/9 14:56
     */
    @RequestMapping("/getSceneQuestionStat")
    public RespDto<List<SceneQuestionStatDto>> getSceneQuestionStat() {
        List<SceneQuestionStatDto> list = statisticsService.getSceneQuestionStat(15);
        list = Lists.reverse(list);
        return RespDto.success(list);
    }


    /**
     * 获取试题信息，错题量，错题率
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/9 15:03
     */
    @RequestMapping("/getQuestionStat")
    public RespDto<List<QuestionStat>> getQuestionStat(String type, Integer size) {
        if (StringUtils.isEmpty(type)) {
            type = "total";
        }
        if (type.equalsIgnoreCase("wrong_rate")
                || type.equalsIgnoreCase("wrong_count")
                || type.equalsIgnoreCase("total")) {
            type = type.toLowerCase() + " desc";
        } else {
            logger.warn("不支持的排序类型");
            type = "total desc";
        }
        if (null == size) {
            size = 15;
        }
        if (StringUtils.isEmpty(type)) {
            type = "total desc";
        }
        List<QuestionStat> list = questionStatService.findByCondition(1, size, new QuestionStat(), type);
        list = Lists.reverse(list);
        return RespDto.success(list);
    }


    /**
     * 获取平均成绩统计
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/9 15:59
     */
    @RequestMapping("/getSceneScoreStat")
    public RespDto<List<Paper>> getSceneScoreStat(Integer size) {
        if (size == null) {
            size = 15;
        }
        List<Paper> list = paperService.getSceneScoreStat(size);
        list = Lists.reverse(list);
        return RespDto.success(list);
    }
}
