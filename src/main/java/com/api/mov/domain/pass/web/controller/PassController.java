package com.api.mov.domain.pass.web.controller;

import com.api.mov.domain.pass.service.PassService;
import com.api.mov.domain.pass.web.dto.PassCreateReq;
import com.api.mov.domain.pass.web.dto.PassRes;
import com.api.mov.global.jwt.UserPrincipal;
import com.api.mov.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PassController {

    private final PassService passService;

    @PostMapping("/pass")
    public ResponseEntity<SuccessResponse<?>> createPass(@RequestBody PassCreateReq passCreateReq,
                                                         @AuthenticationPrincipal UserPrincipal userPrincipal) {
        passService.createPass(passCreateReq, userPrincipal.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.success("패키지 생성을 성공했습니다."));
    }
    // 패키지 상세조회 api
    @GetMapping("/pass/{passId}")
    public ResponseEntity<SuccessResponse<?>> getPassDetail(@PathVariable Long passId) {
        PassRes passDetail = passService.getPassDetail(passId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(passDetail));
    }
}
