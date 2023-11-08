package mapper

import az.ingress.dao.entity.StudentEntity
import az.ingress.model.request.CreateUserRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.data.domain.PageImpl
import spock.lang.Specification

import static az.ingress.mapper.StudentMapper.STUDENT_MAPPER

class StudentMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "TestMapUserRequestToEntity"() {
        given:
        def createUserRequest = random.nextObject(CreateUserRequest)

        when:
        def studentEntity = STUDENT_MAPPER.mapUserRequestToEntity(createUserRequest)

        then:
        createUserRequest.firstName == studentEntity.firstName
        createUserRequest.lastName == studentEntity.lastName
        createUserRequest.age == studentEntity.age
    }

    def "TestMapEntityToResponse"() {
        given:
        def studentEntity = random.nextObject(StudentEntity)

        when:
        def studentResponse = STUDENT_MAPPER.mapEntityToResponse(studentEntity)

        then:
        studentEntity.id == studentResponse.id
        studentEntity.firstName == studentResponse.firstName
        studentEntity.lastName == studentResponse.lastName
        studentEntity.age == studentResponse.age
    }

    def "TestBuildPageableStudentResponse"() {
        given:
        def studentEntity = random.nextObject(StudentEntity)
        def pageOfStudents = new PageImpl([studentEntity])

        when:
        def pageableStudentResponse = STUDENT_MAPPER.buildPageableStudentResponse(pageOfStudents)

        then:
        studentEntity.id == pageableStudentResponse.students[0].id
        studentEntity.firstName == pageableStudentResponse.students[0].firstName
        studentEntity.lastName == pageableStudentResponse.students[0].lastName
        studentEntity.age == pageableStudentResponse.students[0].age
        1 == pageableStudentResponse.totalElements
        1 == pageableStudentResponse.totalPages
        !pageableStudentResponse.hasNextPage
    }
}