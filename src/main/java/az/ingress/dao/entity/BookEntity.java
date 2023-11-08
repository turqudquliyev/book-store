package az.ingress.dao.entity;

import az.ingress.model.enums.BookStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Where(clause = "status <> 'DELETED'")
@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@FieldDefaults(level = PRIVATE)
public class BookEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;
    String name;
    String isbn;
    @Enumerated(STRING)
    BookStatus status;

    @ManyToOne(
            fetch = LAZY,
            cascade = PERSIST
    )
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @JsonBackReference
    @ToString.Exclude
    AuthorEntity author;

    @ManyToMany(
            fetch = LAZY,
            cascade = PERSIST,
            mappedBy = "books"
    )
    @ToString.Exclude
    @JsonBackReference
    Set<StudentEntity> students;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        BookEntity book = (BookEntity) o;
        return getId() != null && Objects.equals(getId(), book.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}