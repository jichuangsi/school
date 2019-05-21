package com.jichuangsi.school.user.service;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public interface IStudentInfoService {
    String findStudentClass(String id);
}
