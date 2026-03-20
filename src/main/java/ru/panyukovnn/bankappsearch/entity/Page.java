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
 * Страница мобильного приложения для поиска
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Page extends AuditableEntity {

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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Page page = (Page) object;

        return Objects.equals(id, page.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}