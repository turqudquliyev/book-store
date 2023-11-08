package az.ingress.mapper;

import az.ingress.dao.entity.StudentEntity;
import az.ingress.model.request.CreateUserRequest;
import az.ingress.model.response.PageableStudentResponse;
import az.ingress.model.response.StudentResponse;
import org.springframework.data.domain.Page;

public enum StudentMapper {
    STUDENT_MAPPER;

    public StudentEntity mapUserRequestToEntity(CreateUserRequest request) {
        return StudentEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .age(request.getAge())
                .build();
    }

    public StudentResponse mapEntityToResponse(StudentEntity entity) {
        return StudentResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .age(entity.getAge())
                .build();
    }

    public PageableStudentResponse buildPageableStudentResponse(Page<StudentEntity> students) {
        return PageableStudentResponse.builder()
                .students(students.map(this::mapEntityToResponse).toList())
                .totalElements(students.getTotalElements())
                .hasNextPage(students.hasNext())
                .totalPages(students.getTotalPages())
                .build();
    }
}
