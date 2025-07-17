package com.tea.server.entity.vo;

import com.tea.server.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersCate {
    private Long id;
    private String name;
    private List<Orders> allList;
}
