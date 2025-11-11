package com.api.mov.domain.pass.web.controller;

import com.api.mov.domain.pass.service.PassService;
import com.api.mov.domain.pass.web.dto.HomePassInfoRes;
import com.api.mov.domain.pass.web.dto.MyPassRes;
import com.api.mov.domain.pass.web.dto.PassCreateReq;
import com.api.mov.global.jwt.UserPrincipal;
import com.api.mov.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PassController {

    private final PassService passService;
    // 패키지 생성 api
    @PostMapping("/pass")
    public ResponseEntity<SuccessResponse<?>> createPass(@RequestBody PassCreateReq passCreateReq,
                                                         @AuthenticationPrincipal UserPrincipal userPrincipal) {
        passService.createPass(passCreateReq, userPrincipal.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.success("패키지 생성을 성공했습니다."));
    }
    // 패키지 조회 api
    @GetMapping("/my-passes")
    public ResponseEntity<SuccessResponse<?>> getMyPasses(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam("status") String status ) {
        List<MyPassRes> myPassRes = passService.getMyPassList(userPrincipal.getId(),status);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(myPassRes));
    }

    //홈화면 패키지 조회
    @GetMapping("/passes")
    public ResponseEntity<SuccessResponse<?>> getPasses(
            @RequestParam(required = false) String passName,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false, defaultValue = "LATEST") String sortBy
    ){
        List<HomePassInfoRes> homePassInfoResList = passService.getPasses(passName, minPrice, maxPrice, sortBy);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(homePassInfoResList));
    }
}
