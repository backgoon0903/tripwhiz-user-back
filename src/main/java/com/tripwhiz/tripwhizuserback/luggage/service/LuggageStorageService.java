package com.tripwhiz.tripwhizuserback.luggage.service;

import com.tripwhiz.tripwhizuserback.fcm.dto.FCMRequestDTO;
import com.tripwhiz.tripwhizuserback.fcm.service.FCMService;
import com.tripwhiz.tripwhizuserback.luggage.dto.LuggageStorageDTO;
import com.tripwhiz.tripwhizuserback.luggage.entity.LuggageStorage;
import com.tripwhiz.tripwhizuserback.luggage.repository.LuggageStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
public class LuggageStorageService {

    private final LuggageStorageRepository luggageStorageRepository;
    private final RestTemplate restTemplate;
    private final FCMService fcmService;

    @Value("${server.store.owner.base.url}")
    private String storeOwnerBaseUrl;

    public LuggageStorageDTO createLuggageStorage(LuggageStorage luggageStorage) {
        luggageStorageRepository.save(luggageStorage);

        // 점주로 요청 전송
        String url = storeOwnerBaseUrl + "/api/storeowner/luggagestorage/create";
        restTemplate.postForObject(url, luggageStorage, Void.class);

        // 점주에게 FCM 알림 전송
        sendStoreOwnerNotification(luggageStorage);

        return LuggageStorageDTO.builder()
                .storageSpot(luggageStorage.getStorageSpot())
                .email(luggageStorage.getEmail())
                .build();
    }

    private void sendStoreOwnerNotification(LuggageStorage luggageStorage) {
        String title = "새로운 수화물 보관 신청";
        String body = "보관 지점: " + luggageStorage.getStorageSpot().getSpotname();

        FCMRequestDTO request = FCMRequestDTO.builder()
                .token("STORE_OWNER_FCM_TOKEN") // 실제 점주 토큰으로 대체
                .title(title)
                .body(body)
                .build();

        fcmService.sendMessage(request);
    }
}
