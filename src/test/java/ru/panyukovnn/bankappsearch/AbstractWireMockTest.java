package ru.panyukovnn.bankappsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.request;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

public abstract class AbstractWireMockTest extends AbstractTest {

    private static final ObjectMapper OBJECT_MAPPER = Jackson2ObjectMapperBuilder.json()
            .build();

    @BeforeEach
    public void beforeEach() {
        WireMock.reset();
    }

    @SneakyThrows
    protected void stubResponseWithDelay(HttpMethod httpMethod, String url, List<?> response, int status, int delay)  {
        stubFor(request(httpMethod.name(), urlMatching(url))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(OBJECT_MAPPER.writeValueAsBytes(response))
                        .withFixedDelay(delay)
                        .withStatus(status)));
    }

    @SneakyThrows
    protected void stubResponse(HttpMethod httpMethod, String url, List<?> response, int status) {
        stubFor(request(httpMethod.name(), urlMatching(url))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(OBJECT_MAPPER.writeValueAsBytes(response))
                        .withStatus(status)));
    }

    protected void stubInternalServerErrorResponse(HttpMethod httpMethod, String url) {
        stubFor(request(httpMethod.name(), urlMatching(url))
                .willReturn(aResponse()
                        .withStatus(500)));
    }
}
