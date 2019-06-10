package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.UserPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserPositionRepository extends JpaRepository<UserPosition,String> {
    void deleteByuserid(String uid);
    List<UserPosition> findByStatus(String status);
    UserPosition findByuserid(String uid);
}
