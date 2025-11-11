package com.api.mov.domain.pass.service;

import com.api.mov.domain.facility.entity.Facility;
import com.api.mov.domain.facility.repository.FacilityRepository;
import com.api.mov.domain.pass.entity.Pass;
import com.api.mov.domain.pass.entity.PassItem;
import com.api.mov.domain.pass.entity.UserPass;
import com.api.mov.domain.pass.entity.UserPassStatus;
import com.api.mov.domain.pass.repository.PassRepository;
import com.api.mov.domain.pass.repository.UserPassRepository;
import com.api.mov.domain.pass.web.dto.HomePassInfoRes;
import com.api.mov.domain.pass.web.dto.MyPassRes;
import com.api.mov.domain.pass.web.dto.PassCreateReq;
import com.api.mov.domain.pass.web.dto.PassItemInfoRes;
import com.api.mov.domain.user.entity.User;
import com.api.mov.domain.user.repository.UserRepository;
import com.api.mov.global.exception.CustomException;
import com.api.mov.global.response.code.pass.PassErrorResponseCode;
import com.api.mov.global.response.code.user.UserErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

        UserPassStatus status;
        // 무조건 대문자로 인식(프론트에서 cart로 보내오면 인식이 안돼요->방지)
        String storageType = passCreateReq.getStorageType().toUpperCase();

        if ("CART".equals(storageType)) {
            status = UserPassStatus.IN_CART;
        } else if ("LOCKER".equals(storageType)) {
            status = UserPassStatus.IN_LOCKER;
        } else {
            // 잘못된 storageType 값이 들어오면 예외 발생
            throw new CustomException(PassErrorResponseCode.INVALID_PASS_REQUEST_400);
        }
        UserPass userPass = UserPass.builder()
                .user(user)
                .pass(pass)
                .status(status) // 분기 처리된 status로 설정
                .build();

        userPassRepository.save(userPass);

    }

    @Override
    @Transactional(readOnly = true)
    public List<MyPassRes> getMyPassList(Long userId,String status) {

        UserPassStatus enumStatus;
        try {
            // 상태 enum 값을 문자열을 enum으로 변환
            // 대소문자 구분 없이 처리하기 위해 toUpperCase() 사용
            enumStatus = UserPassStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 만약 "invalid_status" 같은 잘못된 값이 들어오면 예외 발생
            throw new CustomException(PassErrorResponseCode.INVALID_PASS_REQUEST_400);
        }


        List<UserPass> userPassList = userPassRepository.findByUserIdAndStatusWithPassDetails(userId,enumStatus);

        return userPassList.stream()
                .map(userPass -> {
                    Pass pass = userPass.getPass();
                    List<PassItemInfoRes> passItemInfoList = pass.getPassItems().stream()
                            .map(passItem -> new PassItemInfoRes(
                                    passItem.getFacility().getId(),
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

    @Override
    @Transactional(readOnly = true)
    public List<HomePassInfoRes> getPasses(String passName, Integer minPrice, Integer maxPrice, String sortBy) {
        Sort sort = createSort(sortBy);

        Specification<Pass> spec = ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        if(StringUtils.hasText(passName)){
            spec = spec.and(hasName(passName));
        }


        if(minPrice != null || maxPrice != null){
            spec = spec.and(isPriceBetween(minPrice, maxPrice));
        }

        List<Pass> passList = passRepository.findAll(spec, sort);

        return passList.stream()
                .map(pass -> new HomePassInfoRes(
                        pass.getId(),
                        pass.getName(),
                        pass.getDescription(),
                        pass.getPrice()
                ))
                .toList();
    }



    private Sort createSort(String sortBy){

        return switch (sortBy.toUpperCase()){
            case "PRICE_HIGH" -> Sort.by(Sort.Direction.DESC, "price");
            case "PRICE_LOW" -> Sort.by(Sort.Direction.ASC, "price");
            case "VIEW_COUNT" -> Sort.by(Sort.Direction.DESC, "viewCount");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
    }

    private Specification<Pass> hasName(String passName){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%"+passName+"%"));
    }


    private Specification<Pass> isPriceBetween(Integer minPrice, Integer maxPrice){
        return (root, query, criteriaBuilder) -> {
            if(minPrice != null && maxPrice != null){
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null){
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else if(maxPrice != null){
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
            return criteriaBuilder.conjunction();
        };
    }
}
