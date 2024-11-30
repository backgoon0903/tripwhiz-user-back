package com.tripwhiz.tripwhizuserback.luggage.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Data
public class LuggageDTO {
    private Point startPoint;
    private Point endPoint;

    @Data
    public static class Point {
        private Double lat;
        private Double lng;
    }
}
