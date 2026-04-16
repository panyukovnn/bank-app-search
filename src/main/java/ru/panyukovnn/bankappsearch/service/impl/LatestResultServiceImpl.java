package ru.panyukovnn.bankappsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.panyukovnn.bankappsearch.entity.LatestResultEntity;
import ru.panyukovnn.bankappsearch.repository.LatestResultRepository;
import ru.panyukovnn.bankappsearch.service.LatestResultService;

@Slf4j
@Service
@RequiredArgsConstructor
public class LatestResultServiceImpl implements LatestResultService {

    private final LatestResultRepository latestResultRepository;

    @Value("${bank-app-search.max-size-latest-results}")
    private Integer maxSizeLatestResults;

    @Override
    @Transactional
    public void saveLatestResultEntityAndDeleteOldest(LatestResultEntity latestResultEntity) {
        LatestResultEntity savedLatestResultEntity = latestResultRepository.save(latestResultEntity);

        String clientId = savedLatestResultEntity.getClientId();
        int countDeleted = latestResultRepository.deleteOldestByClientId(clientId, maxSizeLatestResults);

        log.info("Из таблицы latest_result было удалено {} строк с clientId: {}", countDeleted, clientId);
    }
}
