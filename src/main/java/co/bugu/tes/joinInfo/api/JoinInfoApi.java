package co.bugu.tes.joinInfo.api;

import co.bugu.common.RespDto;
import co.bugu.tes.joinInfo.domain.JoinInfo;
import co.bugu.tes.joinInfo.service.IJoinInfoService;
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
 * @create 2018-12-16 23:21
 */
@RestController
@RequestMapping("/joinInfo/api")
public class JoinInfoApi {
    private Logger logger = LoggerFactory.getLogger(JoinInfoApi.class);

    @Autowired
    IJoinInfoService joinInfoService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-12-16 23:21
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<JoinInfo>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody JoinInfo joinInfo) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(joinInfo, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<JoinInfo> list = joinInfoService.findByCondition(pageNum, pageSize, joinInfo);
            PageInfo<JoinInfo> pageInfo = new PageInfo<>(list);
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
     * @param joinInfo
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-12-16 23:21
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveJoinInfo(@RequestBody JoinInfo joinInfo) {
        try {
            Long joinInfoId = joinInfo.getId();
            if(null == joinInfoId){
                logger.debug("保存， saveJoinInfo, 参数： {}", JSON.toJSONString(joinInfo, true));
                joinInfoId = joinInfoService.add(joinInfo);
                logger.info("新增 成功， id: {}", joinInfoId);
            }else{
                joinInfoService.updateById(joinInfo);
                logger.debug("更新成功", JSON.toJSONString(joinInfo, true));
            }
            return RespDto.success(joinInfoId != null);
        } catch (Exception e) {
            logger.error("保存 joinInfo 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.joinInfo.domain.JoinInfo>
     * @author daocers
     * @date 2018-12-16 23:21
     */
    @RequestMapping(value = "/findById")
    public RespDto<JoinInfo> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            JoinInfo joinInfo = joinInfoService.findById(id);
            return RespDto.success(joinInfo);
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
     * @date 2018-12-16 23:21
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = joinInfoService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

