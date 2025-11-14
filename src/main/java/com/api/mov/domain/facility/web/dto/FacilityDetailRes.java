package com.api.mov.domain.facility.web.dto;

public record FacilityDetailRes(
        Long id, //업장 ID
        String name, //업장 이름
        String address, //업장 주소
        String detailAddress, //업장 상세주소
        String contact, //업장 연락처
        int price, //업장 서비스 이용 가격
        String features, //업장 특징
        String weekdayHours, //업장 평일 운영시간
        String weekendHours, //업장 주말 운영시간
        String holidayClosedInfo, //업장 휴무 정보
        String accessGuide, //오는길 정보
        String imageUrl //시설 대표 이미지 URL
) {
}
