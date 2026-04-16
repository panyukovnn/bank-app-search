package ru.panyukovnn.bankappsearch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@ToString
@Entity
@Table(name = "latest_result")
public class LatestResultEntity extends AuditableEntity {

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

        LatestResultEntity latestResultEntity = (LatestResultEntity) object;

        return Objects.equals(id, latestResultEntity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}