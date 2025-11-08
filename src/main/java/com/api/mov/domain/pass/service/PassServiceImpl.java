package com.api.mov.domain.pass.service;

import com.api.mov.domain.facility.entity.Facility;
import com.api.mov.domain.facility.repository.FacilityRepository;
import com.api.mov.domain.pass.entity.Pass;
import com.api.mov.domain.pass.entity.PassItem;
import com.api.mov.domain.pass.entity.UserPass;
import com.api.mov.domain.pass.entity.UserPassStatus;
import com.api.mov.domain.pass.repository.PassRepository;
import com.api.mov.domain.pass.repository.UserPassRepository;
import com.api.mov.domain.pass.web.dto.FacilityInPassRes;
import com.api.mov.domain.pass.web.dto.PassCreateReq;
import com.api.mov.domain.pass.web.dto.PassRes;
import com.api.mov.domain.user.entity.User;
import com.api.mov.domain.user.repository.UserRepository;
import com.api.mov.global.exception.CustomException;
import com.api.mov.global.response.code.report.ReportErrorResponseCode;
import com.api.mov.global.response.code.user.UserErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassServiceImpl implements PassService {
    private final PassRepository passRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;
    private final UserPassRepository userPassRepository;

    @Override
    @Transactional
    public void createPass(PassCreateReq passCreateReq, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));

        Pass pass = Pass.builder()
                .name(passCreateReq.getPassName())
                .price(passCreateReq.getPassPrice())
                .description(passCreateReq.getPassDescription())
                .build();


        List<Facility> facilityList = facilityRepository.findAllById(passCreateReq.getFacilityIdList());
        for (Facility facility : facilityList){
            PassItem passItem = PassItem.builder()
                    .pass(pass)
                    .facility(facility)
                    .build();

            pass.getPassItems().add(passItem);
        }
        passRepository.save(pass);


        UserPass userPass = UserPass.builder()
                .user(user)
                .pass(pass)
                .status(UserPassStatus.IN_CART) // 패키지 생성 후 장바구니에 있는 상태
                .build();

        userPassRepository.save(userPass);

    }

    @Override
    public PassRes getPassDetail(Long passId) {
        Pass pass = passRepository.findById(passId)
                .orElseThrow(() -> new CustomException(ReportErrorResponseCode.NOT_FOUND_PASS));

        List<FacilityInPassRes> facilityDtoList = pass.getPassItems().stream()
                .map(PassItem::getFacility)
                .map(facility -> new FacilityInPassRes(
                        facility.getId(),
                        facility.getName(),
                        facility.getAddress(),
                        facility.getSport().getName() // Sport 엔티티에서 종목명 조회
                ))
                .collect(Collectors.toList());

        return new PassRes(
                pass.getId(),
                pass.getName(),
                pass.getPrice(),
                pass.getDescription(),
                facilityDtoList
        );
    }
}
