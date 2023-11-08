package az.ingress.controller;

import az.ingress.model.response.PageableStudentResponse;
import az.ingress.service.abstraction.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/v1/students")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class StudentController {
    StudentService studentService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public PageableStudentResponse getAllByBook(Pageable pageable, Long bookId) {
        return studentService.getStudentsByBook(pageable, bookId);
    }
}