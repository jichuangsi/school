package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.SchoolRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISchoolRoleRepository extends JpaRepository<SchoolRole,String> {
}
