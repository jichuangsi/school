package com.jichuangsi.school.statistics.service.impl;

import com.jichuangsi.school.statistics.exception.FeignClientException;
import com.jichuangsi.school.statistics.exception.QuestionResultException;
import com.jichuangsi.school.statistics.feign.model.TransferStudent;
import com.jichuangsi.school.statistics.service.IClassStatisticsService;
import com.jichuangsi.school.statistics.service.IFeignService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeignClientServiceImpl implements IFeignService {

    private IClassStatisticsService classStatisticsService;

    @Override
    public List<TransferStudent> getSignStudents(String course, String classId) throws FeignClientException {
        try {
            return classStatisticsService.getSignStudents(course, classId);
        } catch (QuestionResultException e) {
            throw new FeignClientException(e.getMessage());
        }
    }
}
