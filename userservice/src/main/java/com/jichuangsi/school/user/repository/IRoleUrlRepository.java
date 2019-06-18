package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.Roleurl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IRoleUrlRepository extends JpaRepository<Roleurl,String>,PagingAndSortingRepository<Roleurl,String>,JpaSpecificationExecutor<Roleurl> {

}
