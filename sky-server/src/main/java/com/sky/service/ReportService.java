package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@Service
public interface ReportService {
    //统计指定时间内的营业额
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    //统计指定时间内的用户数据
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    //统计指定时间内的订单数据
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    //统计指定时间内的销量排名
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    //导出营业数据
    void exportBusinessData(HttpServletResponse response);
}
