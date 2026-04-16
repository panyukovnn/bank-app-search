package ru.panyukovnn.bankappsearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.panyukovnn.bankappsearch.entity.PageEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PageRepository extends JpaRepository<PageEntity, UUID> {

    List<PageEntity> findByTopResultTrueAndPlatform(String platform);

    @Query(value = """
            WITH page_ranks AS (
                SELECT
                    p.id AS pageId,
                    ts_rank_cd(to_tsvector('russian', p.dictionary), plainto_tsquery('russian', :query)) AS rank
                FROM page p
                WHERE (:version IS NULL OR p.version = :version) AND p.platform = :platform
            )
            SELECT
                id,
                name,
                version,
                platform,
                link,
                dictionary,
                top_result,
                create_time,
                create_user,
                last_update_time,
                last_update_user,
                icon,
                pr.rank
            FROM page p
            JOIN page_ranks pr ON p.id = pr.pageId
            WHERE pr.rank >= :minRank
            ORDER BY pr.rank DESC
            LIMIT :size
            """,
            nativeQuery = true)
    List<PageEntity> findByPlatformAndVersionAndDictionaryPart(@Param("platform") String platform,
                                                               @Param("version") String version,
                                                               @Param("query") String query,
                                                               @Param("minRank") BigDecimal minRank,
                                                               @Param("size") Integer size);
}
