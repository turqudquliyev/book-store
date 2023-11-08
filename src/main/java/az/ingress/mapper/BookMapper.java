package az.ingress.mapper;

import az.ingress.dao.entity.BookEntity;
import az.ingress.model.request.BookRequest;
import az.ingress.model.response.BookResponse;

import static az.ingress.model.enums.BookStatus.CREATED;

public enum BookMapper {
    BOOK_MAPPER;

    public BookEntity mapRequestToEntity(BookRequest request) {
        return BookEntity.builder()
                .name(request.getName())
                .isbn(request.getIsbn())
                .status(CREATED)
                .build();
    }

    public BookResponse mapEntityToResponse(BookEntity entity) {
        return BookResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .isbn(entity.getIsbn())
                .build();
    }
}