package com.tea.server.entity.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tea.server.entity.jd.SaveSpuRequest;
import com.tea.server.entity.jd.ShopCategoryRequest;
import org.springframework.stereotype.Component;

@Component
public class JsonConverter {
    private final ObjectMapper objectMapper;

    public JsonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String toJson(ShopCategoryRequest request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }
    public String toSaveSpuRequestJson(SaveSpuRequest request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }
}
