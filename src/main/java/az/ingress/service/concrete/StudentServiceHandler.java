package az.ingress.service.concrete;

import az.ingress.aop.annotation.Log;
import az.ingress.dao.entity.StudentEntity;
import az.ingress.dao.repository.StudentRepository;
import az.ingress.exception.NotFoundException;
import az.ingress.model.request.CreateUserRequest;
import az.ingress.model.response.PageableStudentResponse;
import az.ingress.service.abstraction.StudentService;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static az.ingress.mapper.StudentMapper.STUDENT_MAPPER;
import static az.ingress.model.constant.ExceptionConstant.STUDENT_NOT_FOUND;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class StudentServiceHandler implements StudentService {
    StudentRepository studentRepository;
    BookServiceHandler bookService;

    public StudentServiceHandler(StudentRepository studentRepository,
                                 @Lazy BookServiceHandler bookService) {
        this.studentRepository = studentRepository;
        this.bookService = bookService;
    }

    public PageableStudentResponse getStudentsByBook(Pageable pageable, Long bookId) {
        var book = bookService.fetchIfExist(bookId);
        var students = studentRepository.findAllByBooks(pageable, book);
        return STUDENT_MAPPER.buildPageableStudentResponse(students);
    }

    public void createStudent(CreateUserRequest request) {
        var student = STUDENT_MAPPER.mapUserRequestToEntity(request);
        studentRepository.save(student);
    }

    public StudentEntity getStudentById(Long id) {
        return fetchIfExist(id);
    }

    private StudentEntity fetchIfExist(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(STUDENT_NOT_FOUND));
    }
}