package az.ingress.dao.repository;

import az.ingress.dao.entity.BookEntity;
import az.ingress.dao.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    Page<StudentEntity> findAllByBooks(Pageable pageable, BookEntity book);
}