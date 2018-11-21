package co.bugu.tes.paper.api;

import co.bugu.common.RespDto;
import co.bugu.tes.paper.domain.Paper;
import co.bugu.tes.paper.service.IPaperService;
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
@RequestMapping("/paper/api")
public class PaperApi {
    private Logger logger = LoggerFactory.getLogger(PaperApi.class);

    @Autowired
    IPaperService paperService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<Paper>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Paper paper) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(paper, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<Paper> list = paperService.findByCondition(pageNum, pageSize, paper);
            PageInfo<Paper> pageInfo = new PageInfo<>(list);
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
     * @param paper
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> savePaper(@RequestBody Paper paper) {
        try {
            Long paperId = paper.getId();
            if(null == paperId){
                logger.debug("保存， savePaper, 参数： {}", JSON.toJSONString(paper, true));
                paperId = paperService.add(paper);
                logger.info("新增 成功， id: {}", paperId);
            }else{
                paperService.updateById(paper);
                logger.debug("更新成功", JSON.toJSONString(paper, true));
            }
            return RespDto.success(paperId != null);
        } catch (Exception e) {
            logger.error("保存 paper 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.paper.domain.Paper>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findById")
    public RespDto<Paper> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Paper paper = paperService.findById(id);
            return RespDto.success(paper);
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
            int count = paperService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}
