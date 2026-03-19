package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishFlavorMapper {
    //批量插入
    void insertBatch(List<DishFlavor> flavors);

    //根据菜品id删除口味数据
    @Delete("delete from dish_flavor where dish_id=#{dishId}")
    void deleteByDishId(Long DishId);

    //根据菜品id集合删除口味数据
    void deleteByDishIds(List<Long> DishIds);

    //根据菜品id查询口味数据
    @Select("select * from dish_flavor where dish_id=#{dishId} ")
    List<DishFlavor> getByDishId(Long id);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
