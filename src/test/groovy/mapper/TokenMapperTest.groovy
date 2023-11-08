package mapper


import az.ingress.model.jwt.AuthPayloadDto
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.mapper.TokenMapper.TOKEN_MAPPER

class TokenMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "TestBuildAccessTokenClaimsSet"() {
        given:
        def authPayloadDto = random.nextObject(AuthPayloadDto)
        def expirationDate = random.nextObject(Date)

        when:
        def accessTokenClaimSet = TOKEN_MAPPER.buildAccessTokenClaimsSet(authPayloadDto, expirationDate)

        then:
        authPayloadDto.userId == accessTokenClaimSet.userId
        authPayloadDto.username == accessTokenClaimSet.username
        expirationDate == accessTokenClaimSet.expirationTime
    }

    def "TestBuildRefreshTokenClaimsSet"() {
        given:
        def authPayloadDto = random.nextObject(AuthPayloadDto)
        def refreshTokenExpirationCount = random.nextInt()
        def expirationTime = random.nextObject(Date)

        when:
        def refreshTokenClaimsSet = TOKEN_MAPPER.buildRefreshTokenClaimsSet(authPayloadDto, refreshTokenExpirationCount, expirationTime)

        then:
        authPayloadDto.userId == refreshTokenClaimsSet.userId
        authPayloadDto.username == refreshTokenClaimsSet.username
        expirationTime == refreshTokenClaimsSet.expirationTime
        refreshTokenExpirationCount == refreshTokenClaimsSet.count
    }
}