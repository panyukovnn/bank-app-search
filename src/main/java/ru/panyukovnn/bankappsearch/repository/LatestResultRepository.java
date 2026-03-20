package ru.panyukovnn.bankappsearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.panyukovnn.bankappsearch.entity.LatestResult;

import java.util.List;
import java.util.UUID;

public interface LatestResultRepository extends JpaRepository<LatestResult, UUID> {

    List<LatestResult> findByClientIdOrderByCreateTimeDesc(String clientId);
}
