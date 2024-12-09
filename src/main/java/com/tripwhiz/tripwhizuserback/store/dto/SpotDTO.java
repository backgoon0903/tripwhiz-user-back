package com.tripwhiz.tripwhizuserback.store.dto;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SpotDTO {

    private Long spno;
    private String spotname;
    private String address;
    private String url;
//    private String tel;
    private Double latitude; // 위도
    private Double longitude; // 경도
    private Long sno; // 점주 ID
    private String sname; // 점주 이름

}
