package com.tea.server.entity.vo;

import com.tea.server.entity.ShopToGoods;
import lombok.Data;

import java.util.List;

@Data
public class CateAndGoods {
    private Long id;
    private String name;
    private List<ShopToGoods> children;
}
