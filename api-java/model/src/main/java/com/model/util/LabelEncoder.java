package com.model.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class LabelEncoder implements Serializable {
    private Map<String, Integer> labelMap = new HashMap<>();

    public void fit(List<String> labels) {
        int idx = 0;
        for (String label : new HashSet<>(labels)) {
            labelMap.put(label, idx++);
        }
    }

    public int encode(String label) {
        return labelMap.getOrDefault(label, 0); // 未知类别返回0
    }

    public String decode(int code) {
        return labelMap.entrySet().stream()
                .filter(e -> e.getValue() == code)
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse("unknown");
    }

    public void setLabelMap(Map<String, Integer> mapping) {
        this.labelMap=mapping;
    }
}