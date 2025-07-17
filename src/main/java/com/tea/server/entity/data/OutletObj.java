package com.tea.server.entity.data;

import com.tea.server.entity.vo.OutletVo;
import lombok.Data;

import java.util.List;

@Data
public class OutletObj {
    private List<OutletVo> outletVoList;
}
