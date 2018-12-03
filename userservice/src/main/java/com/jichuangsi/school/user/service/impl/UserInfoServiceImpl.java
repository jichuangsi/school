package com.jichuangsi.school.user.service.impl;

import com.auth0.jwt.algorithms.Algorithm;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jichuangsi.school.user.constant.Status;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.commons.Md5Util;
import com.jichuangsi.school.user.commons.PageResult;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.model.System.User;
import com.jichuangsi.school.user.model.transfer.TransferClass;
import com.jichuangsi.school.user.model.transfer.TransferSchool;
import com.jichuangsi.school.user.model.transfer.TransferTeacher;
import com.jichuangsi.school.user.repository.UserRepository;
import com.jichuangsi.school.user.service.UserInfoService;
import com.jichuangsi.school.user.util.MappingEntity2ModelConverter;
import com.jichuangsi.school.user.util.MappingModel2EntityConverter;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

import com.jichuangsi.school.user.constant.MyResultCode;


@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private UserRepository userRepository;
    @Resource
    private Algorithm algorithm;
//    @Resource
//    private UserInfoMapper  userInfoMapper;

    /**
     * 根据用户ID获取用户信息
     */
    @Override
    public User findUserInfo(String userId) throws UserServiceException {
        try {
            //是否输入userId
            if (StringUtils.isEmpty(userId)) {
                throw new UserServiceException(MyResultCode.PARAM_MISS_MSG);
            } else {
                Query query = new Query(Criteria.where("status").is(Status.ACTIVATE.getName()).and("id").is(userId));
                UserInfo one = mongoTemplate.findOne(query, UserInfo.class);
                //检查是否存在
                if (one != null) {
                    return MappingEntity2ModelConverter.ConvertUser(one);
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
    @Override
    public List<User> findAllUser() throws UserServiceException {
        try {
            Query query = new Query(Criteria.where("status").ne(Status.DELETE.getName()));
            List<UserInfo> userInfos = mongoTemplate.find(query, UserInfo.class);
            if (userInfos == null || userInfos.size() == 0) {
                throw new UserServiceException(MyResultCode.USER_UNEXITS);
            } else {
                return convertQuestionList(userInfos);
            }
        } catch (Exception e) {
            throw new UserServiceException(e.getMessage());
        }
    }

    @Override
    public User findUser(String id) throws UserServiceException {
        List<UserInfo> userInfos = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            throw new UserServiceException(MyResultCode.PARAM_MISS_MSG);
        }
        Optional<UserInfo> userInfo = userRepository.findById(id);
        if(!userInfo.isPresent()) throw new UserServiceException(MyResultCode.USER_UNEXITS);
        return MappingEntity2ModelConverter.ConvertUser(userInfo.get());
    }

    /**
     * 根据Id进行删除
     *
     * @param id
     */
    @Override
    public long deleteById(String id) throws UserServiceException {
        UpdateResult result = mongoTemplate.updateMulti(new Query(Criteria.where("id").is(id)), new Update().set("status", Status.DELETE.getName()), UserInfo.class);
        return result.getModifiedCount();
        /*try {
            Query query = new Query(Criteria.where("id").is(Id));
            UserInfo one = mongoTemplate.findOne(query, UserInfo.class);
            if (one == null) {
                throw new UserServiceException (MyResultCode.USER_UNEXITS);
            } else {
                if (Status.DELETE.getName().equals(one.getStatus())) {
                    throw new  UserServiceException(MyResultCode.DELETE_FAILED_FLAG);
                } else {
                    Update update = new Update().set("status", Status.DELETE.getName());
                    mongoTemplate.updateFirst(query, update, UserInfo.class);
                }
            }
        }catch (Exception e){
            throw new UserServiceException(e.getMessage());
        }*/

    }
    /**
     * 根据Id进行恢复操作
     *
     * @param id
     */
    @Override
    public long restoreById(String id) throws  UserServiceException{
        UpdateResult result = mongoTemplate.updateMulti(new Query(Criteria.where("id").is(id)), new Update().set("status", Status.ACTIVATE.getName()), UserInfo.class);
        return result.getModifiedCount();
        /*try {
            Query query = new Query(Criteria.where("id").is(Id));
            UserInfo one = mongoTemplate.findOne(query, UserInfo.class);
            if (one == null) {
                throw new UserServiceException (MyResultCode.USER_UNEXITS);
            } else {
                if (Status.ACTIVATE.getName().equals(one.getStatus())) {
                    throw new  UserServiceException(MyResultCode.RESTORE_FAILED);
                } else {
                    Update update = new Update().set("status", Status.ACTIVATE.getName());
                    mongoTemplate.updateFirst(query, update, UserInfo.class);
                }
            }
        }catch (Exception e){
            throw new UserServiceException(e.getMessage());
        }*/
    }

    @Override
    public PageResult findByPage(Integer page, Integer rows) {
        try {
            PageInfo<UserInfo> pageInfo = PageHelper.startPage(page, rows)
                    .doSelectPageInfo(new ISelect() {
                        @Override
                        public void doSelect() {
                            Query query = new Query(new Criteria("status").is(Status.ACTIVATE.getName()));
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
    public long deleteUserInfo(String[] ids) throws UserServiceException {
        UpdateResult result = mongoTemplate.updateMulti(new Query(Criteria.where("id").in(ids)), new Update().set("status", Status.DELETE.getName()), UserInfo.class);
        return result.getModifiedCount();
        /*List<String> fail = new ArrayList<String>();
        try {
            for (String id : ids) {
                Query query =new Query(Criteria.where("id").is(id));
                UserInfo one = mongoTemplate.findOne(query, UserInfo.class);
                if (Status.DELETE.getName().equals(one.getStatus())) {//输入的Id不符合删除需求
                    //throw new UserServiceException("用户" + id + MyResultCode.DELETE_FAILED);
                    fail.add(one.getId());
                } else {//更新操作
                    Update update = new Update().set("status", Status.ACTIVATE.getName());
                    mongoTemplate.updateFirst(query, update, UserInfo.class);
                }
            }
        }catch (Exception e){
            throw new UserServiceException(e.getMessage());
        }
        return fail;*/
    }
    /**
     * 新建用户
     *
     * @param user
     * @return
     */
    public User saveUserInfo(User user) throws UserServiceException {
        try {
            if (StringUtils.isEmpty(user.getUserId()))//新建
            {
                if(userRepository.findOneByAccount(user.getUserAccount()) != null){
                    throw new UserServiceException(MyResultCode.USER_EXISTED);
                }
            }
            else {//修改
                Optional<UserInfo> userInStore = userRepository.findById(user.getUserId());
                if(!userInStore.isPresent()) throw new UserServiceException(MyResultCode.USER_UNEXITS);
                if(!StringUtils.isEmpty(userInStore.get().getAccount())&&!userInStore.get().getAccount().equalsIgnoreCase(user.getUserAccount())){
                    if(userRepository.findOneByAccount(user.getUserAccount()) != null){
                        throw new UserServiceException(MyResultCode.USER_EXISTED);
                    }
                }
            }
            user.setUserStatus(Status.ACTIVATE);
            user.setUserPwd(Md5Util.encodeByMd5(user.getUserPwd()));
            UserInfo userInfo = userRepository.save(MappingModel2EntityConverter.ConvertUser(user));
            return MappingEntity2ModelConverter.ConvertUser(userInfo);

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
    public long restoreUsers(String[] ids) throws UserServiceException {

        UpdateResult result = mongoTemplate.updateMulti(new Query(Criteria.where("id").in(ids)), new Update().set("status", Status.ACTIVATE.getName()), UserInfo.class);
        return result.getModifiedCount();
        /*List<String> fail = new ArrayList<String>();
        try {
            for (String id : ids) {
                Query query =new Query(Criteria.where("id").is(id));
                UserInfo one = mongoTemplate.findOne(query, UserInfo.class);
                if (Status.ACTIVATE.getName().equals(one.getStatus())) {//输入的Id不符合删除需求
                    //throw new UserServiceException("用户" + id + MyResultCode.RESTORE_FAILED);
                    fail.add(one.getId());
                } else {//更新操作
                    Update update = new Update().set("flag", "0");
                    mongoTemplate.updateFirst(query, update, UserInfo.class);
                }
            }
        }catch (Exception e){
            throw new UserServiceException(e.getMessage());
        }
        return fail;*/
    }

    /**
     * 编辑用户信息
     *
     * @param user
     * @return
     */
    @Override
    public User UpdateUserInfo(User user) throws UserServiceException {
        try {
            if (userRepository.findById(user.getUserId()) == null) {
                throw new UserServiceException(MyResultCode.USER_UNEXITS);
            } else {
                return MappingEntity2ModelConverter.ConvertUser(userRepository.save(MappingModel2EntityConverter.ConvertUser(user)));
            }
        } catch (Exception e) {
            throw new UserServiceException(e.getMessage());
        }
    }

    @Override
    public TransferTeacher getTeacherById(String teacherId) {
        Criteria criteria = Criteria.where("id").is(teacherId);
        Query query = new Query(criteria);
        UserInfo userInfo  = mongoTemplate.findOne(query,UserInfo.class);
        return MappingEntity2ModelConverter.ConvertTransferTeacher(userInfo);
    }

    @Override
    public List<TransferClass> getTeacherClass(String id) {
        Optional<UserInfo> result  = userRepository.findById(id);
        if(!result.isPresent()) return null;
        return MappingEntity2ModelConverter.TransferClassInfoFromTeacher(result.get());
    }

    @Override
    public TransferSchool getSchoolInfoById(String id) {
        Optional<UserInfo> result  = userRepository.findById(id);
        if(!result.isPresent()) return null;
        return MappingEntity2ModelConverter.TransferSchoolInfoFromTeacher(result.get());
    }

    private List<User> convertQuestionList(List<UserInfo> userInfos){
        List<User> users = new ArrayList<User>();
        userInfos.forEach(userInfo -> {
            users.add(MappingEntity2ModelConverter.ConvertUser(userInfo));
        });
        return users;
    }
}