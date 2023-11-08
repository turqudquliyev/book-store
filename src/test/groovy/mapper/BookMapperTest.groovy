package mapper

import az.ingress.dao.entity.BookEntity
import az.ingress.model.request.BookRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.mapper.BookMapper.BOOK_MAPPER
import static az.ingress.model.enums.BookStatus.CREATED

class BookMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "TestMapRequestToEntity"() {
        given:
        def bookRequest = random.nextObject(BookRequest)

        when:
        def bookEntity = BOOK_MAPPER.mapRequestToEntity(bookRequest)

        then:
        bookRequest.name == bookEntity.name
        bookRequest.isbn == bookEntity.isbn
        CREATED == bookEntity.status
    }

    def "TestMapEntityToResponse"() {
        given:
        def bookEntity = random.nextObject(BookEntity)

        when:
        def bookResponse = BOOK_MAPPER.mapEntityToResponse(bookEntity)

        then:
        bookEntity.id == bookResponse.id
        bookEntity.name == bookResponse.name
        bookEntity.isbn == bookResponse.isbn
    }
}