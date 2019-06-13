package com.jichuangsi.school.user.service;

import com.jichuangsi.school.user.entity.AdminBanUrl;
import com.jichuangsi.school.user.entity.SchoolRole;
import com.jichuangsi.school.user.model.UrlMapping;
import com.jichuangsi.school.user.model.backmodel.RoleUrlModel;
import com.jichuangsi.school.user.entity.FreeUrl;

import java.util.List;


public interface ISchoolRoleService {

        List<UrlMapping> getAllRole();
        List<RoleUrlModel> getUrlByRoleId(String roleId);
        List<String> getUrlByBackRoleId(String roleId);
        List<FreeUrl> getFreeUrl();
        List<AdminBanUrl> getAdminBanUrl();
}
