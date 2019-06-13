package com.jichuangsi.school.user.service.impl;

import com.jichuangsi.school.user.dao.mapper.IUrlRelationMapper;
import com.jichuangsi.school.user.entity.AdminBanUrl;
import com.jichuangsi.school.user.entity.Roleurl;
import com.jichuangsi.school.user.entity.SchoolRole;
import com.jichuangsi.school.user.model.UrlMapping;
import com.jichuangsi.school.user.model.backmodel.RoleUrlModel;
import com.jichuangsi.school.user.repository.IAdminBanUrlRespository;
import com.jichuangsi.school.user.repository.IUrlRelationRepository;
import com.jichuangsi.school.user.service.ISchoolRoleService;
import com.jichuangsi.school.user.repository.IFreeUrlRespository;
import com.jichuangsi.school.user.entity.FreeUrl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SchoolRoleServiceImpl implements ISchoolRoleService {
    @Resource
    private IUrlRelationMapper urlRelationMapper;
    @Resource
    private IFreeUrlRespository freeUrlRespository;
    @Resource
    private IAdminBanUrlRespository adminBanUrlRespository;

    @Override
    public List<UrlMapping> getAllRole() {
        return urlRelationMapper.selectAllRole();
    }

    @Override
    public List<RoleUrlModel> getUrlByRoleId(String roleId) {
        return urlRelationMapper.selectUrlByRoleId(roleId);
    }

    @Override
    public List<String> getUrlByBackRoleId(String roleId) {
        return urlRelationMapper.selectUelByRoleId(roleId);
    }

    @Override
    public List<FreeUrl> getFreeUrl() {
        return freeUrlRespository.findAll();
    }

    @Override
    public List<AdminBanUrl> getAdminBanUrl() {
        return adminBanUrlRespository.findAll();
    }
}
