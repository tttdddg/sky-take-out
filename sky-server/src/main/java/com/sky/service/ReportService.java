package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface ReportService {
    //统计指定时间内的营业额
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    //统计指定时间内的用户数据
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);
}
