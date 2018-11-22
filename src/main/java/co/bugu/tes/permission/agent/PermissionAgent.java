package co.bugu.tes.permission.agent;

import co.bugu.tes.permission.domain.Permission;
import co.bugu.tes.permission.dto.PermissionTreeDto;
import co.bugu.tes.permission.service.IPermissionService;
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
 * 权限业务逻辑类
 *
 * @author daocers
 * @createTime 2017/12/5
 */
@Service
public class PermissionAgent {
    private static Logger logger = LoggerFactory.getLogger(PermissionAgent.class);

    @Autowired
    IPermissionService permissionService;

    public List<PermissionTreeDto> getPermissionTree() {
        List<Permission> permissionList = permissionService.findByCondition(null);
        if (CollectionUtils.isEmpty(permissionList)) {
            return new ArrayList<>();
        }
        List<PermissionTreeDto> treeDtos = Lists.transform(permissionList, new Function<Permission, PermissionTreeDto>() {
            @Nullable
            @Override
            public PermissionTreeDto apply(@Nullable Permission permission) {
                PermissionTreeDto dto = new PermissionTreeDto();
                BeanUtils.copyProperties(permission, dto);
                return dto;
            }
        });

        Map<Long, List<PermissionTreeDto>> info = new HashMap<>();
        List<PermissionTreeDto> rootList = new ArrayList<>();
        for(PermissionTreeDto dto: treeDtos){
            Long id = dto.getSuperiorId();
            if(!info.containsKey(id)){
                info.put(id, new ArrayList<>());
            }
            info.get(id).add(dto);
            if(null == id || id < 1){
                rootList.add(dto);
            }
        }

        for(PermissionTreeDto dto: rootList){
            dto.setChildren(getChildren(dto.getId(), info));
        }
        return rootList;
    }


    private List<PermissionTreeDto> getChildren(Long id, Map<Long, List<PermissionTreeDto>> info) {
        List<PermissionTreeDto> children = info.get(id);
        if(CollectionUtils.isNotEmpty(children)){
            for(PermissionTreeDto dto: children){
                dto.setChildren(getChildren(dto.getId(), info));
            }
        }
        return children;
    }

}
