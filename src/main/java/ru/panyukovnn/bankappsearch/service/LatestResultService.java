package ru.panyukovnn.bankappsearch.service;

import ru.panyukovnn.bankappsearch.entity.LatestResultEntity;

public interface LatestResultService {

    void saveLatestResultEntityAndDeleteOldest(LatestResultEntity latestResultEntity);
}
