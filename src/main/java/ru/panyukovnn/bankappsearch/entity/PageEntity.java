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

import java.util.Objects;
import java.util.UUID;

/**
 * Страница мобильного приложения для поиска
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "page")
public class PageEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    /**
     * Название страницы
     */
    private String name;
    /**
     * Версия приложения
     */
    private String version;
    /**
     * Платформа (ios, android)
     */
    private String platform;
    /**
     * Ссылка на страницу в приложении
     */
    private String link;
    /**
     * Словарь ключевых слов для поиска
     */
    private String dictionary;
    /**
     * Признак топового результата поиска
     */
    private Boolean topResult;
    /**
     * Иконка страницы
     */
    private String icon;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        PageEntity pageEntity = (PageEntity) object;

        return Objects.equals(id, pageEntity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}