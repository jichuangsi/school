package com.jichuangsi.school.user.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.dao.mapper.IUrlRelationMapper;
import com.jichuangsi.school.user.entity.Roleurl;
import com.jichuangsi.school.user.entity.SchoolRole;
import com.jichuangsi.school.user.entity.UrlRelation;
import com.jichuangsi.school.user.entity.UseWay;
import com.jichuangsi.school.user.model.RoleUrlUseWayModel;
import com.jichuangsi.school.user.repository.IRoleUrlRepository;
import com.jichuangsi.school.user.repository.ISchoolRoleRepository;
import com.jichuangsi.school.user.repository.IUrlRelationRepository;
import com.jichuangsi.school.user.repository.IUseWayRespository;
import com.jichuangsi.school.user.util.MappingEntity2ModelConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class IBackRoleUrlService {

    @Resource
    private IRoleUrlRepository iRoleUrlRepository;
    @Resource
    private IUrlRelationRepository iUrlRelationRepository;
    @Resource
    private ISchoolRoleRepository iSchoolRoleRepository;
    @Resource
    private IUseWayRespository useWayRespository;
    @Resource
    private IUrlRelationMapper urlRelationMapper;
    //新增url
    @Transactional(rollbackFor = Exception.class)
    public void insertRoleUrl(UserInfoForToken userInfoForToken, RoleUrlUseWayModel model){
        iRoleUrlRepository.save(MappingEntity2ModelConverter.CONVERTERFROMROLEURLMODELTOROLEURL(model));
    }
    //修改url
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleUr(UserInfoForToken userInfoForToken, RoleUrlUseWayModel model){
        iRoleUrlRepository.saveAndFlush(MappingEntity2ModelConverter.CONVERTERFROMROLEURLMODELTOROLEURL(model));
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

    //分页查询多条件全部权限
    public Page<Roleurl> getAllRoleUrlByPageAndType(UserInfoForToken userInfoForToken,int pageNum,int pageSize,String usewayid,String name){
        pageNum=pageNum-1;
        Pageable pageable=new PageRequest(pageNum,pageSize);
        Page<Roleurl> page=iRoleUrlRepository.findAll((Root<Roleurl> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            if(usewayid!=null && usewayid!=""){
                Join<Roleurl,UseWay> useWayJoin=root.join("useWay",JoinType.LEFT);
                predicateList.add(criteriaBuilder.equal(useWayJoin.get("id"), usewayid));
            }
            if(name!=null && name!=""){
                predicateList.add(criteriaBuilder.like(root.get("name"),"%"+name+"%"));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
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

    //查询url父级分类
    public List<UseWay> getAllUseWay(){
        return useWayRespository.findAll();
    }

    //添加修改分类
    public void insertUseWay(UseWay useWay){
        useWayRespository.saveAndFlush(useWay);
    }

    //删除
    public void deleteUseaWay(String id){
        List<UseWay> useWayList=getAllUseWay();
        String wayId="";
        for (UseWay way:useWayList) {
            if("其他功能".equals(way.getName()) || "其他".startsWith(way.getName())){
                wayId=way.getId();
            }
        }
        urlRelationMapper.updateByUseId(id,wayId);
        useWayRespository.deleteById(id);
    }
}
