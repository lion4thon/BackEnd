package com.api.mov.domain.pass.service;

import com.api.mov.domain.facility.entity.Facility;
import com.api.mov.domain.facility.repository.FacilityRepository;
import com.api.mov.domain.pass.entity.Pass;
import com.api.mov.domain.pass.entity.PassItem;
import com.api.mov.domain.pass.entity.UserPass;
import com.api.mov.domain.pass.repository.PassRepository;
import com.api.mov.domain.pass.repository.UserPassRepository;
import com.api.mov.domain.pass.web.dto.MyPassRes;
import com.api.mov.domain.pass.web.dto.PassCreateReq;
import com.api.mov.domain.pass.web.dto.PassItemInfoRes;
import com.api.mov.domain.user.entity.User;
import com.api.mov.domain.user.repository.UserRepository;
import com.api.mov.global.exception.CustomException;
import com.api.mov.global.response.code.user.UserErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .build();

        userPassRepository.save(userPass);

    }

    @Override
    @Transactional(readOnly = true)
    public List<MyPassRes> getMyPassList(Long userId) {
        List<UserPass> userPassList = userPassRepository.findAllByUserIdWithDetails(userId);

        return userPassList.stream()
                .map(userPass -> {
                    Pass pass = userPass.getPass();
                    List<PassItemInfoRes> passItemInfoList = pass.getPassItems().stream()
                            .map(passItem -> new PassItemInfoRes(
                                    passItem.getFacility().getName(),
                                    passItem.getFacility().getSport().getName()
                            )).toList();

                    return new MyPassRes(
                            pass.getId(),
                            pass.getName(),
                            pass.getPrice(),
                            pass.getDescription(),
                            passItemInfoList
                    );
                }).toList();
    }
}
