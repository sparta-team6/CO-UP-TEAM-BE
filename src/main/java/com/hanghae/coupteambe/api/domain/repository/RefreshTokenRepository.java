package com.hanghae.coupteambe.api.domain.repository;

import com.hanghae.coupteambe.api.domain.entity.security.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByLoginId(String loginId);

    void deleteByLoginId(String loginId);
}
