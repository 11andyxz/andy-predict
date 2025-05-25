package com.model.controller;


import com.model.dto.request;
import com.model.dto.response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.model.service.featureProcessor;
import com.model.service.onnxpredictor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@RestController
@RequestMapping("/api")
public class modelController {
    private static final Logger log = LoggerFactory.getLogger(modelController.class);
    private final featureProcessor featureProcessor;
    private final onnxpredictor predictor;

    public modelController(@Lazy featureProcessor processor, onnxpredictor predictor) {
        this.featureProcessor = processor;
        this.predictor = predictor;
    }

    @PostMapping("/predict_behavior")
    public response predict(@RequestBody request req) {
        try {
            log.info("Starting prediction process...");

            // 1. 特征处理
            int[][] sequences = featureProcessor.processSequence(req.getEvents());
            log.debug("Processed sequence: {}", Arrays.deepToString(sequences));

            // 2. 修改此处：生成二维风险特征
            float[][] riskFeatures = featureProcessor.calculateRiskFeatures(req.getEvents());
            log.debug("Processed risk features: {}", Arrays.deepToString(riskFeatures));

            // 3. 模型预测（传入正确的二维特征）
            float[][] outputs = predictor.predict(sequences, riskFeatures);
            log.debug("Prediction outputs: {}", Arrays.deepToString(outputs));

            // 4. 取最后一个序列的预测结果（假设输出结构为 [batch_size, 3]）
            float[] probabilities = outputs[outputs.length - 1];

            // 5. 生成响应
            return new response(
                    featureProcessor.getLabel(probabilities),
                    probabilities
            );
        } catch (Exception e) {
            log.error("Unexpected error during prediction: ", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Prediction failed: " + e.getMessage()
            );
        }
    }
}




