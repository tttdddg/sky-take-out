package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface ReportService {
    //统计指定时间内的营业额
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}
