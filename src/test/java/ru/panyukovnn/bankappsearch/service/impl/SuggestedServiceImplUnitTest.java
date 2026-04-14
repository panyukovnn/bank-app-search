package ru.panyukovnn.bankappsearch.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.panyukovnn.bankappsearch.dto.LatestSearchDto;
import ru.panyukovnn.bankappsearch.dto.SuggestedRequestData;
import ru.panyukovnn.bankappsearch.dto.SuggestedResponseData;
import ru.panyukovnn.bankappsearch.dto.SuggestedSectionDto;
import ru.panyukovnn.bankappsearch.entity.LatestResultEntity;
import ru.panyukovnn.bankappsearch.entity.PageEntity;
import ru.panyukovnn.bankappsearch.repository.LatestResultRepository;
import ru.panyukovnn.bankappsearch.repository.PageRepository;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SuggestedServiceImplUnitTest {

    private final PageRepository pageRepository = mock(PageRepository.class);
    private final LatestResultRepository latestResultRepository = mock(LatestResultRepository.class);

    private final SuggestedServiceImpl suggestedService = new SuggestedServiceImpl(pageRepository, latestResultRepository);

    @Nested
    class HandleSuggested {

        @Test
        void when_handleSuggested_withIosPlatform_then_returnsTopPages() {
            UUID pageId = UUID.fromString("f59b2248-3b9a-412c-b2a1-dabdff0d7839");

            PageEntity pageEntity = PageEntity.builder()
                .id(pageId)
                .name("Переводы")
                .link("/transfers")
                .platform("ios")
                .topResult(true)
                .build();

            when(pageRepository.findByTopResultTrueAndPlatform("ios")).thenReturn(List.of(pageEntity));
            when(latestResultRepository.findByClientIdOrderByCreateTimeDesc("client1")).thenReturn(List.of());

            SuggestedRequestData request = SuggestedRequestData.builder()
                .clientId("client1")
                .platform("ios")
                .build();

            SuggestedResponseData result = suggestedService.handleSuggested(request);

            assertThat(result.getSection(), hasSize(1));

            SuggestedSectionDto section = result.getSection().get(0);
            assertEquals("TOP", section.getEntityType());
            assertEquals("Популярное", section.getEntityName());
            assertThat(section.getPages(), hasSize(1));
            assertEquals("Переводы", section.getPages().get(0).getName());
            assertEquals("/transfers", section.getPages().get(0).getLink());
            assertNull(section.getPages().get(0).getIcon());
            assertThat(result.getLatestSearch(), empty());

            verify(pageRepository).findByTopResultTrueAndPlatform("ios");
            verify(latestResultRepository).findByClientIdOrderByCreateTimeDesc("client1");
        }

        @Test
        void when_handleSuggested_withNoTopResults_then_returnsEmptyPages() {
            when(pageRepository.findByTopResultTrueAndPlatform("android")).thenReturn(List.of());
            when(latestResultRepository.findByClientIdOrderByCreateTimeDesc("client2")).thenReturn(List.of());

            SuggestedRequestData request = SuggestedRequestData.builder()
                .clientId("client2")
                .platform("android")
                .build();

            SuggestedResponseData result = suggestedService.handleSuggested(request);

            assertThat(result.getSection(), hasSize(1));

            SuggestedSectionDto section = result.getSection().get(0);
            assertEquals("TOP", section.getEntityType());
            assertEquals("Популярное", section.getEntityName());
            assertThat(section.getPages(), empty());
            assertThat(result.getLatestSearch(), empty());

            verify(pageRepository).findByTopResultTrueAndPlatform("android");
            verify(latestResultRepository).findByClientIdOrderByCreateTimeDesc("client2");
        }

        @Test
        void when_handleSuggested_withClientVersion_then_filtersNewerPages() {
            PageEntity pageEntityWithoutVersion = PageEntity.builder()
                .id(UUID.fromString("f59b2248-3b9a-412c-b2a1-dabdff0d7839"))
                .name("Переводы")
                .link("/transfers")
                .platform("ios")
                .version(null)
                .topResult(true)
                .build();

            PageEntity pageEntityWithOlderVersion = PageEntity.builder()
                .id(UUID.fromString("7cc96730-2a16-4ee5-a8b8-0545a3e1dcd3"))
                .name("Платежи")
                .link("/payments")
                .platform("ios")
                .version("1.0.0")
                .topResult(true)
                .build();

            PageEntity pageEntityWithNewerVersion = PageEntity.builder()
                .id(UUID.fromString("449a5319-92ec-43d2-b884-5d1a80dd5f18"))
                .name("Кредиты")
                .link("/credits")
                .platform("ios")
                .version("3.0.0")
                .topResult(true)
                .build();

            when(pageRepository.findByTopResultTrueAndPlatform("ios"))
                .thenReturn(List.of(pageEntityWithoutVersion, pageEntityWithOlderVersion, pageEntityWithNewerVersion));
            when(latestResultRepository.findByClientIdOrderByCreateTimeDesc("client1")).thenReturn(List.of());

            SuggestedRequestData request = SuggestedRequestData.builder()
                .clientId("client1")
                .platform("ios")
                .clientVersion("2.0.0")
                .build();

            SuggestedResponseData result = suggestedService.handleSuggested(request);

            assertThat(result.getSection(), hasSize(1));

            SuggestedSectionDto section = result.getSection().get(0);
            assertThat(section.getPages(), hasSize(2));
            assertEquals("Переводы", section.getPages().get(0).getName());
            assertEquals("Платежи", section.getPages().get(1).getName());

            verify(pageRepository).findByTopResultTrueAndPlatform("ios");
        }

        @Test
        void when_handleSuggested_withClientHistory_then_returnsLatestSearch() {
            UUID resultId1 = UUID.fromString("3a59e199-e1d7-47d6-b7c2-cc0ac5714451");
            UUID resultId2 = UUID.fromString("3aab5ef3-ac43-4aed-80a6-2a4ad8114d37");

            LatestResultEntity latestResultEntity1 = LatestResultEntity.builder()
                .id(resultId1)
                .clientId("client1")
                .searchString("переводы")
                .build();

            LatestResultEntity latestResultEntity2 = LatestResultEntity.builder()
                .id(resultId2)
                .clientId("client1")
                .searchString("платежи")
                .build();

            when(pageRepository.findByTopResultTrueAndPlatform("ios")).thenReturn(List.of());
            when(latestResultRepository.findByClientIdOrderByCreateTimeDesc("client1"))
                .thenReturn(List.of(latestResultEntity1, latestResultEntity2));

            SuggestedRequestData request = SuggestedRequestData.builder()
                .clientId("client1")
                .platform("ios")
                .build();

            SuggestedResponseData result = suggestedService.handleSuggested(request);

            assertThat(result.getLatestSearch(), hasSize(2));

            LatestSearchDto firstSearch = result.getLatestSearch().get(0);
            assertEquals("3a59e199-e1d7-47d6-b7c2-cc0ac5714451", firstSearch.getId());
            assertEquals("переводы", firstSearch.getSearchString());

            LatestSearchDto secondSearch = result.getLatestSearch().get(1);
            assertEquals("3aab5ef3-ac43-4aed-80a6-2a4ad8114d37", secondSearch.getId());
            assertEquals("платежи", secondSearch.getSearchString());

            verify(latestResultRepository).findByClientIdOrderByCreateTimeDesc("client1");
        }

        @Test
        void when_handleSuggested_withNoClientHistory_then_returnsEmptyLatestSearch() {
            when(pageRepository.findByTopResultTrueAndPlatform("ios")).thenReturn(List.of());
            when(latestResultRepository.findByClientIdOrderByCreateTimeDesc("client3")).thenReturn(List.of());

            SuggestedRequestData request = SuggestedRequestData.builder()
                .clientId("client3")
                .platform("ios")
                .build();

            SuggestedResponseData result = suggestedService.handleSuggested(request);

            assertThat(result.getLatestSearch(), empty());

            verify(latestResultRepository).findByClientIdOrderByCreateTimeDesc("client3");
        }
    }
}