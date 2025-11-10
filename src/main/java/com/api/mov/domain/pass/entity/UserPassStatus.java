package com.api.mov.domain.pass.entity;

public enum UserPassStatus {
    IN_CART,    // 패키지 보관함
    IN_LOCKER,  // 패키지 보관함
    OWNED,      // 진행 중인 패키지
    COMPLETED   // 완료된 패키지
}
