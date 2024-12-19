package com.tripwhiz.tripwhizuserback.luggage.service;

import com.tripwhiz.tripwhizuserback.fcm.dto.FCMRequestDTO;
import com.tripwhiz.tripwhizuserback.fcm.service.FCMService;
import com.tripwhiz.tripwhizuserback.luggage.dto.LuggageStorageDTO;
import com.tripwhiz.tripwhizuserback.luggage.entity.LuggageStorage;
import com.tripwhiz.tripwhizuserback.luggage.repository.LuggageStorageRepository;
import com.tripwhiz.tripwhizuserback.store.domain.Spot;
import com.tripwhiz.tripwhizuserback.store.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class LuggageStorageService {

    private final LuggageStorageRepository luggageStorageRepository;
    private final RestTemplate restTemplate;
    private final FCMService fcmService;
    private final SpotRepository spotRepository;

    @Value("${server.store.owner.base.url}")
    private String storeOwnerBaseUrl;

    private static final String STORE_OWNER_TOKEN = "ejNdaRA-F63OJWcgxg9Kgd:APA91bExX1NC-_ONgAOLw3-nIw7aam6UoOyu3xm5WDroQ4_ixpOshWpTL9OZYXl9RGByU1N5WK5t3L5e40AY5LEhDRxA3Cq4PnDpVc7xntPwzzizbZQp0ic";

    public LuggageStorageDTO createLuggageStorage(LuggageStorage luggageStorage) {
        // 로컬 데이터베이스에서 Spot 확인
        Spot spot = spotRepository.findById(luggageStorage.getStorageSpot().getSpno())
                .orElseThrow(() -> new IllegalArgumentException("Spot not found for ID: " + luggageStorage.getStorageSpot().getSpno()));

        // 수화물 저장
        luggageStorage.setStorageSpot(spot);
        luggageStorageRepository.save(luggageStorage);

        // 어드민 서버로 요청 전송
        sendLuggageToAdmin(luggageStorage);

        // 스토어오너에게 알림 발송
        sendStoreOwnerNotification(luggageStorage);

        return LuggageStorageDTO.builder()
                .storageSpot(luggageStorage.getStorageSpot())
                .email(luggageStorage.getEmail())
                .build();
    }

    private void sendLuggageToAdmin(LuggageStorage luggageStorage) {
        String url = storeOwnerBaseUrl + "/api/storeowner/luggagestorage/create";
        try {
            restTemplate.postForObject(url, luggageStorage, Void.class);
            log.info("LuggageStorage sent to Admin Server: {}", luggageStorage.getLsno());
        } catch (Exception e) {
            log.error("Failed to send LuggageStorage to Admin Server", e);
        }
    }

    private void sendStoreOwnerNotification(LuggageStorage luggageStorage) {
        String title = "새로운 수화물 보관 요청";
        String body = "보관 지점: " + luggageStorage.getStorageSpot().getSpotname();

        FCMRequestDTO request = FCMRequestDTO.builder()
                .token(STORE_OWNER_TOKEN)
                .title(title)
                .body(body)
                .build();

        try {
            fcmService.sendMessage(request);
            log.info("Notification sent to store owner: {}", STORE_OWNER_TOKEN);
        } catch (Exception e) {
            log.error("Failed to send notification to store owner", e);
        }
    }
}
