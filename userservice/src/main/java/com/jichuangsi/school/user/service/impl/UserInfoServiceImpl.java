package com.jichuangsi.school.user.service.impl;

import ch.qos.logback.core.joran.conditional.ElseAction;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.commons.Md5Util;
import com.jichuangsi.school.user.commons.PageResult;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.repository.UserRepository;
import com.jichuangsi.school.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

import com.jichuangsi.school.user.commons.MyResultCode;


@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserRepository userRepository;
    @Resource
    private Algorithm algorithm;
//    @Resource
//    private UserInfoMapper  userInfoMapper;

    /**
     * 根据用户ID获取用户信息
     */
    @Override
    public UserInfo findUserInfo(String userId) throws UserServiceException {
        try {
            //是否输入userId
            if (StringUtils.isEmpty(userId)) {
                throw new UserServiceException(MyResultCode.PARAM_MISS_MSG);
            } else {
                Query query = new Query(Criteria.where("flag").is("0").and("id").is(userId));
                UserInfo one = mongoTemplate.findOne(query, UserInfo.class);
                //检查是否存在
                if (one != null) {
                    return one;
                } else {
                    throw new UserServiceException(MyResultCode.USER_UNEXITS);
                }
            }
        } catch (Exception e) {
            throw new UserServiceException(e.getMessage());
        }
    }
    /**
     * 查询全部用户
     */
    public List<UserInfo> findAllUser() throws UserServiceException {
        try {
            Query query = new Query(new Criteria("flag").is("0"));
            List<UserInfo> userInfos = mongoTemplate.find(query, UserInfo.class);
            if (userInfos == null) {
                throw new UserServiceException(MyResultCode.USER_UNEXITS);
            } else {
                return userInfos;
            }
        } catch (Exception e) {
            throw new UserServiceException(e.getMessage());
        }
    }

    @Override
    public List<UserInfo> findUser(String id) throws UserServiceException {
        List<UserInfo> userInfos = new ArrayList<>();
        if (id == null || id.trim() == "") {
            List<UserInfo> all = userRepository.findAll();
            return all;
        }
        userInfos.add(userRepository.findOneByUserId(id));
        return userInfos;
    }

    /**
     * 根据Id进行删除
     *
     * @param Id
     */
    @Override
    public String delteById(String Id) throws UserServiceException {
        try {
            Query query = new Query(Criteria.where("id").is(Id));
            UserInfo one = mongoTemplate.findOne(query, UserInfo.class);
            if (one == null) {
                throw new UserServiceException (MyResultCode.USER_UNEXITS);
            } else {
                if (one.getFlag().equals("1")) {
                    throw new  UserServiceException(MyResultCode.DELETE_FAILED_FLAG);
                } else {
                    Update update = new Update().set("flag", "1");
                    mongoTemplate.updateFirst(query, update, UserInfo.class);
                    throw new UserServiceException(MyResultCode.DELETE_SUCCESS);
                }
            }
        }catch (Exception e){
            throw new UserServiceException(e.getMessage());
        }

    }
    /**
     * 根据Id进行恢复操作
     *
     * @param Id
     */
    @Override
    public String RestoreById(String Id) throws  UserServiceException{
        try {
            Query query = new Query(Criteria.where("id").is(Id));
            UserInfo one = mongoTemplate.findOne(query, UserInfo.class);
            if (one == null) {
                throw new UserServiceException (MyResultCode.USER_UNEXITS);
            } else {
                if (one.getFlag().equals("0")) {
                    throw new  UserServiceException(MyResultCode.RESTORE_FAILED);
                } else {
                    Update update = new Update().set("flag", "0");
                    mongoTemplate.updateFirst(query, update, UserInfo.class);
                    throw new UserServiceException(MyResultCode.SUCESS_MSG);
                }
            }
        }catch (Exception e){
            throw new UserServiceException(e.getMessage());
        }
    }
    @Override
    public PageResult findByPage(Integer page, Integer rows) {
        try {
            PageInfo<UserInfo> pageInfo = PageHelper.startPage(page, rows)
                    .doSelectPageInfo(new ISelect() {
                        @Override
                        public void doSelect() {
                            Query query = new Query(new Criteria("flag").is("0"));
                            mongoTemplate.find(query, UserInfo.class);
                        }
                    });
            return new PageResult(pageInfo.getTotal(), pageInfo.getList());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /*
     * 根据用户Id批量删除用户
     * */
    public List<String> deleteUserInfo(String[] ids) throws UserServiceException {
        try {
            for (String id : ids) {
                Query query =new Query(Criteria.where("id").is(id));
                UserInfo one = mongoTemplate.findOne(query, UserInfo.class);
                if (one.getFlag().equals("1")) {//输入的Id不符合删除需求
                    throw new UserServiceException("用户" + id + MyResultCode.DELETE_FAILED);
                } else {//更新操作

                    Update update = new Update().set("flag", "1");
                    mongoTemplate.updateFirst(query, update, UserInfo.class);
                    throw new UserServiceException("用户" + id + MyResultCode.DELETE_SUCCESS);
                }
            }
            throw new UserServiceException(MyResultCode.SYS_ERROR_MSG);
        }catch (Exception e){
            throw new UserServiceException(e.getMessage());
        }
    }
    /**
     * 新建用户
     *
     * @param userInfo
     * @return
     */
    public UserInfo saveUserInfo(UserInfo userInfo) throws UserServiceException {
        try {
            if (StringUtils.isEmpty(userInfo.getId()))//新建
            {
                if(userRepository.findOneByUserId(userInfo.getUserId()) != null){
                    throw new UserServiceException(MyResultCode.USER_EXISTED);
                }
                userInfo.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                userInfo.setCreateTime(new Date().getTime());
            }
            else {//修改
                Optional<UserInfo> userInStore = userRepository.findById(userInfo.getUserId());
                System.out.println(userInStore);
                if(!userInStore.isPresent()) throw new UserServiceException(MyResultCode.USER_UNEXITS);
                if(!StringUtils.isEmpty(userInStore.get().getUserId())&&!userInStore.get().getUserId().equalsIgnoreCase(userInfo.getUserId())){
                    if(userRepository.findOneByUserId(userInfo.getUserId()) != null){
                        throw new UserServiceException(MyResultCode.USER_EXISTED);
                    }
                }
            }
            userInfo.setFlag("0");
            String pwd = Md5Util.encodeByMd5(userInfo.getPwd());
            userInfo.setPwd(pwd);
            userInfo.setUpdateTime(new Date().getTime());
            UserInfo save = userRepository.save(userInfo);
            return save;

        }catch (Exception e){
            throw new UserServiceException(e.getMessage());
        }
    }

    /**
     * 批量恢复
     *
     * @param ids
     */
    @Override
    public void RestoreUsers(String[] ids) throws UserServiceException {
        try {
            for (String id : ids) {
                Query query =new Query(Criteria.where("id").is(id));
                UserInfo one = mongoTemplate.findOne(query, UserInfo.class);
                if (one.getFlag().equals("1")) {//输入的Id不符合删除需求
                    throw new UserServiceException("用户" + id + MyResultCode.RESTORE_FAILED);
                } else {//更新操作

                    Update update = new Update().set("flag", "0");
                    mongoTemplate.updateFirst(query, update, UserInfo.class);
                    throw new UserServiceException("用户" + id + MyResultCode.SUCESS_MSG);
                }
            }
            throw new UserServiceException(MyResultCode.SYS_ERROR_MSG);
        }catch (Exception e){
            throw new UserServiceException(e.getMessage());
        }
    }

    /**
     * 编辑用户信息
     *
     * @param userInfo
     * @return
     */
    public UserInfo UpdateUserInfo(UserInfo userInfo) throws UserServiceException {
        try {
            if (userRepository.findOneByUserId(userInfo.getId()) == null) {
                throw new UserServiceException(MyResultCode.UPDATE_FAILED);
            } else {
                userInfo.setUpdateTime(new Date().getTime());
                return userRepository.save(userInfo);
            }
        } catch (Exception e) {
            throw new UserServiceException(e.getMessage());
        }

    }
    public  void test(String id){
        Query query= new Query(Criteria.where("userId").is("0"));
        UserInfo byId = mongoTemplate.findById(id, UserInfo.class);

    }
}