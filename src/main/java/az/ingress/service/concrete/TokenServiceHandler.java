package az.ingress.service.concrete;

import az.ingress.aop.annotation.Log;
import az.ingress.aop.annotation.LogIgnore;
import az.ingress.exception.AuthenticationException;
import az.ingress.model.jwt.AuthCacheData;
import az.ingress.model.jwt.AuthPayloadDto;
import az.ingress.model.response.LoginResponse;
import az.ingress.service.abstraction.TokenService;
import az.ingress.util.CacheUtil;
import az.ingress.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import static az.ingress.mapper.TokenMapper.TOKEN_MAPPER;
import static az.ingress.model.constant.AuthConstant.*;
import static az.ingress.model.constant.ExceptionConstant.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static jodd.util.Base64.decode;
import static jodd.util.Base64.encodeToString;
import static lombok.AccessLevel.PRIVATE;

@Log
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class TokenServiceHandler implements TokenService {
    final CacheUtil cacheUtil;
    final JwtUtil jwtUtil;
    @Value("${jwt.access-token.expiration.time}")
    int accessTokenExpirationTime;
    @Value("${jwt.refresh-token.expiration.time}")
    int refreshTokenExpirationTime;

    @SneakyThrows
    public LoginResponse generateToken(AuthPayloadDto dto) {
        final int refreshTokenExpirationCount = 50;
        var keyPair = jwtUtil.generateKeyPair();
        var accessTokenClaimsSet = TOKEN_MAPPER.buildAccessTokenClaimsSet(
                dto,
                jwtUtil.generateSessionExpirationTime(accessTokenExpirationTime)
        );
        var refreshTokenClaimsSet = TOKEN_MAPPER.buildRefreshTokenClaimsSet(
                dto,
                refreshTokenExpirationCount,
                jwtUtil.generateSessionExpirationTime(refreshTokenExpirationTime)
        );
        var authCacheData = AuthCacheData.of(
                accessTokenClaimsSet,
                encodeToString(keyPair.getPublic().getEncoded())
        );
        cacheUtil.saveToCache(AUTH_CACHE_DATA_PREFIX + dto.getUserId(), authCacheData, TOKEN_EXPIRE_DAY_COUNT, DAYS);
        var accessToken = jwtUtil.generateToken(accessTokenClaimsSet, keyPair.getPrivate());
        var refreshToken = jwtUtil.generateToken(refreshTokenClaimsSet, keyPair.getPrivate());
        return LoginResponse.of(accessToken, refreshToken);
    }

    @LogIgnore
    public LoginResponse refreshToken(String refreshToken) {
        var refreshTokenClaimsSet = jwtUtil.getClaimsFromRefreshToken(refreshToken);
        var userId = refreshTokenClaimsSet.getUserId();
        var username = refreshTokenClaimsSet.getUsername();
        try {
            AuthCacheData authCacheData = cacheUtil.getBucket(AUTH_CACHE_DATA_PREFIX + userId);
            if (authCacheData == null) throw new AuthenticationException(USER_UNAUTHORIZED, 401);
            var publicKey = KeyFactory.getInstance(RSA).generatePublic(
                    new X509EncodedKeySpec(decode(authCacheData.getPublicKey()))
            );
            jwtUtil.verifyToken(refreshToken, (RSAPublicKey) publicKey);
            if (jwtUtil.isRefreshTokenTimeExpired(refreshTokenClaimsSet)) {
                throw new AuthenticationException(REFRESH_TOKEN_EXPIRED, 401);
            }
            if (jwtUtil.isRefreshTokenCountExpired(refreshTokenClaimsSet)) {
                throw new AuthenticationException(REFRESH_TOKEN_COUNT_EXPIRED, 401);
            }
            return generateToken(AuthPayloadDto.of(userId, username));
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AuthenticationException(USER_UNAUTHORIZED, 401);
        }
    }


    @LogIgnore
    public AuthPayloadDto validateToken(String accessToken) {
        try {
            var userId = jwtUtil.getClaimsFromAccessToken(accessToken).getUserId();
            var username = jwtUtil.getClaimsFromAccessToken(accessToken).getUsername();
            AuthCacheData authCacheData = cacheUtil.getBucket(AUTH_CACHE_DATA_PREFIX + userId);
            if (authCacheData == null) {
                throw new AuthenticationException(TOKEN_EXPIRED, 406);
            }
            var publicKey = KeyFactory.getInstance(RSA).generatePublic(
                    new X509EncodedKeySpec(decode(authCacheData.getPublicKey()))
            );
            jwtUtil.verifyToken(accessToken, (RSAPublicKey) publicKey);
            if (jwtUtil.isTokenExpired(authCacheData.getAccessTokenClaimsSet().getExpirationTime())) {
                throw new AuthenticationException(TOKEN_EXPIRED, 406);
            }
            return AuthPayloadDto.of(userId, username);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error(String.valueOf(ex));
            throw new AuthenticationException(USER_UNAUTHORIZED, 401);
        }
    }
}