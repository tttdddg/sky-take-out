package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.vo.UserReportVO;
import org.springframework.util.StringUtils;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService{
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end){
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        List<Double> turnoverList=new ArrayList<>();
        for(LocalDate date:dateList){
            //查询date日期对应的营业额，营业额指订单状态为“已完成”的订单金额合计
            LocalDateTime beginTime=LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime=LocalDateTime.of(date, LocalTime.MAX);

            Map map=new HashMap();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover=orderMapper.sumByMap(map);
            turnover=turnover==null?0.0:turnover;//避免null
            turnoverList.add(turnover);
        }

        return TurnoverReportVO
                .builder()
                .dateList(dateList.stream().map(LocalDate::toString).collect(Collectors.joining(",")))
                .turnoverList(turnoverList.stream().map(Object::toString).collect(Collectors.joining(",")))
                .build();
    }

    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end){
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        //存放每天新增用户数
        List<Integer> newUserList=new ArrayList<>();
        //存放每天总用户数
        List<Integer> totalUserList=new ArrayList<>();

        for(LocalDate date:dateList){
            LocalDateTime beginTime =LocalDateTime.of(date,LocalTime.MIN);
            LocalDateTime endTime =LocalDateTime.of(date,LocalTime.MAX);

            Map map=new HashMap();
            map.put("end",endTime);
            Integer totalUser=userMapper.countByMap(map);

            map.put("begin",beginTime);
            Integer newUser=userMapper.countByMap(map);

            totalUserList.add(totalUser);
            newUserList.add(newUser);
        }
        return UserReportVO.builder()
                .dateList(dateList.stream().map(LocalDate::toString).collect(Collectors.joining(",")))
                .totalUserList(totalUserList.stream().map(Object::toString).collect(Collectors.joining(",")))
                .newUserList(newUserList.stream().map(Object::toString).collect(Collectors.joining(",")))
                .build();
    }
}
