package com.jichuangsi.school.user.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.entity.Roleurl;
import com.jichuangsi.school.user.entity.SchoolRole;
import com.jichuangsi.school.user.entity.UrlRelation;
import com.jichuangsi.school.user.repository.IRoleUrlRepository;
import com.jichuangsi.school.user.repository.ISchoolRoleRepository;
import com.jichuangsi.school.user.repository.IUrlRelationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IBackRoleUrlService {

    @Resource
    private IRoleUrlRepository iRoleUrlRepository;
    @Resource
    private IUrlRelationRepository iUrlRelationRepository;
    @Resource
    private ISchoolRoleRepository iSchoolRoleRepository;
    //新增url
    @Transactional(rollbackFor = Exception.class)
    public void insertRoleUrl(UserInfoForToken userInfoForToken, Roleurl roleurl){
        iRoleUrlRepository.save(roleurl);
    }
    //新增和修改url
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleUr(UserInfoForToken userInfoForToken, Roleurl roleurl){
        iRoleUrlRepository.saveAndFlush(roleurl);
    }
    //根据id获得url
    public Roleurl getUrlByUrlId(UserInfoForToken userInfoForToken, String urlId){
        return iRoleUrlRepository.getOne(urlId);
    }
    //根据id删除url
    @Transactional(rollbackFor = Exception.class)
    public void deleteUrl(UserInfoForToken userInfoForToken, String urlId){
        iUrlRelationRepository.deleteByUid(urlId);
        iRoleUrlRepository.deleteById(urlId);
    }
    //查询全部权限
    public List<Roleurl> getAllRoleUrl(UserInfoForToken userInfoForToken){
        return iRoleUrlRepository.findAll();
    }

    //分页查询全部权限
    public Page<Roleurl> getAllRoleUrlByPage(UserInfoForToken userInfoForToken, int pageNum, int pageSize){
        pageNum=pageNum-1;
        PageRequest pageRequest=new PageRequest(pageNum,pageSize);
        Page<Roleurl> page=iRoleUrlRepository.findAll(pageRequest);
        //List<Roleurl> roleurls=page.getContent();
        return page;
    }

    //添加角色对应的url
    @Transactional(rollbackFor = Exception.class)
    public void insertUrlRelation(UserInfoForToken userInfoForToken,UrlRelation urlRelation){
        iUrlRelationRepository.save(urlRelation);
    }
    //批量添加角色对应的url
    public void batchInsertUrlRelation(UserInfoForToken userInfoForToken,List<UrlRelation> urlRelation){
        iUrlRelationRepository.saveAll(urlRelation);

    }
    //根据id删除用户相关url
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleUrl(UserInfoForToken userInfoForToken, String urlRelationId){
        iUrlRelationRepository.deleteById(urlRelationId);
    }

    //根据id批量删除用户相关url
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteRoleUrl(UserInfoForToken userInfoForToken,List<UrlRelation>  urlRelationId){
        iUrlRelationRepository.deleteInBatch(urlRelationId);
    }
    //查询角色信息
    public List<SchoolRole> getAllRole(){
        return iSchoolRoleRepository.findAll();
    }
}
