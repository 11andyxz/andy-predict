package com.model.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.util.LabelEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class Modelconfig {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("classpath:processing/scaler_params.json")
    private Resource scalerResource;

    @Value("classpath:processing/event_encoder.json")
    private Resource encoderResource;

    @Bean
    public Map<String, Object> scalerParams() throws IOException {
        // 读取 JSON 文件内容
        Map<String, List<Double>> params = objectMapper.readValue(
                scalerResource.getInputStream(),
                new TypeReference<Map<String, List<Double>>>() {}
        );

        // 提取 mean 和 std 并转为 float[]
        List<Double> meanList = params.get("mean");
        List<Double> stdList = params.get("std");

        if (meanList == null || stdList == null) {
            throw new IllegalArgumentException("Missing 'mean' or 'std' in scaler parameters.");
        }

        float[] mean = new float[meanList.size()];
        for (int i = 0; i < meanList.size(); i++) {
            mean[i] = meanList.get(i).floatValue();
        }
        float[] std = new float[stdList.size()];
        for (int i = 0; i < stdList.size(); i++) {
            std[i] = stdList.get(i).floatValue();  // 手动转 float
        }

        return Map.of(
                "mean", mean,
                "std", std
        );
    }

    @Bean
    public LabelEncoder eventEncoder() throws IOException {
        // 读取 JSON 中的标签映射：例如 {"click":0, "addtocart":1, ...}
        Map<String, Integer> labelMap = objectMapper.readValue(
                encoderResource.getInputStream(),
                new TypeReference<Map<String, Integer>>() {}
        );

        if (labelMap == null || labelMap.isEmpty()) {
            throw new IllegalArgumentException("Event encoder file is empty or invalid.");
        }

        LabelEncoder encoder = new LabelEncoder();
        encoder.setLabelMap(labelMap); // 使用 setter 注入
        return encoder;
    }
}