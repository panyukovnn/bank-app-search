package ru.panyukovnn.bankappsearch.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

import static ru.panyukovnn.bankappsearch.util.Constant.DEFAULT_DB_USER;

@Getter
@Setter
@MappedSuperclass
public class AuditableEntity {

    private Instant createTime;
    private String createUser;
    private Instant lastUpdateTime;
    private String lastUpdateUser;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        String login = DEFAULT_DB_USER;

        this.createTime = now;
        this.lastUpdateTime = now;
        this.createUser = login;
        this.lastUpdateUser = login;
    }

    @PreUpdate
    public void preUpdate() {
        String login = DEFAULT_DB_USER;

        this.lastUpdateTime = Instant.now();
        this.lastUpdateUser = login;

        // На случай если id задается вручную и @PrePersist не отработает
        if (this.createTime == null) {
            this.createTime = Instant.now();
        }

        if (this.createUser == null) {
            this.createUser = login;
        }
    }
}