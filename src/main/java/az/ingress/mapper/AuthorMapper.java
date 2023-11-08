package az.ingress.mapper;

import az.ingress.dao.entity.AuthorEntity;
import az.ingress.model.request.CreateUserRequest;

public enum AuthorMapper {
    AUTHOR_MAPPER;

    public AuthorEntity mapUserRequestToEntity(CreateUserRequest request) {
        return AuthorEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .age(request.getAge())
                .build();
    }
}