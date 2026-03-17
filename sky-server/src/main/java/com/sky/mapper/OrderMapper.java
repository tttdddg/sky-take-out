package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    //插入数据
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    //查询超时订单
    @Select("select * from orders where status=#{status} and order_time < (当前时间 -15分钟)")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    //根据id查询订单
    @Select("select * from orders where id=#{id}")
    Orders getById(long id);
}
