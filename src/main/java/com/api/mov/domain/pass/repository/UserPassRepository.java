package com.api.mov.domain.pass.repository;

import com.api.mov.domain.pass.entity.UserPass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPassRepository extends JpaRepository<UserPass, Long> {
}
