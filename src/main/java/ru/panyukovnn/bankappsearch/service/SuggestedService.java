package ru.panyukovnn.bankappsearch.service;

import ru.panyukovnn.bankappsearch.dto.SuggestedRequestData;
import ru.panyukovnn.bankappsearch.dto.SuggestedResponseData;

public interface SuggestedService {

    SuggestedResponseData handleSuggested(SuggestedRequestData request);
}
