package az.ingress.mapper;


import az.ingress.model.jwt.AccessTokenClaimsSet;
import az.ingress.model.jwt.AuthPayloadDto;
import az.ingress.model.jwt.RefreshTokenClaimsSet;

import java.util.Date;

import static az.ingress.model.constant.AuthConstant.ISSUER;

public enum TokenMapper {
    TOKEN_MAPPER;

    public AccessTokenClaimsSet buildAccessTokenClaimsSet(AuthPayloadDto dto, Date expirationTime) {
        return AccessTokenClaimsSet.builder()
                .iss(ISSUER)
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .createdTime(new Date())
                .expirationTime(expirationTime)
                .build();
    }

    public RefreshTokenClaimsSet buildRefreshTokenClaimsSet(AuthPayloadDto dto,
                                                            int refreshTokenExpirationCount,
                                                            Date expirationTime) {
        return RefreshTokenClaimsSet.builder()
                .iss(ISSUER)
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .expirationTime(expirationTime)
                .count(refreshTokenExpirationCount)
                .build();
    }
}