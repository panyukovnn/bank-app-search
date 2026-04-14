package ru.panyukovnn.bankappsearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.panyukovnn.bankappsearch.entity.LatestResultEntity;

import java.util.List;
import java.util.UUID;

public interface LatestResultRepository extends JpaRepository<LatestResultEntity, UUID> {

    List<LatestResultEntity> findByClientIdOrderByCreateTimeDesc(String clientId);

    @Modifying
    @Query(value = """
            DELETE FROM latest_result lre1
            WHERE lre1.id IN (
                SELECT
                    lre2.id
                FROM latest_result lre2
                WHERE lre2.client_id = :clientId
                ORDER BY lre2.create_time DESC
                OFFSET :offset
            )
            """,
            nativeQuery = true)
    int deleteOldestByClientId(@Param("clientId") String clientId, @Param("offset") Integer offset);
}
