package com.tripwhiz.tripwhizuserback.store.service;


import com.tripwhiz.tripwhizuserback.store.domain.Spot;
import com.tripwhiz.tripwhizuserback.store.dto.SpotDTO.SpotDTO;
import com.tripwhiz.tripwhizuserback.store.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class SpotService {

    private final SpotRepository spotRepository;


    // 특정 Spot 읽기
    public SpotDTO read(Long spno) {
        Spot spot = spotRepository.findById(spno)
                .orElseThrow(() -> new IllegalArgumentException("Spot with ID " + spno + " not found."));


        return SpotDTO.builder()
                .spno(spot.getSpno())
                .spotname(spot.getSpotname())
                .address(spot.getAddress())
                .tel(spot.getTel())
                .build();
    }

    // 모든 Spot 리스트 반환
    public List<SpotDTO> list() {
        return spotRepository.findAll().stream()
                .map(spot -> SpotDTO.builder()
                        .spno(spot.getSpno())
                        .spotname(spot.getSpotname())
                        .address(spot.getAddress())
                        .tel(spot.getTel())
                        .build())
                .collect(Collectors.toList());
    }

    // 새로운 Spot 추가
    public SpotDTO add(SpotDTO spotDTO) {
        log.info("----- Starting Spot Addition -----");
        log.info("Received SpotDTO: {}", spotDTO);

        // StoreOwner 조회
        log.info("Fetching StoreOwner with sno: {}", spotDTO.getSno());


        // Spot 엔티티 생성
        log.info("Creating Spot entity...");
        Spot spot = Spot.builder()
                .tel(spotDTO.getTel())
                .spotname(spotDTO.getSpotname())
                .address(spotDTO.getAddress())
                .build();
        log.info("Spot entity created: {}", spot);

        // Spot 저장
        log.info("Saving Spot entity to the database...");
        spotRepository.save(spot);
        log.info("Spot entity saved with spno: {}", spot.getSpno());

        // SpotDTO 업데이트 후 반환
        spotDTO.setSpno(spot.getSpno());
        log.info("Returning SpotDTO: {}", spotDTO);
        log.info("----- End of Spot Addition -----");

        return spotDTO;
    }


    public SpotDTO modify(Long spno, SpotDTO modifyDTO) {
        log.info("Modifying Spot with ID: {}", spno);
        log.debug("Received SpotDTO for modification: {}", modifyDTO);

        Spot spot = spotRepository.findById(spno)
                .orElseThrow(() -> {
                    log.error("Spot not found with ID: {}", spno);
                    return new IllegalArgumentException("Spot not found.");
                });

        log.info("Spot found for modification: {}", spot);

        // Update fields
        spot.setSpotname(modifyDTO.getSpotname());
        spot.setAddress(modifyDTO.getAddress());
        spot.setTel(modifyDTO.getTel());

        Spot updatedSpot = spotRepository.save(spot);
        log.info("Spot updated successfully: {}", updatedSpot);

        return SpotDTO.builder()
                .spno(updatedSpot.getSpno())
                .spotname(updatedSpot.getSpotname())
                .address(updatedSpot.getAddress())
                .tel(updatedSpot.getTel())
                .build();
    }


    // Spot 삭제
    public void delete(Long spno) {
        Spot spot = spotRepository.findById(spno)
                .orElseThrow(() -> new IllegalArgumentException("Spot with ID " + spno + " not found."));
        spotRepository.delete(spot);
    }
}
