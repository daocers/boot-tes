package co.bugu.tes.paper.api;

import co.bugu.common.RespDto;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.paper.domain.Paper;
import co.bugu.tes.paper.dto.PaperDto;
import co.bugu.tes.paper.enums.AnswerFlagEnum;
import co.bugu.tes.paper.service.IPaperService;
import co.bugu.tes.scene.domain.Scene;
import co.bugu.tes.scene.service.ISceneService;
import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.service.IUserService;
import co.bugu.util.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    IUserService userService;
    @Autowired
    ISceneService sceneService;


    /**
     * 下载成绩
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/11 10:28
     */
    @RequestMapping(value = "/downloadScore")
    public void downloadScore(Long sceneId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Scene scene = sceneService.findById(sceneId);

            Paper query = new Paper();
            query.setSceneId(sceneId);
            query.setIsDel(DelFlagEnum.NO.getCode());
            List<Paper> paperList = paperService.findByCondition(query);
            List<List<String>> data = new ArrayList<>();
            List<String> title = Arrays.asList(new String[]{"用户名", "姓名",
                    "场次编号", "场次名称", "试卷编号", "百分制成绩", "原始成绩", "知识类得分", "凭条得分", "凭条数量", "凭条正确率",
                    "作答标志", "入场时间", "交卷时间"});
            List<List<String>> contents = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(paperList)) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                contents = Lists.transform(paperList, new Function<Paper, List<String>>() {
                    @Override
                    public List<String> apply(@Nullable Paper paper) {
                        List<String> line = new ArrayList<>();
                        User user = userService.findById(paper.getUserId());
                        if (null != user) {
                            line.add(user.getUsername());
                            line.add(user.getName());
                        } else {
                            line.add("");
                            line.add("");
                        }
                        line.add(scene.getCode());
                        line.add(scene.getName());
                        line.add(paper.getCode());
                        line.add(paper.getScore().toString());
                        line.add(paper.getOriginalScore().toString());
                        line.add(paper.getCommonScore().toString());
                        line.add(paper.getReceiptScore().toString());
                        line.add(scene.getReceiptCount().toString());
                        line.add(paper.getReceiptRate().toString());
                        int answerFlag = paper.getAnswerFlag();
                        if (AnswerFlagEnum.NO.getCode() == answerFlag) {
                            line.add("未作答");
                        } else if (AnswerFlagEnum.YES.getCode() == answerFlag) {
                            line.add("已作答");
                        } else {
                            line.add("");
                        }

                        line.add(format.format(paper.getBeginTime()));
                        line.add(format.format(paper.getEndTime()));

                        return line;
                    }
                });
            }
            data.add(title);
            data.addAll(contents);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(("成绩.xlsx").getBytes(), "iso-8859-1"));
            OutputStream outputStream = response.getOutputStream();
            ExcelUtil.writeToOutputStream("xlsx", title, contents, null, outputStream);

            outputStream.close();
        } catch (Exception e) {
            logger.error("下载成绩表失败", e);
        }

    }

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<PaperDto>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody PaperDto paperDto) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(paperDto, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }

            Long userId = null;
            String username = paperDto.getUserName();
            if (StringUtils.isNotEmpty(username)) {
                User user = new User();
                user.setUsername(username);
                user.setName(paperDto.getName());
                List<User> users = userService.findByCondition(user);
                if (CollectionUtils.isEmpty(users)) {
                    return RespDto.fail("没有找到用户");
                } else {
                    userId = users.get(0).getId();
                }
            }
            String sceneCode = paperDto.getSceneCode();
            Long sceneId = paperDto.getSceneId();
            if (null == sceneId) {
                if (StringUtils.isNotEmpty(sceneCode)) {
                    Scene scene = new Scene();
                    scene.setCode(sceneCode);
                    List<Scene> scenes = sceneService.findByCondition(scene);
                    if (CollectionUtils.isEmpty(scenes)) {
                        return RespDto.fail("没有该场次信息");
                    }
                    sceneId = scenes.get(0).getId();
                }
            }


            Paper paper = new Paper();
            paper.setUserId(userId);
            paper.setSceneId(sceneId);

            PageInfo<Paper> pageInfo = paperService.findByConditionWithPage(pageNum, pageSize, paper);
            PageInfo<PaperDto> res = new PageInfo<>();
            BeanUtils.copyProperties(pageInfo, res);
            if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
                List<PaperDto> dtos = Lists.transform(pageInfo.getList(), new Function<Paper, PaperDto>() {
                    @Override
                    public PaperDto apply(@Nullable Paper paper) {
                        PaperDto dto = new PaperDto();
                        BeanUtils.copyProperties(paper, dto);
                        User user = userService.findById(paper.getUserId());
                        Scene scene = sceneService.findById(paper.getSceneId());
                        if (null != user) {
                            dto.setUserName(user.getName());
                        }
                        if (null != scene) {
                            dto.setSceneName(scene.getName());
                            dto.setReceiptCount(scene.getReceiptCount());
                        }
                        return dto;
                    }
                });
                res.setList(dtos);
            }
            logger.info("查询到数据： {}", JSON.toJSONString(pageInfo, true));
            return RespDto.success(res);
        } catch (Exception e) {
            logger.error("findByCondition  失败", e);
            return RespDto.fail("筛选失败");
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
            if (null == paperId) {
                logger.debug("保存， savePaper, 参数： {}", JSON.toJSONString(paper, true));
                paperId = paperService.add(paper);
                logger.info("新增 成功， id: {}", paperId);
            } else {
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

