package ru.panyukovnn.bankappsearch.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.panyukovnn.bankappsearch.AbstractTest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class SearchControllerSuggestedTest extends AbstractTest {

    private static final String SUGGESTED_URL = "/api/v1/search/suggested";

    @Nested
    class GetSuggested {

        @Test
        @Sql("/sql/controller/search/suggested/insert-suggested-test-data.sql")
        void when_getSuggested_withValidRequest_then_returnsOkWithStructuredResponse() throws Exception {
            String requestBody = """
                {
                    "data": {
                        "clientId": "client1",
                        "platform": "ios"
                    }
                }
                """;

            mockMvc.perform(post(SUGGESTED_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.data.section", hasSize(1)))
                .andExpect(jsonPath("$.data.section[0].entityType").value("TOP"))
                .andExpect(jsonPath("$.data.section[0].entityName").value("Популярное"))
                .andExpect(jsonPath("$.data.section[0].pages", hasSize(2)))
                .andExpect(jsonPath("$.data.latestSearch", hasSize(2)));
        }

        @Test
        void when_getSuggested_withoutClientId_then_returnsBadRequest() throws Exception {
            String requestBody = """
                {
                    "data": {
                        "platform": "ios"
                    }
                }
                """;

            mockMvc.perform(post(SUGGESTED_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", notNullValue()))
                .andExpect(jsonPath("$.error.code").value("validation"))
                .andExpect(jsonPath("$.error.validations", hasSize(1)))
                .andExpect(jsonPath("$.error.validations[0].path").value("data.clientId"));
        }

        @Test
        @Sql("/sql/controller/search/suggested/insert-suggested-test-data.sql")
        void when_getSuggested_withIosPlatform_then_returnsOnlyIosPages() throws Exception {
            String requestBody = """
                {
                    "data": {
                        "clientId": "unknownClient",
                        "platform": "ios"
                    }
                }
                """;

            mockMvc.perform(post(SUGGESTED_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.section[0].pages", hasSize(2)))
                .andExpect(jsonPath("$.data.section[0].pages[0].name").value("Переводы"))
                .andExpect(jsonPath("$.data.section[0].pages[1].name").value("Платежи"))
                .andExpect(jsonPath("$.data.latestSearch", hasSize(0)));
        }

        @Test
        @Sql("/sql/controller/search/suggested/insert-suggested-test-data.sql")
        void when_getSuggested_withExistingClient_then_returnsLatestSearch() throws Exception {
            String requestBody = """
                {
                    "data": {
                        "clientId": "client1",
                        "platform": "android"
                    }
                }
                """;

            mockMvc.perform(post(SUGGESTED_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.section[0].pages", hasSize(1)))
                .andExpect(jsonPath("$.data.latestSearch", hasSize(2)))
                .andExpect(jsonPath("$.data.latestSearch[0].id").value("3a59e199-e1d7-47d6-b7c2-cc0ac5714451"))
                .andExpect(jsonPath("$.data.latestSearch[0].searchString").value("переводы"))
                .andExpect(jsonPath("$.data.latestSearch[1].id").value("3aab5ef3-ac43-4aed-80a6-2a4ad8114d37"))
                .andExpect(jsonPath("$.data.latestSearch[1].searchString").value("платежи"));
        }
    }
}