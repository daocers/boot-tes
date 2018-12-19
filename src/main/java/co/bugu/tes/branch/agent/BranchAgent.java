package co.bugu.tes.branch.agent;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.branch.domain.Branch;
import co.bugu.tes.branch.dto.BranchTreeDto;
import co.bugu.tes.branch.service.IBranchService;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务处理类
 *
 * @author daocers
 * @createTime 2017/12/6
 */
@Service
public class BranchAgent {
    private static Logger logger = LoggerFactory.getLogger(BranchAgent.class);

    @Autowired
    IBranchService branchService;

    /***
     *      获取机构树
     * @author daocers
     * @date 2017/12/6 14:55
     * @param
     * @return java.util.List<co.bugu.tes.branch.dto.BranchTreeDto>
     */
    public List<BranchTreeDto> getBranchTree() {
        List<Branch> branches = branchService.findByCondition(null);

        if (CollectionUtils.isNotEmpty(branches)) {
            List<BranchTreeDto> dtoList = Lists.transform(branches, new Function<Branch, BranchTreeDto>() {
                @Nullable
                @Override
                public BranchTreeDto apply(@Nullable Branch branch) {
                    BranchTreeDto dto = new BranchTreeDto();
                    BeanUtils.copyProperties(branch, dto);
                    return dto;
                }
            });

            List<BranchTreeDto> rootList = new ArrayList<>();
            Map<Long, List<BranchTreeDto>> info = new HashMap<>();
            for (BranchTreeDto dto : dtoList) {
                if (!info.containsKey(dto.getSuperiorId())) {
                    info.put(dto.getSuperiorId(), new ArrayList<>());
                }
                info.get(dto.getSuperiorId()).add(dto);
                if (dto.getSuperiorId() < 1) {
                    rootList.add(dto);
                }
            }

            for (BranchTreeDto branchTreeDto : rootList) {
                branchTreeDto.setChildren(getChildren(branchTreeDto.getId(), info));
//                getAndSetChildren(branchTreeDto, info);
            }

            logger.info("获取到树信息： {}", rootList);
            return rootList;

        }
        return null;
    }

    /**
     * 递归获取下级
     *
     * @param id
     * @param info
     * @return
     */
    private List<BranchTreeDto> getChildren(Long id, Map<Long, List<BranchTreeDto>> info) {
        List<BranchTreeDto> children = info.get(id);

        if (CollectionUtils.isEmpty(children)) {
            return null;
        } else {
            for (BranchTreeDto child : children) {
                child.setChildren(getChildren(child.getId(), info));
            }
        }
        return children;
    }


}
