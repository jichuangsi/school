package com.jichuangsi.school.user.dao.mapper;

import com.jichuangsi.school.user.entity.Roleurl;
import com.jichuangsi.school.user.model.UrlMapping;
import com.jichuangsi.school.user.model.backmodel.RoleUrlModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IUrlRelationMapper {
    //根据用户id查询权限
    @Select("<script>select s.rname as roleName,r.url as urlList from urlrelation u inner join schoolrole s  on u.rid=s.id INNER JOIN roleurl r on u.uid=r.id </script>")
    List<UrlMapping> selectAllRole();
    //根据角色id获得角色url相关信息
    @Select("<script>select u.id as id,u.rid as roleId,r.id as urlId,r.`name` as name,r.url as url from urlrelation u INNER JOIN roleurl r on u.uid=r.id where u.rid=#{roleId}</script>")
    List<RoleUrlModel> selectUrlByRoleId(@Param("roleId") String roleId);
    //根据角色id获得角色url
    @Select("<script>select r.url as url from urlrelation u INNER JOIN roleurl r on u.uid=r.id where u.rid=#{roleId}</script>")
    List<String> selectUelByRoleId(@Param("roleId")String roleId);
}
