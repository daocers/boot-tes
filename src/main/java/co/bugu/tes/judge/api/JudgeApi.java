package co.bugu.tes.judge.api;

import co.bugu.common.RespDto;
import co.bugu.tes.judge.domain.Judge;
import co.bugu.tes.judge.service.IJudgeService;
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
@RequestMapping("/judge/api")
public class JudgeApi {
    private Logger logger = LoggerFactory.getLogger(JudgeApi.class);

    @Autowired
    IJudgeService judgeService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<Judge>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Judge judge) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(judge, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<Judge> list = judgeService.findByCondition(pageNum, pageSize, judge);
            PageInfo<Judge> pageInfo = new PageInfo<>(list);
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
     * @param judge
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveJudge(@RequestBody Judge judge) {
        try {
            Long judgeId = judge.getId();
            if(null == judgeId){
                logger.debug("保存， saveJudge, 参数： {}", JSON.toJSONString(judge, true));
                judgeId = judgeService.add(judge);
                logger.info("新增 成功， id: {}", judgeId);
            }else{
                judgeService.updateById(judge);
                logger.debug("更新成功", JSON.toJSONString(judge, true));
            }
            return RespDto.success(judgeId != null);
        } catch (Exception e) {
            logger.error("保存 judge 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.judge.domain.Judge>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findById")
    public RespDto<Judge> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Judge judge = judgeService.findById(id);
            return RespDto.success(judge);
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
            int count = judgeService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

