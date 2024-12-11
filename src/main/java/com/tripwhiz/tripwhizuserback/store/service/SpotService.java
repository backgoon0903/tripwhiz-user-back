package com.tripwhiz.tripwhizuserback.store.service;


import com.tripwhiz.tripwhizuserback.manager.entity.StoreOwner;
import com.tripwhiz.tripwhizuserback.manager.repository.StoreOwnerRepository;
import com.tripwhiz.tripwhizuserback.store.domain.Spot;
import com.tripwhiz.tripwhizuserback.store.dto.SpotDTO;
import com.tripwhiz.tripwhizuserback.store.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class SpotService {

    private final SpotRepository spotRepository;
    private final StoreOwnerRepository storeOwnerRepository;
    private final RestTemplate restTemplate;

    @Value("${server.store.owner.base.url}")
    private String adminApiUrl;

    // 점주 서버를 통해 Spot 리스트 조회
    public List<SpotDTO> fetchSpotListFromAdminServer() {
        log.info("Fetching Spot List from Admin Server: {}", adminApiUrl);

        ResponseEntity<List<SpotDTO>> response = restTemplate.exchange(
                adminApiUrl + "/api/admin/spot/user/list", // 점주 서버의 API 경로
                HttpMethod.GET,
                null, // 인증 필요 시 HttpEntity 추가
                new ParameterizedTypeReference<List<SpotDTO>>() {}
        );

        List<SpotDTO> spotList = response.getBody();

        log.info("Spot List: {}", spotList);

        if (spotList != null) {
            log.info("Successfully fetched {} spots from Admin Server", spotList.size());
            // Spot 리스트를 로컬 데이터베이스에 저장
            spotList.forEach(spotDTO -> {

                StoreOwner storeOwner = storeOwnerRepository.findById(spotDTO.getSno())
                        .orElseThrow(() -> new IllegalArgumentException("StoreOwner not found for sno: " + spotDTO.getSno()));

                Spot spot = spotDTO.toEntity(storeOwner);
                if (!spotRepository.existsById(spot.getSpno())) {
                    spotRepository.save(spot);
                    log.info("Saved Spot to local database: {}", spot);
                }
            });
        } else {
            log.warn("No spots retrieved from Admin Server");
        }

        return spotList;
    }


    // 특정 Spot 읽기
    public SpotDTO read(Long spno) {
        Spot spot = spotRepository.findById(spno)
                .orElseThrow(() -> new IllegalArgumentException("Spot with ID " + spno + " not found."));


        return SpotDTO.builder()
                .spno(spot.getSpno())
                .spotname(spot.getSpotname())
                .address(spot.getAddress())
                .url(spot.getUrl())
//                .tel(spot.getTel())
                .build();
    }
}
