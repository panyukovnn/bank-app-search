package ru.panyukovnn.bankappsearch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

/**
 * Последний результат поиска пользователя
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LatestResult extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    /**
     * Идентификатор клиента
     */
    private String clientId;
    /**
     * Строка поискового запроса
     */
    private String searchString;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        LatestResult latestResult = (LatestResult) object;

        return Objects.equals(id, latestResult.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}