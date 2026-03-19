package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
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

    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end){
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        //存放每天订单总数
        List<Integer> orderCountList=new ArrayList<>();
        //存放每天有效订单数
        List<Integer> validOrderCountList=new ArrayList<>();

        //遍历日期列表，获取每天对应的订单数据
        for(LocalDate date:dateList){
            LocalDateTime beginTime =LocalDateTime.of(date,LocalTime.MIN);
            LocalDateTime endTime =LocalDateTime.of(date,LocalTime.MAX);

            //查询每天订单总数
            Integer orderCount=getOrderCount(beginTime,endTime,null);

            //查询每天有效订单数
            Integer validCount=getOrderCount(beginTime,endTime,Orders.COMPLETED);

            orderCountList.add(orderCount);
            validOrderCountList.add(validCount);
        }

        //计算时间内完成订单总数量
        Integer totalOrderCount=orderCountList.stream().reduce(Integer::sum).get();
        //计算时间内完成有效订单数量
        Integer validOrderCount=validOrderCountList.stream().reduce(Integer::sum).get();

        //订单完成率
        Double orderCompletionRate=0.0;
        if(totalOrderCount!=0){
            orderCompletionRate=(double)validOrderCount/totalOrderCount;
        }

        return OrderReportVO.builder()
                .dateList(dateList.stream().map(LocalDate::toString).collect(Collectors.joining(",")))
                .orderCountList(orderCountList.stream().map(Object::toString).collect(Collectors.joining(",")))
                .validOrderCountList(validOrderCountList.stream().map(Object::toString).collect(Collectors.joining(",")))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    private Integer getOrderCount(LocalDateTime begin, LocalDateTime end,Integer status){
        Map map=new HashMap();
        map.put("begin",begin);
        map.put("end",end);
        map.put("status",status);

        return orderMapper.countByMap(map);
    }

    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end){
        LocalDateTime beginTime =LocalDateTime.of(begin,LocalTime.MIN);
        LocalDateTime endTime =LocalDateTime.of(end,LocalTime.MAX);

        List<GoodsSalesDTO> salesTop10=orderMapper.getSalesTop10(beginTime,endTime);
        salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        return SalesTop10ReportVO.builder().build();
    }
}
