package com.jichuangsi.school.statistics.service;

import com.jichuangsi.school.statistics.exception.FeignClientException;
import com.jichuangsi.school.statistics.feign.model.TransferStudent;

import java.util.List;

public interface IFeignService {

    List<TransferStudent> getSignStudents(String course,String classId) throws FeignClientException;
}
