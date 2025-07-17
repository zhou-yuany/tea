package com.tea.server.entity.data;

import com.tea.server.entity.BatchShopPrice;
import lombok.Data;

import java.util.List;

@Data
public class GoodBatch {
    private List<BatchShopPrice> batchList;
}
