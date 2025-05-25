package com.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// PredictResponse.java
@Data
@AllArgsConstructor
public class response {
    private String predictedAction;
    private float[] probabilities;
}