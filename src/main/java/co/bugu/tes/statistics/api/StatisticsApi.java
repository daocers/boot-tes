package co.bugu.tes.statistics.api;

import co.bugu.common.RespDto;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author daocers
 * @Date 2019/5/6:10:56
 * @Description:
 */
@RestController
@RequestMapping("/statistics/api")
public class StatisticsApi {
    private Logger logger = LoggerFactory.getLogger(StatisticsApi.class);


    /**
     * 获取指定场次错题数量列表
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/6 10:59
     */
    @RequestMapping("/getWrongCountList")
    public RespDto<PageInfo<Integer>> getQuestionWrongCountOfScene(Long sceneId, Integer pageNum, Integer pageSize) {
        return RespDto.success();
    }


    /**
     * 获取指定场次的试题错误率列表
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/6 11:02
     */
    @RequestMapping("/getWrongRateList")
    public RespDto<PageInfo<Float>> getWrongRateOfScene(Long sceneId, Integer pageNum, Integer pageSize) {
        return RespDto.success();
    }


    /**
     * 获取指定试题每场考试的错误率
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/6 11:11
     */
    @RequestMapping("/getQuestionFollow")
    public RespDto<PageInfo<Integer>> getQuestionWrongCountWithScene(Long questionId, Integer pageNum, Integer pageSize){
        return RespDto.success();
    }
}
