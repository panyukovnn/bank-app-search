package ru.panyukovnn.bankappsearch.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import ru.panyukovnn.bankappsearch.dto.ExternalSectionDto;
import ru.panyukovnn.bankappsearch.dto.SearchSectionRequestData;

import java.util.List;

@FeignClient(
        url = "${bank-app-search.integrations.operation-history.host}/operation-history/api/v1",
        name = "operation-history")
public interface OperationHistoryClient {

    @PostMapping("/operations/search")
    List<ExternalSectionDto> findExternalSections(SearchSectionRequestData searchSectionRequestData);
}
