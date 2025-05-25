package com.model.service;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;



@Service
public class onnxpredictor {
    private final OrtEnvironment env;
    private OrtSession session;

    public onnxpredictor() throws OrtException {
        this.env = OrtEnvironment.getEnvironment();
        OrtSession.SessionOptions opts = new OrtSession.SessionOptions();
        opts.setOptimizationLevel(OrtSession.SessionOptions.OptLevel.ALL_OPT);
        opts.setInterOpNumThreads(2);


        try (InputStream modelStream = getClass().getResourceAsStream("/model/result_model.onnx")) {
            if (modelStream == null) {
                throw new FileNotFoundException("Model file not found in resources!");
            }

            // 将输入流转为临时文件，供 ONNX Runtime 使用
            File tempFile = File.createTempFile("result_model", ".onnx");
            tempFile.deleteOnExit();  // 自动清理

            Files.copy(modelStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // 加载模型
            this.session = env.createSession(tempFile.getAbsolutePath(), opts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float[][] predict(int[][] sequences, float[][] riskFeatures) throws OrtException {
        // 创建输入Tensor
        OnnxTensor sequenceTensor = OnnxTensor.createTensor(env, sequences);
        OnnxTensor riskTensor = OnnxTensor.createTensor(env, riskFeatures);

        // 执行推理
        Map<String, OnnxTensor> inputs = Map.of(
                "sequence_input", sequenceTensor,
                "risk_input", riskTensor
        );

        try (OrtSession.Result results = session.run(inputs)) {
            return (float[][]) results.get(0).getValue();
        }
    }
}
