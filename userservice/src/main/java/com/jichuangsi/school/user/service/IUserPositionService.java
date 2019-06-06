package com.jichuangsi.school.user.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.entity.UserPosition;
import com.jichuangsi.school.user.model.user.TeacherModel;
import com.jichuangsi.school.user.repository.IUserPositionRepository;
import com.jichuangsi.school.user.util.MappingModel2EntityConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IUserPositionService {
    @Resource
    private IUserPositionRepository iUserPositionRepository;

    //添加和修改
    @Transactional(rollbackFor = Exception.class)
    public void insertUserPosition(UserInfoForToken userInfoForToken,UserPosition userPosition){
        iUserPositionRepository.saveAndFlush(userPosition);
    }
    //添加和修改
    @Transactional(rollbackFor = Exception.class)
    public void insertUserPosition(UserInfoForToken userInfoForToken, TeacherModel model){
        UserPosition userPosition=MappingModel2EntityConverter.CONVERTEERFROMTEACHERMODELTOUSERPOSITION(model);
        iUserPositionRepository.saveAndFlush(userPosition);
    }
    //查找全部
    public List<UserPosition> getAllUserPosition(UserInfoForToken userInfoForToken){
        //return iUserPositionRepository.findAll();
        return iUserPositionRepository.findByStatus("1");
    }

    //删除记录
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserPosition(UserInfoForToken userInfoForToken,String userId){
        iUserPositionRepository.deleteByuserid(userId);
    }

    //根据老师id修改老师状态
    @Transactional(rollbackFor = Exception.class)
    public void updateUserPosition(UserInfoForToken userInfoForToken,String userId){
        UserPosition userPosition=iUserPositionRepository.findByuserid(userId);
        userPosition.setStatus("0");
        iUserPositionRepository.saveAndFlush(userPosition);
    }
}
