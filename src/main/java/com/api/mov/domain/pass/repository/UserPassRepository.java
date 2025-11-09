package com.api.mov.domain.pass.repository;

import com.api.mov.domain.pass.entity.UserPass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPassRepository extends JpaRepository<UserPass, Long> {

    @Query("SELECT up FROM UserPass up " +
    "JOIN FETCH up.pass p " +
    "JOIN FETCH  p.passItems pi " +
    "JOIN FETCH  pi.facility f " +
    "join FETCH  f.sport " +
    "WHERE up.user.id = :userId")
    List<UserPass> findAllByUserIdWithDetails(@Param("userId") Long userId);

    @Query("SELECT up FROM UserPass up JOIN FETCH up.pass WHERE  up.user.id = :userId and up.pass.id IN :passIds")
    List<UserPass> findAllByUserIdAndPassIdInWithPass(@Param("userId")Long userId, @Param("passIds") List<Long> passIds);
}
