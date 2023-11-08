package mapper

import az.ingress.dao.entity.RoleEntity
import az.ingress.dao.entity.UserEntity
import az.ingress.model.request.RegisterationRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.mapper.UserMapper.USER_MAPPER

class UserMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "TestMapRegistrationRequestToEntity"() {
        given:
        def registrationRequest = random.nextObject(RegisterationRequest)
        def roleEntity = random.nextObject(RoleEntity)

        when:
        def userEntity = USER_MAPPER.mapRegistrationRequestToEntity(registrationRequest, roleEntity)

        then:
        registrationRequest.email == userEntity.username
        registrationRequest.password == userEntity.password
        Set.of(roleEntity) == userEntity.authorities
    }

    def "TestMapEntityToResponse"() {
        given:
        def userEntity = random.nextObject(UserEntity)

        when:
        def userResponse = USER_MAPPER.mapEntityToResponse(userEntity)

        then:
        userEntity.id == userResponse.id
        userEntity.username == userResponse.email
    }
}