package com.model.dto;


import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class request {
    private List<request.Event> events;

    @Data
    public static class Event {
        private String type;
        private Instant timestamp;
    }
}