package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.FreeUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFreeUrlRespository extends JpaRepository<FreeUrl,String> {
}
