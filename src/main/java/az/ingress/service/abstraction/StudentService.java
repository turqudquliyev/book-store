package az.ingress.service.abstraction;

import az.ingress.model.request.CreateUserRequest;
import az.ingress.model.response.PageableStudentResponse;
import org.springframework.data.domain.Pageable;


public interface StudentService {
    PageableStudentResponse getStudentsByBook(Pageable pageable, Long bookId);

    void createStudent(CreateUserRequest request);
}