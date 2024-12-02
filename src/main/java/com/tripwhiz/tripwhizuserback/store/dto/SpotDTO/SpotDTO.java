package com.tripwhiz.tripwhizuserback.store.dto.SpotDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpotDTO {
    private Long spno; // 지점 번호
    private String spotname; // 지점 이름
    private String address; // 지점 주소
    private String tel; // 지점 전화번호
    private int sno; // 점주 번호 (nullable로 설정)


}
