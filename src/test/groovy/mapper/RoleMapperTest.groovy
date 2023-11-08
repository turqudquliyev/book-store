package mapper

import az.ingress.dao.entity.RoleEntity
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.mapper.RoleMapper.ROLE_MAPPER

class RoleMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "TestBuildRoleEntity"() {
        given:
        def role = random.nextObject(String)

        when:
        def roleEntity = ROLE_MAPPER.buildRoleEntity(role)

        then:
        role == roleEntity.authority
    }

    def "TestMapEntityToResponse"() {
        given:
        def roleEntity = random.nextObject(RoleEntity)

        when:
        def roleResponse = ROLE_MAPPER.mapEntityToResponse(roleEntity)

        then:
        roleEntity.authority == roleResponse.authority
    }
}