package com.tripwhiz.tripwhizuserback.luggage.service;

import com.tripwhiz.tripwhizuserback.fcm.dto.FCMRequestDTO;
import com.tripwhiz.tripwhizuserback.fcm.service.FCMService;
import com.tripwhiz.tripwhizuserback.luggage.dto.LuggageStorageDTO;
import com.tripwhiz.tripwhizuserback.luggage.entity.LuggageStorage;
import com.tripwhiz.tripwhizuserback.luggage.repository.LuggageStorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class LuggageStorageService {

    private final LuggageStorageRepository luggageStorageRepository;
    private final RestTemplate restTemplate;
    private final FCMService fcmService;

    @Value("${server.store.owner.base.url}")
    private String storeOwnerBaseUrl;

    public LuggageStorageDTO createLuggageStorage(LuggageStorage luggageStorage) {
        // DB 저장
        LuggageStorage savedLuggage = luggageStorageRepository.save(luggageStorage);

        // 어드민 서버로 데이터 전송
        sendLuggageToAdmin(savedLuggage);

        // FCM 알림 전송
        sendNotificationToStoreOwner(savedLuggage);

        // 저장된 데이터를 DTO로 변환하여 반환
        return LuggageStorageDTO.builder()
                .lsno(savedLuggage.getLsno())
                .email(savedLuggage.getEmail())
                .storageDate(savedLuggage.getStorageDate())
                .status(savedLuggage.getStatus())
                .storageSpot(savedLuggage.getStorageSpot())
                .build();
    }

    private void sendLuggageToAdmin(LuggageStorage luggageStorage) {
        String url = storeOwnerBaseUrl + "/api/storeowner/luggagestorage/create";
        try {
            restTemplate.postForObject(url, luggageStorage, Void.class);
            log.info("어드민 서버에 수화물 데이터 전송 완료: {}", luggageStorage.getLsno());
        } catch (Exception e) {
            log.error("어드민 서버 전송 실패: {}", e.getMessage());
        }
    }

    private void sendNotificationToStoreOwner(LuggageStorage luggageStorage) {
        String title = "새로운 수화물 보관 요청";
        String body = "보관 장소: " + luggageStorage.getStorageSpot().getSpotname();

        FCMRequestDTO request = FCMRequestDTO.builder()
                .token("STORE_OWNER_FCM_TOKEN") // 점주 FCM 토큰
                .title(title)
                .body(body)
                .build();

        try {
            fcmService.sendMessage(request);
            log.info("점주에게 알림 전송 성공");
        } catch (Exception e) {
            log.error("점주 알림 전송 실패: {}", e.getMessage());
        }
    }
}

