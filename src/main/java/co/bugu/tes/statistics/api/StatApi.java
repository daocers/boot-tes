package co.bugu.tes.statistics.api;

import co.bugu.common.RespDto;
import co.bugu.tes.statistics.dto.QuestionBankStatDto;
import co.bugu.tes.statistics.dto.QuestionDistributeDto;
import co.bugu.tes.statistics.dto.SceneQuestionStatDto;
import co.bugu.tes.statistics.service.IStatService;
import co.bugu.tes.statistics.service.IStatisticsService;
import com.google.common.collect.Lists;
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


    @RequestMapping("/getSceneQuestionStat")
    public RespDto<List<SceneQuestionStatDto>> getSceneQuestionStat() {
        List<SceneQuestionStatDto> list = statisticsService.getSceneQuestionStat(15);
        list = Lists.reverse(list);
        return RespDto.success(list);
    }
}
