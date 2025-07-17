package com.tea.server.entity.vo;

import com.tea.server.entity.Categorize;
import com.tea.server.entity.Goods;
import com.tea.server.entity.ShopToGoods;
import lombok.Data;

import java.util.List;

@Data
public class CateGoods extends Categorize {
    private List<ShopToGoods> goodsList;
}
