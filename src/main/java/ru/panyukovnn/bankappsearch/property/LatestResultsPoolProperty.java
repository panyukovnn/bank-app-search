package ru.panyukovnn.bankappsearch.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "bank-app-search.pools.latest-results-pool")
public class LatestResultsPoolProperty {

    private Integer poolSize;
    private Integer threadTimeoutSeconds;
    private Integer queueSize;
}
