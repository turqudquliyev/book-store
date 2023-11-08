package az.ingress.service.abstraction;

import az.ingress.dao.entity.AuthorEntity;
import az.ingress.model.request.CreateUserRequest;

public interface AuthorService {
    void createAuthor(CreateUserRequest request);

    AuthorEntity getAuthorById(Long id);

    void subscribeAuthor(Long id, Long studentId);
}