package az.ingress.dao.repository;

import az.ingress.dao.entity.AuthorEntity;
import az.ingress.dao.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BookRepository extends CrudRepository<BookEntity, Long> {
    Optional<BookEntity> findByIdAndAuthor(Long id, AuthorEntity author);
}