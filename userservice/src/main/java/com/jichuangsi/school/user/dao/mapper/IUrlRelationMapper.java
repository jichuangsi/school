package com.jichuangsi.school.user.dao.mapper;

import com.jichuangsi.school.user.model.UrlMapping;
import com.jichuangsi.school.user.model.backmodel.RoleUrlModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
@Mapper
public interface IUrlRelationMapper {
    //根据用户id查询权限
    @Select("<script>select u.rid as roleName,u.uid as urlList from urlrelation u </script>")
    List<UrlMapping> selectAllRole();
    //根据角色id获得角色url相关信息
    @Select("<script>select u.id as id,u.rid as roleId,r.id as urlId,r.`name` as name,r.url as url,r.usewayid as usewayid from urlrelation u INNER JOIN roleurl r on u.uid=r.id where u.rid=#{roleId}</script>")
    List<RoleUrlModel> selectUrlByRoleId(@Param("roleId") String roleId);
    //根据角色id获得角色url
    @Select("<script>select r.url as url from urlrelation u INNER JOIN roleurl r on u.uid=r.id where u.rid=#{roleId}</script>")
    List<String> selectUelByRoleId(@Param("roleId")String roleId);

    @Update("<script>UPDATE roleurl set usewayid=#{newUseWay} WHERE usewayid=#{oldUseway}</script>")
    void updateByUseId(@Param("oldUseway")String oldUseway,@Param("newUseWay")String newUseWay);
}
