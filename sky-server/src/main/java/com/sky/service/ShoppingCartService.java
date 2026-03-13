package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShoppingCartService {
    //添加购物车
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    //查询购物车
    List<ShoppingCart> showShoppingCart();

    //清空购物车
    void cleanShoppingCart();
}
