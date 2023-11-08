package mapper


import az.ingress.model.request.CreateUserRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.mapper.AuthorMapper.AUTHOR_MAPPER

class AuthorMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "TestMapUserRequestToEntity"() {
        given:
        def createUserRequest = random.nextObject(CreateUserRequest)

        when:
        def authorEntity = AUTHOR_MAPPER.mapUserRequestToEntity(createUserRequest)

        then:
        createUserRequest.firstName == authorEntity.firstName
        createUserRequest.lastName == authorEntity.lastName
        createUserRequest.age == authorEntity.age
    }
}