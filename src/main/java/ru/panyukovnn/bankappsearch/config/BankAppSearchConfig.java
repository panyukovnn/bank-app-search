package ru.panyukovnn.bankappsearch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.panyukovnn.bankappsearch.property.LatestResultsPoolProperty;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class BankAppSearchConfig {

    @Bean
    public ExecutorService latestResultsPool(LatestResultsPoolProperty property) {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(property.getQueueSize());
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                property.getPoolSize(),
                property.getPoolSize(),
                property.getThreadTimeoutSeconds(),
                TimeUnit.SECONDS,
                queue,
                new ThreadPoolExecutor.CallerRunsPolicy());
        executor.allowCoreThreadTimeOut(true);

        return executor;
    }
}
