package co.bugu.tes.profile.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.profile.dao.ProfileDao;
import co.bugu.tes.profile.domain.Profile;
import co.bugu.tes.profile.service.IProfileService;
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
 * @create 2018-11-19 19:29
 */
@Service
public class ProfileServiceImpl implements IProfileService {
    @Autowired
    ProfileDao profileDao;

    private Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(Profile profile) {
        //todo 校验参数
        profile.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        profile.setCreateTime(now);
        profile.setUpdateTime(now);
        profileDao.insert(profile);
        return profile.getId();
    }

    @Override
    public int updateById(Profile profile) {
        logger.debug("profile updateById, 参数： {}", JSON.toJSONString(profile, true));
        Preconditions.checkNotNull(profile.getId(), "id不能为空");
        profile.setUpdateTime(new Date());
        return profileDao.updateById(profile);
    }

    @Override
    public List<Profile> findByCondition(Profile profile) {
        logger.debug("profile findByCondition, 参数： {}", JSON.toJSONString(profile, true));
        PageHelper.orderBy(ORDER_BY);
        List<Profile> profiles = profileDao.findByObject(profile);

        logger.debug("查询结果， {}", JSON.toJSONString(profiles, true));
        return profiles;
    }

    @Override
    public List<Profile> findByCondition(Integer pageNum, Integer pageSize, Profile profile) {
        logger.debug("profile findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(profile, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Profile> profiles = profileDao.findByObject(profile);

        logger.debug("查询结果， {}", JSON.toJSONString(profiles, true));
        return profiles;
    }

    @Override
    public PageInfo<Profile> findByConditionWithPage(Integer pageNum, Integer pageSize, Profile profile) {
        logger.debug("profile findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(profile, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Profile> profiles = profileDao.findByObject(profile);

        logger.debug("查询结果， {}", JSON.toJSONString(profiles, true));
        return new PageInfo<>(profiles);
    }

    @Override
    public Profile findById(Long profileId) {
        logger.debug("profile findById, 参数 profileId: {}", profileId);
        Profile profile = profileDao.selectById(profileId);

        logger.debug("查询结果： {}", JSON.toJSONString(profile, true));
        return profile;
    }

    @Override
    public int deleteById(Long profileId, Long operatorId) {
        logger.debug("profile 删除， 参数 profileId : {}", profileId);
        Profile profile = new Profile();
        profile.setId(profileId);
        profile.setIsDel(DelFlagEnum.YES.getCode());
        profile.setUpdateTime(new Date());
        profile.setUpdateUserId(operatorId);
        int num = profileDao.updateById(profile);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
