package com.tripwhiz.tripwhizuserback.luggage.service;

import com.tripwhiz.tripwhizuserback.luggage.dto.LuggageDTO;
import com.tripwhiz.tripwhizuserback.luggage.entity.Luggage;
import com.tripwhiz.tripwhizuserback.luggage.repository.LuggageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LuggageService {

    @Autowired
    private LuggageRepository luggageRepository;

    public void saveLuggage(LuggageDTO.Point startPoint, LuggageDTO.Point endPoint) {
        // 출발지 저장
        Luggage startLuggage = Luggage.builder()
                .name("Start Point")
                .lat(startPoint.getLat())
                .lng(startPoint.getLng())
                .build();
        luggageRepository.save(startLuggage);

        // 도착지 저장
        Luggage endLuggage = Luggage.builder()
                .name("End Point")
                .lat(endPoint.getLat())
                .lng(endPoint.getLng())
                .build();
        luggageRepository.save(endLuggage);
    }
}
