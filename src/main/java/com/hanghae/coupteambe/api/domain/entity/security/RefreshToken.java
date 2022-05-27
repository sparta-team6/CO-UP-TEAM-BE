package com.hanghae.coupteambe.api.domain.entity.security;

import com.hanghae.coupteambe.api.domain.entity.baseentity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken extends BaseTimeEntity {

    @Id
    private String loginId;

    private String refreshToken;

    @Builder
    public RefreshToken(String loginId, String refreshToken) {
        this.loginId = loginId;
        this.refreshToken = refreshToken;
    }

    public RefreshToken updateToken(String refreshToken){
        this.refreshToken = refreshToken;
        return this;
    }
}
