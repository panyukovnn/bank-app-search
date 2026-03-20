package ru.panyukovnn.bankappsearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.panyukovnn.bankappsearch.entity.Page;

import java.util.List;
import java.util.UUID;

public interface PageRepository extends JpaRepository<Page, UUID> {

    List<Page> findByTopResultTrueAndPlatform(String platform);
}
