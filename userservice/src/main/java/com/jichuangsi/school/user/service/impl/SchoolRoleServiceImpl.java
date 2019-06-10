package com.jichuangsi.school.user.service.impl;

import com.jichuangsi.school.user.dao.mapper.IUrlRelationMapper;
import com.jichuangsi.school.user.entity.Roleurl;
import com.jichuangsi.school.user.entity.SchoolRole;
import com.jichuangsi.school.user.model.UrlMapping;
import com.jichuangsi.school.user.model.backmodel.RoleUrlModel;
import com.jichuangsi.school.user.repository.IUrlRelationRepository;
import com.jichuangsi.school.user.service.ISchoolRoleService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SchoolRoleServiceImpl implements ISchoolRoleService {
    @Resource
    private IUrlRelationMapper urlRelationMapper;

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
}
