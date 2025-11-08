package com.api.mov.domain.pass.service;

import com.api.mov.domain.facility.entity.Facility;
import com.api.mov.domain.pass.web.dto.PassCreateReq;

import java.util.List;

public interface PassService {
    void createPass(PassCreateReq passCreateReq, Long userId);
}
