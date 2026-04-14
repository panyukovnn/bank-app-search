package ru.panyukovnn.bankappsearch.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.panyukovnn.bankappsearch.AbstractWireMockTest;
import ru.panyukovnn.bankappsearch.dto.EntityType;
import ru.panyukovnn.bankappsearch.dto.ExternalSectionDto;
import ru.panyukovnn.bankappsearch.dto.SearchSectionRequestData;
import ru.panyukovnn.bankappsearch.dto.SearchType;
import ru.panyukovnn.bankappsearch.dto.common.CommonRequest;
import ru.panyukovnn.bankappsearch.util.Constant;
import ru.panyukovnn.bankappsearch.util.ConstantTest;
import ru.panyukovnn.bankappsearch.util.DataFactoryTest;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class SearchControllerWireMockTest extends AbstractWireMockTest {

    private static final String OPERATION_HISTORY_URL = "/operation-history/api/v1/operations/search";
    private static final String SEARCH_SECTIONS_URL = "/api/v1/search";

    @Test
    @SneakyThrows
    @Sql("/sql/controller/search/insert-test-data.sql")
    void findSections_whenSearchTypesIsNull_expectEmptyResult() {
        SearchSectionRequestData searchSectionRequestData = DataFactoryTest.createSearchSectionRequestData(null, 10);
        CommonRequest<SearchSectionRequestData> request = CommonRequest.<SearchSectionRequestData>builder()
                .data(searchSectionRequestData)
                .build();
        List<ExternalSectionDto> externalSectionDtoList = DataFactoryTest.createExternalSectionDtoList();

        stubResponse(HttpMethod.POST, OPERATION_HISTORY_URL, externalSectionDtoList, HttpStatus.OK.value());

        mockMvc.perform(post(SEARCH_SECTIONS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data.section").doesNotExist())
                .andExpect(jsonPath("$.data.externalSection").doesNotExist());
    }

    @Test
    @SneakyThrows
    @Sql("/sql/controller/search/insert-test-data.sql")
    void findSections_whenSearchTypesOnlyPage_expectSectionListExists() {
        SearchSectionRequestData searchSectionRequestData = DataFactoryTest.createSearchSectionRequestData(
                List.of(SearchType.PAGE), 10);
        CommonRequest<SearchSectionRequestData> request = CommonRequest.<SearchSectionRequestData>builder()
                .data(searchSectionRequestData)
                .build();
        List<ExternalSectionDto> externalSectionDtoList = DataFactoryTest.createExternalSectionDtoList();

        searchSectionRequestData.setClientVersion(null);
        stubResponse(HttpMethod.POST, OPERATION_HISTORY_URL, externalSectionDtoList, HttpStatus.OK.value());

        mockMvc.perform(post(SEARCH_SECTIONS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data.externalSection").doesNotExist())
                .andExpect(jsonPath("$.data.section[0].entityType").value(EntityType.SECTION.name()))
                .andExpect(jsonPath("$.data.section[0].entityName").value(Constant.SEARCH_SECTION_ENTITY_NAME))
                .andExpect(jsonPath("$.data.section[0].pages[0].name").value(ConstantTest.PAGE_NAME))
                .andExpect(jsonPath("$.data.section[0].pages[0].link").value(ConstantTest.PAGE_LINK))
                .andExpect(jsonPath("$.data.section[0].pages[0].icon").value(ConstantTest.PAGE_ICON))
                .andExpect(jsonPath("$.data.section[0].pages[1].name").value(ConstantTest.PAGE_NAME_2))
                .andExpect(jsonPath("$.data.section[0].pages[1].link").value(ConstantTest.PAGE_LINK_2))
                .andExpect(jsonPath("$.data.section[0].pages[1].icon").value(ConstantTest.PAGE_ICON_2));
    }

    @Test
    @SneakyThrows
    @Sql("/sql/controller/search/insert-test-data.sql")
    void findSections_whenSearchTypesOnlyHistoryAndResponseStatusOperationHistoryService200_expectExternalSectionListExists() {
        SearchSectionRequestData searchSectionRequestData = DataFactoryTest.createSearchSectionRequestData(
                List.of(SearchType.HISTORY), null);
        CommonRequest<SearchSectionRequestData> request = CommonRequest.<SearchSectionRequestData>builder()
                .data(searchSectionRequestData)
                .build();
        List<ExternalSectionDto> externalSectionDtoList = DataFactoryTest.createExternalSectionDtoList();

        stubResponse(HttpMethod.POST, OPERATION_HISTORY_URL, externalSectionDtoList, HttpStatus.OK.value());

        mockMvc.perform(post(SEARCH_SECTIONS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data.section").doesNotExist())
                .andExpect(jsonPath("$.data.externalSection[0].entityType").value(EntityType.OPERATION.name()))
                .andExpect(jsonPath("$.data.externalSection[0].records[0].id").value(ConstantTest.OPERATION_ID))
                .andExpect(jsonPath("$.data.externalSection[0].records[0].name").value(ConstantTest.OPERATION_NAME))
                .andExpect(jsonPath("$.data.externalSection[0].records[0].categoryCode").value(ConstantTest.OPERATION_CATEGORY_CODE))
                .andExpect(jsonPath("$.data.externalSection[0].records[0].typeCode").value(ConstantTest.OPERATION_TYPE_CODE))
                .andExpect(jsonPath("$.data.externalSection[0].records[0].icon").value(ConstantTest.PAGE_ICON))
                .andExpect(jsonPath("$.data.externalSection[1].entityType").value(EntityType.HISTORY.name()))
                .andExpect(jsonPath("$.data.externalSection[1].operations[0].id").value(ConstantTest.HISTORY_ID))
                .andExpect(jsonPath("$.data.externalSection[1].operations[0].name").value(ConstantTest.HISTORY_NAME))
                .andExpect(jsonPath("$.data.externalSection[1].operations[0].amount").value(ConstantTest.AMOUNT))
                .andExpect(jsonPath("$.data.externalSection[1].operations[0].currency").value(ConstantTest.RUB_CURRENCY))
                .andExpect(jsonPath("$.data.externalSection[1].operations[0].date").exists());
    }

    @Test
    @SneakyThrows
    @Sql("/sql/controller/search/insert-test-data.sql")
    void findSections_whenSearchTypesOnlyHistoryAndResponseStatusOperationHistoryService400_expectEmptyExternalSectionList() {
        SearchSectionRequestData searchSectionRequestData = DataFactoryTest.createSearchSectionRequestData(
                List.of(SearchType.HISTORY), null);
        CommonRequest<SearchSectionRequestData> request = CommonRequest.<SearchSectionRequestData>builder()
                .data(searchSectionRequestData)
                .build();
        List<ExternalSectionDto> externalSectionDtoList = DataFactoryTest.createExternalSectionDtoList();

        stubResponse(HttpMethod.POST, OPERATION_HISTORY_URL, externalSectionDtoList, HttpStatus.BAD_REQUEST.value());

        mockMvc.perform(post(SEARCH_SECTIONS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data.section").doesNotExist())
                .andExpect(jsonPath("$.data.externalSection").isEmpty());
    }

    @Test
    @SneakyThrows
    @Sql("/sql/controller/search/insert-test-data.sql")
    void findSections_whenSearchTypesOnlyHistoryAndResponseStatusOperationHistoryService404_expectEmptyExternalSectionList() {
        SearchSectionRequestData searchSectionRequestData = DataFactoryTest.createSearchSectionRequestData(
                List.of(SearchType.HISTORY), null);
        CommonRequest<SearchSectionRequestData> request = CommonRequest.<SearchSectionRequestData>builder()
                .data(searchSectionRequestData)
                .build();
        List<ExternalSectionDto> externalSectionDtoList = DataFactoryTest.createExternalSectionDtoList();

        stubResponse(HttpMethod.POST, OPERATION_HISTORY_URL, externalSectionDtoList, HttpStatus.BAD_REQUEST.value());

        mockMvc.perform(post(SEARCH_SECTIONS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data.section").doesNotExist())
                .andExpect(jsonPath("$.data.externalSection").isEmpty());
    }

    @Test
    @SneakyThrows
    @Sql("/sql/controller/search/insert-test-data.sql")
    void findSections_whenSearchTypesOnlyHistoryAndResponseStatusOperationHistoryService505_expectEmptyExternalSectionList() {
        SearchSectionRequestData searchSectionRequestData = DataFactoryTest.createSearchSectionRequestData(
                List.of(SearchType.HISTORY), null);
        CommonRequest<SearchSectionRequestData> request = CommonRequest.<SearchSectionRequestData>builder()
                .data(searchSectionRequestData)
                .build();

        stubInternalServerErrorResponse(HttpMethod.POST, OPERATION_HISTORY_URL);

        mockMvc.perform(post(SEARCH_SECTIONS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data.section").doesNotExist())
                .andExpect(jsonPath("$.data.externalSection").isEmpty());
    }

    @Test
    @SneakyThrows
    @Sql("/sql/controller/search/insert-test-data.sql")
    void findSections_whenSearchTypesOnlyHistoryAndTimeoutExceeded_expectEmptyExternalSectionList() {
        SearchSectionRequestData searchSectionRequestData = DataFactoryTest.createSearchSectionRequestData(
                List.of(SearchType.HISTORY), null);
        CommonRequest<SearchSectionRequestData> request = CommonRequest.<SearchSectionRequestData>builder()
                .data(searchSectionRequestData)
                .build();
        List<ExternalSectionDto> externalSectionDtoList = DataFactoryTest.createExternalSectionDtoList();

        stubResponseWithDelay(HttpMethod.POST, OPERATION_HISTORY_URL, externalSectionDtoList, HttpStatus.OK.value(), 1100);

        mockMvc.perform(post(SEARCH_SECTIONS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data.section").doesNotExist())
                .andExpect(jsonPath("$.data.externalSection").isEmpty());
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("findSections_whenRequestBodyNotValid_methodSource")
    void findSections_whenRequestBodyNotValid_expectResponseStatus400(SearchSectionRequestData requestData) {
        CommonRequest<SearchSectionRequestData> request = CommonRequest.<SearchSectionRequestData>builder()
                .data(requestData)
                .build();

        mockMvc.perform(post(SEARCH_SECTIONS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> findSections_whenRequestBodyNotValid_methodSource() {
        return Stream.of(
                Arguments.of(new SearchSectionRequestData(null, ConstantTest.SEARCH_STRING,
                        ConstantTest.IOS_PLATFORM, ConstantTest.PLATFORM_VERSION, null, null)),
                Arguments.of(new SearchSectionRequestData("123", ConstantTest.SEARCH_STRING,
                        ConstantTest.IOS_PLATFORM, ConstantTest.PLATFORM_VERSION, null, null)),
                Arguments.of(new SearchSectionRequestData(ConstantTest.CLIENT_ID, null,
                        ConstantTest.IOS_PLATFORM, ConstantTest.PLATFORM_VERSION, null, null)),
                Arguments.of(new SearchSectionRequestData(ConstantTest.CLIENT_ID, "12",
                        ConstantTest.IOS_PLATFORM, ConstantTest.PLATFORM_VERSION, null, null)),
                Arguments.of(new SearchSectionRequestData(ConstantTest.CLIENT_ID, ConstantTest.SEARCH_STRING,
                        " ", ConstantTest.PLATFORM_VERSION, null, null)),
                Arguments.of(new SearchSectionRequestData(ConstantTest.CLIENT_ID, ConstantTest.SEARCH_STRING,
                        ConstantTest.IOS_PLATFORM, ConstantTest.PLATFORM_VERSION, null, -1))
        );
    }
}