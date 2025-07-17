package com.tea.server.core;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PageBean<T> {

    private Long total;

    private List<T> data;

    public PageBean(IPage<T> page){
        this.total = page.getTotal();
        this.data = page.getRecords();
    }

}
