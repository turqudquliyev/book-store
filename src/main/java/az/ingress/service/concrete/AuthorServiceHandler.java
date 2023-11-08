package az.ingress.service.concrete;

import az.ingress.aop.annotation.Log;
import az.ingress.dao.entity.AuthorEntity;
import az.ingress.dao.entity.StudentEntity;
import az.ingress.dao.repository.AuthorRepository;
import az.ingress.exception.NotFoundException;
import az.ingress.model.request.CreateUserRequest;
import az.ingress.service.abstraction.AuthorService;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static az.ingress.mapper.AuthorMapper.AUTHOR_MAPPER;
import static az.ingress.model.constant.ExceptionConstant.AUTHOR_NOT_FOUND;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class AuthorServiceHandler implements AuthorService {
    AuthorRepository authorRepository;
    StudentServiceHandler studentService;

    public AuthorServiceHandler(AuthorRepository authorRepository,
                                @Lazy StudentServiceHandler studentService) {
        this.authorRepository = authorRepository;
        this.studentService = studentService;
    }

    public void createAuthor(CreateUserRequest request) {
        var author = AUTHOR_MAPPER.mapUserRequestToEntity(request);
        authorRepository.save(author);
    }

    public AuthorEntity getAuthorById(Long id) {
        return fetchIfExist(id);
    }

    @Transactional
    public void subscribeAuthor(Long id, Long studentId) {
        var author = fetchIfExist(id);
        var student = studentService.getStudentById(id);
        setEntityRelations(author, student);
    }

    private void setEntityRelations(AuthorEntity author,
                                    StudentEntity student) {
        author.setStudents(Set.of(student));
        student.setAuthors(Set.of(author));
    }

    private AuthorEntity fetchIfExist(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AUTHOR_NOT_FOUND));
    }
}