package com.jichuangsi.school.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.commons.Md5Util;
import com.jichuangsi.school.user.commons.PageResult;
import com.jichuangsi.school.user.constant.MyResultCode;
import com.jichuangsi.school.user.constant.ResultCode;
import com.jichuangsi.school.user.constant.Status;
import com.jichuangsi.school.user.entity.*;
import com.jichuangsi.school.user.entity.org.ClassInfo;
import com.jichuangsi.school.user.entity.org.GradeInfo;
import com.jichuangsi.school.user.entity.org.PhraseInfo;
import com.jichuangsi.school.user.entity.org.SchoolInfo;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.feign.model.ClassTeacherInfoModel;
import com.jichuangsi.school.user.model.System.User;
import com.jichuangsi.school.user.model.backstage.UpdatePwdModel;
import com.jichuangsi.school.user.model.school.SchoolRoleModel;
import com.jichuangsi.school.user.model.school.UserConditionModel;
import com.jichuangsi.school.user.model.transfer.TransferClass;
import com.jichuangsi.school.user.model.transfer.TransferSchool;
import com.jichuangsi.school.user.model.transfer.TransferStudent;
import com.jichuangsi.school.user.model.transfer.TransferTeacher;
import com.jichuangsi.school.user.model.user.StudentModel;
import com.jichuangsi.school.user.model.user.TeacherModel;
import com.jichuangsi.school.user.repository.*;
import com.jichuangsi.school.user.service.UserInfoService;
import com.jichuangsi.school.user.util.MappingEntity2ModelConverter;
import com.jichuangsi.school.user.util.MappingModel2EntityConverter;
import com.mongodb.client.result.UpdateResult;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private UserRepository userRepository;
    @Resource
    private ISchoolInfoRepository schoolInfoRepository;
    @Resource
    private Algorithm algorithm;
    @Resource
    private IPhraseInfoRepository phraseInfoRepository;
    @Resource
    private IGradeInfoRepository gradeInfoRepository;
    @Resource
    private IClassInfoRepository classInfoRepository;
    @Resource
    private IUserExtraRepository userExtraRepository;
    @Resource
    private ISchoolRoleInfoRepository schoolRoleInfoRepository;
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
        if (!userInfo.isPresent()) throw new UserServiceException(MyResultCode.USER_UNEXITS);
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
    public long restoreById(String id) throws UserServiceException {
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
    public void deleteUserInfo(String[] ids) throws UserServiceException {
        List<UserInfo> userInfos = userRepository.findByIdIn(Arrays.asList(ids));
       /* UpdateResult result = mongoTemplate.updateMulti(new Query(Criteria.where("id").in(ids)), new Update().set("status", Status.DELETE.getName()), UserInfo.class);
        return result.getModifiedCount();*/
       for (UserInfo userInfo : userInfos){
           userInfo.setStatus(Status.DELETE.getName());
       }
       userRepository.saveAll(userInfos);
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
                if (userRepository.findOneByAccount(user.getUserAccount()) != null) {
                    throw new UserServiceException(MyResultCode.USER_EXISTED);
                }
                user.setUserId(UUID.randomUUID().toString());
            }
//            else {//修改
//                Optional<UserInfo> userInStore = userRepository.findById(user.getUserId());
//                if(!userInStore.isPresent()) throw new UserServiceException(MyResultCode.USER_UNEXITS);
//                if(!StringUtils.isEmpty(userInStore.get().getAccount())&&!userInStore.get().getAccount().equalsIgnoreCase(user.getUserAccount())){
//                    if(userRepository.findOneByAccount(user.getUserAccount()) != null){
//                        throw new UserServeException(MyResultCode.USER_EXISTED);
//                    }
//                }

//            }
            user.setUserStatus(Status.ACTIVATE);
            user.setUserPwd(Md5Util.encodeByMd5(user.getUserPwd()));
            UserInfo userInfo = userRepository.save(MappingModel2EntityConverter.ConvertUser(user));
            return MappingEntity2ModelConverter.ConvertUser(userInfo);
        } catch (Exception e) {
            throw new UserServiceException(e.getMessage());
        }

    }

    /**
     * 批量恢复
     *
     * @param ids
     */
    @Override
    public void restoreUsers(String[] ids) throws UserServiceException {
       List<UserInfo> userInfos = userRepository.findByIdIn(Arrays.asList(ids));
       for (UserInfo userInfo : userInfos){
           userInfo.setStatus(Status.ACTIVATE.getName());
       }
       userRepository.saveAll(userInfos);
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
                User user1 = MappingEntity2ModelConverter.ConvertUser(userRepository.save(MappingModel2EntityConverter.ConvertUser(user)));
                return user1;
            }
        } catch (Exception e) {
            throw new UserServiceException(e.getMessage());
        }
    }

    @Override
    public TransferTeacher getTeacherById(String teacherId) {
        Criteria criteria = Criteria.where("id").is(teacherId);
        Query query = new Query(criteria);
        UserInfo userInfo = mongoTemplate.findOne(query, UserInfo.class);
        return MappingEntity2ModelConverter.ConvertTransferTeacher(userInfo);
    }

    @Override
    public List<TransferClass> getTeacherClass(String id) {
        Optional<UserInfo> result = userRepository.findById(id);
        if (!result.isPresent()) return null;
        return MappingEntity2ModelConverter.TransferClassInfoFromTeacher(result.get());
    }

    @Override
    public TransferSchool getSchoolInfoById(String id) {
        UserInfo userInfo = userRepository.findFirstById(id);
        return MappingEntity2ModelConverter.TransferSchoolInfoFromTeacher(userInfo);
    }

    @Override
    public List<User> findAllForDelete() throws UserServiceException {
        try {
            Query query = new Query(Criteria.where("status").is(Status.DELETE.getName()));
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
    public User findDeleteOne(String id) throws UserServiceException {
        try {
            //是否输入userId
            if (StringUtils.isEmpty(id)) {
                throw new UserServiceException(MyResultCode.PARAM_MISS_MSG);
            } else {
                Query query = new Query(Criteria.where("status").ne(Status.ACTIVATE.getName()).and("id").is(id));
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

    @Override
    public void TrulyDeleted(String[] ids) throws UserServiceException {
        List<UserInfo> userInfos = userRepository.findByIdIn(Arrays.asList(ids));
        userRepository.deleteAll(userInfos);
    }

    public List<TransferStudent> getStudentsByClassId(String classId) {
        Criteria criteria = Criteria.where("roleInfos").elemMatch(Criteria.where("roleName").is("Student").and("primaryClass.classId").is(classId)).and("status").is(Status.ACTIVATE.getName());
        Query query = new Query(criteria);
        return convertStudentsList(mongoTemplate.find(query, UserInfo.class));
    }

    private List<User> convertQuestionList(List<UserInfo> userInfos) {
        List<User> users = new ArrayList<User>();
        userInfos.forEach(userInfo -> {
            users.add(MappingEntity2ModelConverter.ConvertUser(userInfo));
        });
        return users;
    }

    private List<TransferStudent> convertStudentsList(List<UserInfo> students) {
        List<TransferStudent> transferStudents = new ArrayList<TransferStudent>();
        students.forEach(s -> {
            transferStudents.add(MappingEntity2ModelConverter.TransferStudent(s));
        });
        return transferStudents;
    }

    @Override
    public void saveTeacher(UserInfoForToken userInfo, TeacherModel model) throws UserServiceException {
        if(StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPwd())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        //try {
            if (userRepository.countByAccount(model.getAccount()) > 0) {
                throw new UserServiceException(ResultCode.USER_IS_EXIST);
            }
            UserInfo info = MappingModel2EntityConverter.CONVERTEERFROMTEACHERMODEL(model);
            info.setStatus(Status.ACTIVATE.getName());
          /*  if (info.getRoleInfos().get(0) instanceof TeacherInfo){
                TeacherInfo teacherInfo = (TeacherInfo) info.getRoleInfos().get(0);
                if (null != teacherInfo.getSecondaryClasses()){
                    for (TeacherInfo.Class cla : teacherInfo.getSecondaryClasses()){
                        ClassInfo classInfo = classInfoRepository.findFirstByIdAndDeleteFlag(cla.getClassId(),"0");
                        if (null == classInfo){
                            throw new UserServiceException(cla.getClassName()+"不存在");
                        }
                        for (SubjectTeacherInfo subjectTeacherInfo : classInfo.getTeacherInfos()){
                            if (subjectTeacherInfo.getSubjectId().equals(teacherInfo.getPrimarySubject().getSubjectId()) || subjectTeacherInfo.getSubjectName().equals(teacherInfo.getPrimarySubject().getSubjectName())){
                                if (!StringUtils.isEmpty(subjectTeacherInfo.getTeacherId())){
                                    throw new UserServiceException(ResultCode.CLASS_SUBJECT_MES);
                                }
                            }
                        }
                    }
                }
                if (null != teacherInfo.getPrimaryClass()){
                    ClassInfo classInfo = classInfoRepository.findFirstByIdAndDeleteFlag(teacherInfo.getPrimaryClass().getClassId(),"0");
                    if (null == classInfo){
                        throw new UserServiceException(teacherInfo.getPrimaryClass().getClassName()+"不存在");
                    }
                    for (SubjectTeacherInfo subjectTeacherInfo : classInfo.getTeacherInfos()){
                        if (subjectTeacherInfo.getSubjectId().equals(teacherInfo.getPrimarySubject().getSubjectId()) || subjectTeacherInfo.getSubjectName().equals(teacherInfo.getPrimarySubject().getSubjectName())){
                            if (!StringUtils.isEmpty(subjectTeacherInfo.getTeacherId())){
                                throw new UserServiceException(ResultCode.CLASS_SUBJECT_MES);
                            }
                        }
                    }
                }
            }*/
            userRepository.save(info);
        /*} catch (Exception e) {
            throw new UserServiceException(ResultCode.);
        }*/
    }

    @Override
    public void saveStudent(UserInfoForToken userInfo, StudentModel model) throws UserServiceException {
        if(StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPwd())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        //try {
            if (userRepository.countByAccount(model.getAccount()) > 0) {
                throw new UserServiceException(ResultCode.USER_IS_EXIST);
            }
            UserInfo info = MappingModel2EntityConverter.CONVERTEERFROMSTUDENTMODEL(model);
            info.setStatus(Status.ACTIVATE.getName());
            userRepository.save(info);
        /*} catch (Exception e) {
            throw new UserServiceException(ResultCode.PARAM_ERR_MSG);
        }*/
    }

    @Override
    public String saveExcelStudents(MultipartFile file, UserInfoForToken userInfo) throws UserServiceException {
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xls")) {
            System.out.println("文件不是.xls类型");
        }
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            List<String> errorRowNum = new ArrayList<String>();
            List<UserInfo> userInfos = new ArrayList<UserInfo>();
            List<String> userAccounts = new ArrayList<String>();
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                HSSFSheet sheet = workbook.getSheetAt(i);
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                for (int j = 1; j < physicalNumberOfRows; j++) {
                    //...
                    HSSFRow row = sheet.getRow(j);
                    UserInfo info;
                    try {
                        info = saveRowStudent(row, userAccounts);
                        userAccounts.add(info.getAccount());
                    } catch (UserServiceException e) {
                        errorRowNum.add((j + 1) + " ");
                        continue;
                    }
                    userInfos.add(info);
                }
            }
            userRepository.saveAll(userInfos);
            return JSONObject.toJSONString(errorRowNum.toArray());
        } catch (IOException e) {
            throw new UserServiceException(ResultCode.EXCEL_IMPORT_MSG);
        }
    }

    private UserInfo saveRowStudent(HSSFRow row, List<String> accounts) throws UserServiceException {
        UserInfo userInfo = new UserInfo();
        StudentInfo studentInfo = new StudentInfo();
        String schoolNme = "";
        String phraseName = "";
        String gradeName = "";
        String className = "";
        String studentName = "";
        String sexStr = "";
        String account = "";
        String pwd = "";
        int physicalNumberOfCells = row.getPhysicalNumberOfCells();
        for (int i = 0; i < physicalNumberOfCells; i++) {
            switch (i) {
                case 0:
                    schoolNme = getCellString(row.getCell(i));
                    break;
                case 1:
                    phraseName = getCellString(row.getCell(i));
                    break;
                case 2:
                    gradeName = getCellString(row.getCell(i));
                    break;
                case 3:
                    className = getCellString(row.getCell(i));
                    break;
                case 4:
                    studentName = getCellString(row.getCell(i));
                    break;
                case 5:
                    sexStr = getCellString(row.getCell(i));
                    break;
                case 6:
                    account = getCellString(row.getCell(i));
                    break;
                case 7:
                    pwd = getCellString(row.getCell(i));
                    break;
                default:
                    break;
            }
        }
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(pwd)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        if (accounts.contains(account)) {
            throw new UserServiceException(ResultCode.USER_IS_EXIST);
        }
        SchoolInfo schoolInfo = schoolInfoRepository.findFirstByDeleteFlagAndNameOrderByCreateTimeDesc("0", schoolNme);
        if (null == schoolInfo) {
            throw new UserServiceException(ResultCode.SELECT_NULL_MSG);
        }
        PhraseInfo phraseInfo = phraseInfoRepository.findFirstByDeleteFlagAndPhraseNameAndIdInOrderByCreatedTimeDesc("0", phraseName, schoolInfo.getPhraseIds());
        if (null == phraseInfo) {
            throw new UserServiceException(ResultCode.SELECT_NULL_MSG);
        }
        GradeInfo gradeInfo = gradeInfoRepository.findFirstByDeleteFlagAndNameAndIdInOrderByCreateTimeDesc("0", gradeName, phraseInfo.getGradeIds());
        if (null == gradeInfo) {
            throw new UserServiceException(ResultCode.SELECT_NULL_MSG);
        }
        ClassInfo classInfo = classInfoRepository.findFirstByDeleteFlagAndNameAndIdInOrderByCreateTimeDesc("0", className, gradeInfo.getClassIds());
        if (null == classInfo) {
            throw new UserServiceException(ResultCode.SELECT_NULL_MSG);
        }
        studentInfo.setPhrase(phraseInfo.getId(), phraseInfo.getPhraseName());
        studentInfo.setPrimaryClass(classInfo.getId(), classInfo.getName());
        studentInfo.setPrimaryGrade(gradeInfo.getId(), gradeInfo.getName());
        studentInfo.setSchool(schoolInfo.getId(), schoolInfo.getName());
        studentInfo.setRoleName("Student");
        List<RoleInfo> studentInfos = new ArrayList<RoleInfo>();
        studentInfos.add(studentInfo);
        userInfo.setRoleInfos(studentInfos);
        userInfo.setStatus(Status.ACTIVATE.getName());
        userInfo.setUpdateTime(new Date().getTime());
        userInfo.setName(studentName);
        userInfo.setCreateTime(new Date().getTime());
        if ("男".equals(sexStr.trim())) {
            userInfo.setSex("M");
        } else {
            userInfo.setSex("F");
        }
        userInfo.setPwd(Md5Util.encodeByMd5(pwd));
        userInfo.setAccount(account);
        if (userRepository.countByAccount(account) > 0) {
            throw new UserServiceException(ResultCode.USER_IS_EXIST);
        }
        return userInfo;
    }

    private String getCellString(HSSFCell cell) throws UserServiceException {
        if (null == cell) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        return cell.getStringCellValue();
    }

    @Override
    public List<TransferTeacher> getTeachersByClassId(String classId) throws UserServiceException {
        if (StringUtils.isEmpty(classId)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        ClassInfo classInfo = classInfoRepository.findFirstByIdAndDeleteFlag(classId,"0");
        if (null == classInfo){
            throw new UserServiceException(ResultCode.CLASS_SELECT_NULL_MSG);
        }
        List<TransferTeacher> transferTeachers = new ArrayList<TransferTeacher>();
        classInfo.getTeacherInfos().forEach(teacherInfo -> {
            TransferTeacher transferTeacher = new TransferTeacher();
            transferTeacher.setSubjectId(teacherInfo.getSubjectId());
            transferTeacher.setSubjectName(teacherInfo.getSubjectName());
            transferTeacher.setTeacherId(teacherInfo.getTeacherId());
            transferTeacher.setTeacherName(teacherInfo.getTeacherName());
            transferTeacher.setPrimaryClassName(classInfo.getName());
            transferTeacher.setPrimaryClassId(classInfo.getId());
            if (StringUtils.isEmpty(teacherInfo.getTeacherId())){
                transferTeacher.setHeadMaster("0");
            }
            transferTeachers.add(transferTeacher);
        });
        for (TransferTeacher teacher : transferTeachers){
            if(!StringUtils.isEmpty(teacher.getTeacherId())) {
                if (teacher.getTeacherId().equals(classInfo.getHeadMasterId())) {
                    teacher.setHeadMaster("1");
                }
            }
        }
        return transferTeachers;
    }

    @Override
    public void coldUserInfo(String userId) throws UserServiceException {
        if (StringUtils.isEmpty(userId)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        UserInfo userInfo = userRepository.findFirstById(userId);
        if(null == userInfo){
            throw new UserServiceException(ResultCode.SELECT_NULL_MSG);
        }
        userInfo.setStatus(Status.DELETE.getName());
        userRepository.save(userInfo);
    }

    @Override
    public void updateTeacher(UserInfoForToken userInfo,TeacherModel model) throws UserServiceException {
        if (StringUtils.isEmpty(model.getId())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        UserInfo teacher = userRepository.findFirstById(model.getId());
        TeacherInfo info = new TeacherInfo();
        if (teacher.getRoleInfos().get(0) instanceof TeacherInfo){
            info = (TeacherInfo) teacher.getRoleInfos().get(0);
        }
        if (null == teacher){
            throw new UserServiceException(ResultCode.SELECT_NULL_MSG);
        }
        UserInfo transferTeacher = MappingModel2EntityConverter.CONVERTEERFROMTEACHERMODEL(model);
        if (transferTeacher.getRoleInfos().get(0) instanceof TeacherInfo){
            TeacherInfo teacherInfo = (TeacherInfo) transferTeacher.getRoleInfos().get(0);
            if (null != teacherInfo.getSecondaryGrades() ){
                info.setSecondaryGrades(teacherInfo.getSecondaryGrades());
            }
            if (null != teacherInfo.getSecondarySubjects()){
                info.setSecondarySubjects(teacherInfo.getSecondarySubjects());
            }
            if (null != teacherInfo.getPrimaryGrade()){
                info.setPrimaryGrade(teacherInfo.getPrimaryGrade());
            }
            if (null != teacherInfo.getPrimarySubject()){
                info.setPrimarySubject(teacherInfo.getPrimarySubject());
            }
            if (null != teacherInfo.getPhrase()){
                info.setPhrase(teacherInfo.getPhrase());
            }
            if (null != teacherInfo.getRoleIds()){
                info.setRoleIds(teacherInfo.getRoleIds());
            }
          /*  if (null != teacherInfo.getSecondaryClasses()){
                for (TeacherInfo.Class cla : teacherInfo.getSecondaryClasses()){
                    ClassInfo classInfo = classInfoRepository.findFirstByIdAndDeleteFlag(cla.getClassId(),"0");
                    if (null == classInfo){
                        throw new UserServiceException(cla.getClassName()+"不存在");
                    }
                    for (SubjectTeacherInfo subjectTeacherInfo : classInfo.getTeacherInfos()){
                        if (subjectTeacherInfo.getSubjectId().equals(info.getPrimarySubject().getSubjectId()) || subjectTeacherInfo.getSubjectName().equals(info.getPrimarySubject().getSubjectName())){
                            if (!StringUtils.isEmpty(subjectTeacherInfo.getTeacherId())){
                                throw new UserServiceException(ResultCode.CLASS_SUBJECT_MES);
                            }
                        }
                    }
                }
                info.setSecondaryClasses(teacherInfo.getSecondaryClasses());
            }
            if (null != teacherInfo.getPrimaryClass()){
                ClassInfo classInfo = classInfoRepository.findFirstByIdAndDeleteFlag(teacherInfo.getPrimaryClass().getClassId(),"0");
                if (null == classInfo){
                    throw new UserServiceException(teacherInfo.getPrimaryClass().getClassName()+"不存在");
                }
                for (SubjectTeacherInfo subjectTeacherInfo : classInfo.getTeacherInfos()){
                    if (subjectTeacherInfo.getSubjectId().equals(info.getPrimarySubject().getSubjectId()) || subjectTeacherInfo.getSubjectName().equals(info.getPrimarySubject().getSubjectName())){
                        if (!StringUtils.isEmpty(subjectTeacherInfo.getTeacherId())){
                            throw new UserServiceException(ResultCode.CLASS_SUBJECT_MES);
                        }
                    }
                }
                info.setPrimaryClass(teacherInfo.getPrimaryClass());
            }*/
        }
        List<RoleInfo> roleInfos = new ArrayList<RoleInfo>();
        roleInfos.add(info);
        teacher.setRoleInfos(roleInfos);
        if (!StringUtils.isEmpty(transferTeacher.getName())){
            teacher.setName(transferTeacher.getName());
        }
        teacher.setUpdateTime(new Date().getTime());
        userRepository.save(teacher);
    }

    @Override
    public void updateStudent(UserInfoForToken userInfo, StudentModel model) throws UserServiceException {
        if (StringUtils.isEmpty(model.getId())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        UserInfo student = userRepository.findFirstById(model.getId());
        StudentInfo info = new StudentInfo();
        if (student.getRoleInfos().get(0) instanceof StudentInfo){
            info = (StudentInfo) student.getRoleInfos().get(0);
        }
        if (null == student){
            throw new UserServiceException(ResultCode.SELECT_NULL_MSG);
        }
        UserInfo transferStudent = MappingModel2EntityConverter.CONVERTEERFROMSTUDENTMODEL(model);
        if (transferStudent.getRoleInfos().get(0) instanceof StudentInfo){
            StudentInfo studentInfo = (StudentInfo) transferStudent.getRoleInfos().get(0);
            if (null != studentInfo.getPrimaryClass()){
                info.setPrimaryClass(studentInfo.getPrimaryClass());
            }
            if (null != studentInfo.getPrimaryGrade()){
                info.setPrimaryGrade(studentInfo.getPrimaryGrade());
            }
            if (null != studentInfo.getPhrase()){
                info.setPhrase(studentInfo.getPhrase());
            }
        }
        List<RoleInfo> roleInfos = new ArrayList<RoleInfo>();
        roleInfos.add(info);
        student.setRoleInfos(roleInfos);
        if (!StringUtils.isEmpty(transferStudent.getName())){
            student.setName(transferStudent.getName());
        }
        student.setUpdateTime(new Date().getTime());
        userRepository.save(student);
    }

    @Override
    public void updateOtherPwd(UserInfoForToken userInfo, UpdatePwdModel model, String userId) throws  UserServiceException{
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(model.getNewPwd())){
            throw new UserServiceException(com.jichuangsi.school.user.constant.ResultCode.PARAM_MISS_MSG);
        }
        UserInfo otherUser = userRepository.findFirstByIdAndStatus(userId,Status.ACTIVATE.getName());
        if(null == otherUser){
            throw new UserServiceException(com.jichuangsi.school.user.constant.ResultCode.USER_ISNOT_EXIST);
        }
        otherUser.setPwd(Md5Util.encodeByMd5(model.getNewPwd()));
        otherUser.setUpdateTime(new Date().getTime());
        userRepository.save(otherUser);
    }

    @Override
    public void insertSchoolRole(UserInfoForToken userInfo, SchoolRoleModel model,String schoolId) throws UserServiceException {
        if (StringUtils.isEmpty(model.getRoleName()) || StringUtils.isEmpty(schoolId)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        if (!(schoolInfoRepository.countByIdAndDeleteFlag(schoolId,"0") > 0)){
            throw new UserServiceException(ResultCode.SCHOOL_SELECT_NULL_MSG);
        }
        if (schoolRoleInfoRepository.countByRoleNameAndDeleteFlagAndSchoolId(model.getRoleName(),"0",schoolId) > 0){
            throw new UserServiceException(ResultCode.ROLE_EXIST_MSG);
        }
        SchoolRoleInfo roleInfo = MappingModel2EntityConverter.CONVERTERFROMSCHOOLROLEMODEL(model);
        roleInfo.setCreatedId(userInfo.getUserId());
        roleInfo.setCreatedName(userInfo.getUserName());
        roleInfo.setUpdatedId(userInfo.getUserId());
        roleInfo.setUpdatedName(userInfo.getUserName());
        roleInfo.setSchoolId(schoolId);
        schoolRoleInfoRepository.save(roleInfo);
    }

    @Override
    public void updateSchoolRole(UserInfoForToken userInfo, SchoolRoleModel model) throws UserServiceException {
        if (StringUtils.isEmpty(model.getRoleName()) || StringUtils.isEmpty(model.getId())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        SchoolRoleInfo roleInfo = schoolRoleInfoRepository.findFirstByIdAndDeleteFlag(model.getId(),"0");
        if (null == roleInfo){
            throw new UserServiceException(ResultCode.SELECT_NULL_MSG);
        }
        roleInfo.setRoleName(model.getRoleName());
        roleInfo.setUpdatedName(userInfo.getUserName());
        roleInfo.setUpdatedId(userInfo.getUserId());
        roleInfo.setUpdatedTime(new Date().getTime());
        schoolRoleInfoRepository.save(roleInfo);
    }

    @Override
    public List<SchoolRoleModel> getSchoolRoles(UserInfoForToken userInfo,String schoolId) throws UserServiceException {
        if (StringUtils.isEmpty(schoolId)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        List<SchoolRoleInfo> schoolRoleInfos = schoolRoleInfoRepository.findByDeleteFlagAndSchoolId("0",schoolId);
        List<SchoolRoleModel> schoolRoleModels = new ArrayList<SchoolRoleModel>();
        schoolRoleInfos.forEach(schoolRoleInfo -> {
            schoolRoleModels.add(MappingEntity2ModelConverter.CONVERTERFROMSCHOOLROLEINFO(schoolRoleInfo));
        });
        return schoolRoleModels;
    }

    @Override
    public void deleteSchoolRole(UserInfoForToken userInfo, String roleId) throws UserServiceException {
        if (StringUtils.isEmpty(roleId)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        SchoolRoleInfo roleInfo = schoolRoleInfoRepository.findFirstByIdAndDeleteFlag(roleId,"0");
        if (null == roleInfo){
            throw new UserServiceException(ResultCode.SELECT_NULL_MSG);
        }
        roleInfo.setDeleteFlag("1");
        roleInfo.setUpdatedTime(new Date().getTime());
        roleInfo.setUpdatedId(userInfo.getUserId());
        roleInfo.setUpdatedName(userInfo.getUserName());
        schoolRoleInfoRepository.save(roleInfo);
    }

    @Override
    public PageInfo<TeacherModel> getTeachers(UserInfoForToken userInfo, String schoolId,int pageIndex,int pageSize) throws UserServiceException {
        if (StringUtils.isEmpty(schoolId)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        List<UserInfo> userInfos = userExtraRepository.findBySchoolId(schoolId,pageIndex,pageSize);
        List<TeacherModel> teacherModels = new ArrayList<TeacherModel>();
        userInfos.forEach(userInfo1 -> {
            teacherModels.add(MappingEntity2ModelConverter.CONVERTERFROMUSERINFO(userInfo1));
        });
        PageInfo<TeacherModel> pageInfo = new PageInfo<>();
        pageInfo.setList(teacherModels);
        pageInfo.setPageNum(pageIndex);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(userExtraRepository.countBySchoolId(schoolId));
        return pageInfo;
    }

    @Override
    public List<ClassTeacherInfoModel> getStudentTeachers(String studentId) throws UserServiceException {
        if (StringUtils.isEmpty(studentId)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        UserInfo student = userRepository.findFirstById(studentId);
        if (null == student){
            throw new UserServiceException(ResultCode.USER_SELECT_NULL_MSG);
        }
        String classId = "";
        if (student.getRoleInfos().get(0) instanceof StudentInfo){
            StudentInfo studentInfo = (StudentInfo) student.getRoleInfos().get(0);
            classId = studentInfo.getPrimaryClass().getClassId();
        }
        List<UserInfo> userInfos = userExtraRepository.findByRoleInfos(classId);
        List<ClassTeacherInfoModel> classTeacherInfoModels = new ArrayList<ClassTeacherInfoModel>();
        for(UserInfo userInfo : userInfos){
            ClassTeacherInfoModel model = new ClassTeacherInfoModel();
            model.setId(userInfo.getId());
            model.setName(userInfo.getName());
            if (userInfo.getRoleInfos().get(0) instanceof TeacherInfo){
                TeacherInfo teacherInfo = (TeacherInfo) userInfo.getRoleInfos().get(0);
                model.setSubject(teacherInfo.getPrimarySubject().getSubjectName());
                if (null != teacherInfo.getPrimaryClass() && classId.equals(teacherInfo.getPrimaryClass().getClassId())){
                    model.setHeadMaster("班主任");
                }
                List<String> roleNames = new ArrayList<String>();
                for (String roleId : teacherInfo.getRoleIds()){
                    SchoolRoleInfo roleInfo = schoolRoleInfoRepository.findFirstByIdAndDeleteFlag(roleId,"0");
                    if (null != roleInfo){
                        roleNames.add(roleInfo.getRoleName());
                    }
                }
                model.setRoleName(roleNames);
            }
            classTeacherInfoModels.add(model);
        }
        return classTeacherInfoModels;
    }

    @Override
    public PageInfo<TeacherModel> getTeachersByCondition(UserInfoForToken userInfo, UserConditionModel model) throws UserServiceException {
        if (StringUtils.isEmpty(model.getSchoolId())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        List<String> classIds = new ArrayList<String>();
        if (!StringUtils.isEmpty(model.getClassId())){
            classIds.add(model.getClassId());
        }else if (!StringUtils.isEmpty(model.getGradeId())){
            GradeInfo gradeInfo = gradeInfoRepository.findFirstById(model.getGradeId());
            if (null == gradeInfo){
                throw new UserServiceException(ResultCode.GRADE_SELECT_NULL_MSG);
            }
            classIds.addAll(gradeInfo.getClassIds());
        }else if (!StringUtils.isEmpty(model.getPhraseId())){
            PhraseInfo phraseInfo = phraseInfoRepository.findFirstById(model.getPhraseId());
            if (null == phraseInfo){
                throw new UserServiceException(ResultCode.PHRASE_SELECT_NULL_MSG);
            }
            List<GradeInfo> gradeInfos = gradeInfoRepository.findByDeleteFlagAndIdInOrderByCreateTime("0",phraseInfo.getGradeIds());
            for (GradeInfo gradeInfo : gradeInfos){
                classIds.addAll(gradeInfo.getClassIds());
            }
        }
        List<UserInfo> userInfos = userExtraRepository.findByConditions(model.getSchoolId(),classIds,model.getUserName(),"Teacher",model.getSubjectId(),model.getPageIndex(),model.getPageSize());
        List<TeacherModel> models = new ArrayList<TeacherModel>();
        userInfos.forEach(userInfo1 -> {
            models.add(getTeacherRoles(MappingEntity2ModelConverter.CONVERTERFROMUSERINFO(userInfo1)));
        });
        PageInfo<TeacherModel> pageInfo = new PageInfo<TeacherModel>();
        pageInfo.setList(models);
        pageInfo.setPageNum(model.getPageIndex());
        pageInfo.setPageSize(model.getPageSize());
        pageInfo.setTotal(userExtraRepository.countByConditions(model.getSchoolId(),classIds,model.getUserName(),"Teacher",model.getSubjectId()));
        return pageInfo;
    }

    @Override
    public PageInfo<StudentModel> getStudentByCondition(UserInfoForToken userInfo, UserConditionModel model) throws UserServiceException {
        if (StringUtils.isEmpty(model.getSchoolId())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        List<UserInfo> userInfos = userExtraRepository.findByCondition(model.getSchoolId(),model.getPhraseId(),model.getGradeId(),model.getClassId(),model.getUserName(),"Student",model.getSubjectId(),model.getPageIndex(),model.getPageSize());
        List<StudentModel> studentModels = new ArrayList<StudentModel>();
        userInfos.forEach(userInfo1 -> {
            studentModels.add(MappingEntity2ModelConverter.CONVERTESTUDENTMODELRFROMUSERINFO(userInfo1));
        });
        PageInfo<StudentModel> pageInfo = new PageInfo<StudentModel>();
        pageInfo.setTotal(userExtraRepository.countByCondition(model.getSchoolId(),model.getPhraseId(),model.getGradeId(),model.getClassId(),model.getUserName(),"Student",model.getSubjectId()));
        pageInfo.setPageSize(model.getPageSize());
        pageInfo.setPageNum(model.getPageIndex());
        pageInfo.setList(studentModels);
        return pageInfo;
    }

    private TeacherModel getTeacherRoles(TeacherModel model){
        List<SchoolRoleInfo> schoolRoleInfos = schoolRoleInfoRepository.findByDeleteFlagAndIdIn("0",model.getRoleIds());
        for (SchoolRoleInfo schoolRoleInfo : schoolRoleInfos){
            model.getRoleInfos().add(new SchoolRoleModel(schoolRoleInfo.getId(),schoolRoleInfo.getRoleName()));
        }
        return model;
    }
}