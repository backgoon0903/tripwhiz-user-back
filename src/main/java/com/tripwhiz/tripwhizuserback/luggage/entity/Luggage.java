package com.tripwhiz.tripwhizuserback.luggage.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "luggage")
public class Luggage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 예: "Start Point" 또는 "End Point"
    private Double lat;  // 위도
    private Double lng;  // 경도
}
