package com.model.service;


import com.model.controller.modelController;
import com.model.dto.request;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.model.util.LabelEncoder;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class featureProcessor {
    @Lazy
    private final LabelEncoder eventEncoder;
    private final float[] mean;
    private final float[] std;
    private static final Logger log = LoggerFactory.getLogger(modelController.class);


    public featureProcessor(LabelEncoder encoder, Map<String, Object> scalerParams) {
        this.eventEncoder = encoder;
        this.mean = (float[]) scalerParams.get("mean");
        this.std = (float[]) scalerParams.get("std");
    }

    public int[][] processSequence(List<request.Event> events) {
        // 编码事件序列
        int[] encoded = events.stream()
                .map(e -> eventEncoder.encode(e.getType()))
                .mapToInt(i -> i)
                .toArray();

        // 生成滑动窗口 (窗口长度=10)
        int seqLength = 10;
        int[][] sequences = new int[encoded.length - seqLength][seqLength];
        for (int i = 0; i < encoded.length - seqLength; i++) {
            System.arraycopy(encoded, i, sequences[i], 0, seqLength);
        }
        log.info("Generated sequences with dimensions: {} x {}", sequences.length, sequences[0].length);
        return sequences;
    }

    public String getLabel(float[] probabilities) {
        // 校验输入
        if (probabilities == null || probabilities.length != 3) {
            throw new IllegalArgumentException("概率数组必须包含3个元素");
        }

        // 找到最大概率的索引
        int maxIndex = 0;
        for (int i = 1; i < probabilities.length; i++) {
            if (probabilities[i] > probabilities[maxIndex]) {
                maxIndex = i;
            }
        }

        // 解码为标签名
        return eventEncoder.decode(maxIndex);
    }

    public float[][] calculateRiskFeatures(List<request.Event> events) {
        // 生成与序列数量匹配的特征矩阵
        int seqLength = 10;
        int batchSize = events.size() - seqLength;

        float[][] features = new float[batchSize][2];

        // 遍历每个序列窗口
        for (int i = 0; i < batchSize; i++) {
            // 截取当前窗口的事件（假设需要窗口内的事件计算特征）
            List<request.Event> windowEvents = events.subList(i, i + seqLength);

            // 计算当前窗口的特征
            long totalEvents = windowEvents.size();
            long cartAdds = windowEvents.stream().filter(e -> "addtocart".equals(e.getType())).count();
            long purchases = windowEvents.stream().filter(e -> "transaction".equals(e.getType())).count();
            float abandonRate = (cartAdds > 0) ? (cartAdds - purchases) / (float) cartAdds : 0.5f;

            // 标准化
            float[] feature = {
                    (float)totalEvents,
                    abandonRate
            };
            for (int j = 0; j < feature.length; j++) {
                feature[j] = (feature[j] - mean[j]) / std[j];
            }

            features[i] = feature;
        }
        return features;
    }
}
