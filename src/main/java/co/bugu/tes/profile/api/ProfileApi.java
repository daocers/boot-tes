package co.bugu.tes.profile.api;

import co.bugu.common.RespDto;
import co.bugu.tes.profile.domain.Profile;
import co.bugu.tes.profile.service.IProfileService;
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
 * @create 2018-11-19 19:29
 */
@RestController
@RequestMapping("/profile/api")
public class ProfileApi {
    private Logger logger = LoggerFactory.getLogger(ProfileApi.class);

    @Autowired
    IProfileService profileService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-19 19:29
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<Profile>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Profile profile) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(profile, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<Profile> list = profileService.findByCondition(pageNum, pageSize, profile);
            PageInfo<Profile> pageInfo = new PageInfo<>(list);
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
     * @param profile
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-19 19:29
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveProfile(@RequestBody Profile profile) {
        try {
            Long profileId = profile.getId();
            if(null == profileId){
                logger.debug("保存， saveProfile, 参数： {}", JSON.toJSONString(profile, true));
                profileId = profileService.add(profile);
                logger.info("新增 成功， id: {}", profileId);
            }else{
                profileService.updateById(profile);
                logger.debug("更新成功", JSON.toJSONString(profile, true));
            }
            return RespDto.success(profileId != null);
        } catch (Exception e) {
            logger.error("保存 profile 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.profile.domain.Profile>
     * @author daocers
     * @date 2018-11-19 19:29
     */
    @RequestMapping(value = "/findById")
    public RespDto<Profile> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Profile profile = profileService.findById(id);
            return RespDto.success(profile);
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
     * @date 2018-11-19 19:29
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = profileService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

