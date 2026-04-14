package ru.panyukovnn.bankappsearch.service;

import ru.panyukovnn.bankappsearch.dto.SearchSectionRequestData;
import ru.panyukovnn.bankappsearch.dto.SearchSectionResponseData;

public interface SearchSectionService {

    SearchSectionResponseData findSections(SearchSectionRequestData requestData);
}
