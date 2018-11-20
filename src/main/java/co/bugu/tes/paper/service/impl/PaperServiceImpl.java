package co.bugu.tes.paper.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.paper.dao.PaperDao;
import co.bugu.tes.paper.domain.Paper;
import co.bugu.tes.paper.service.IPaperService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author daocers
 * @create 2018-11-20 17:15
 */
@Service
public class PaperServiceImpl implements IPaperService {
    @Autowired
    PaperDao paperDao;

    private Logger logger = LoggerFactory.getLogger(PaperServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(Paper paper) {
        //todo 校验参数
        paper.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        paper.setCreateTime(now);
        paper.setUpdateTime(now);
        paperDao.insert(paper);
        return paper.getId();
    }

    @Override
    public int updateById(Paper paper) {
        logger.debug("paper updateById, 参数： {}", JSON.toJSONString(paper, true));
        Preconditions.checkNotNull(paper.getId(), "id不能为空");
        paper.setUpdateTime(new Date());
        return paperDao.updateById(paper);
    }

    @Override
    public List<Paper> findByCondition(Paper paper) {
        logger.debug("paper findByCondition, 参数： {}", JSON.toJSONString(paper, true));
        PageHelper.orderBy(ORDER_BY);
        List<Paper> papers = paperDao.findByObject(paper);

        logger.debug("查询结果， {}", JSON.toJSONString(papers, true));
        return papers;
    }

    @Override
    public List<Paper> findByCondition(Integer pageNum, Integer pageSize, Paper paper) {
        logger.debug("paper findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(paper, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Paper> papers = paperDao.findByObject(paper);

        logger.debug("查询结果， {}", JSON.toJSONString(papers, true));
        return papers;
    }

    @Override
    public PageInfo<Paper> findByConditionWithPage(Integer pageNum, Integer pageSize, Paper paper) {
        logger.debug("paper findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(paper, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Paper> papers = paperDao.findByObject(paper);

        logger.debug("查询结果， {}", JSON.toJSONString(papers, true));
        return new PageInfo<>(papers);
    }

    @Override
    public Paper findById(Long paperId) {
        logger.debug("paper findById, 参数 paperId: {}", paperId);
        Paper paper = paperDao.selectById(paperId);

        logger.debug("查询结果： {}", JSON.toJSONString(paper, true));
        return paper;
    }

    @Override
    public int deleteById(Long paperId, Long operatorId) {
        logger.debug("paper 删除， 参数 paperId : {}", paperId);
        Paper paper = new Paper();
        paper.setId(paperId);
        paper.setIsDel(DelFlagEnum.YES.getCode());
        paper.setUpdateTime(new Date());
        paper.setUpdateUserId(operatorId);
        int num = paperDao.updateById(paper);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
