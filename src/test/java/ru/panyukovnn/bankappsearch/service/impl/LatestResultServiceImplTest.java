package ru.panyukovnn.bankappsearch.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.panyukovnn.bankappsearch.AbstractTest;
import ru.panyukovnn.bankappsearch.entity.LatestResultEntity;
import ru.panyukovnn.bankappsearch.util.ConstantTest;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Transactional
class LatestResultServiceImplTest extends AbstractTest {

    @Test
    @Sql("/sql/controller/search/insert-test-data.sql")
    void saveLatestResultEntityAndDeleteOldest_whenArgumentNotNull_expectSuccess() {
        LatestResultEntity latestResultEntity = LatestResultEntity.builder()
                .clientId(ConstantTest.CLIENT_ID)
                .searchString(ConstantTest.SEARCH_STRING)
                .build();

        latestResultService.saveLatestResultEntityAndDeleteOldest(latestResultEntity);

        Set<UUID> latestResultIds = latestResultRepository.findAll()
                .stream()
                .map(LatestResultEntity::getId)
                .collect(Collectors.toSet());

        assertFalse(latestResultIds.contains(ConstantTest.EARLIEST_LATEST_RESULT_ENTITY_ID));
        assertEquals(ConstantTest.MAX_LATEST_RESULTS, latestResultIds.size());
    }
}