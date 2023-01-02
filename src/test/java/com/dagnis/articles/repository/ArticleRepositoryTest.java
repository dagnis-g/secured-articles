package com.dagnis.articles.repository;

import com.dagnis.articles.model.ArticleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void shouldFindPublishedCountFotPast7Days() {
        var article0 = createArticle(1L);
        var article1 = createArticle(2L);
        var article11 = createArticle(2L);
        var article3 = createArticle(3L);
        var article4 = createArticle(4L);
        var article5 = createArticle(5L);
        var article6 = createArticle(6L);
        var article7 = createArticle(7L);
        var article8 = createArticle(8L);
        var article9 = createArticle(0L);

        articleRepository.save(article0);
        articleRepository.save(article1);
        articleRepository.save(article11);
        articleRepository.save(article3);
        articleRepository.save(article4);
        articleRepository.save(article5);
        articleRepository.save(article6);
        articleRepository.save(article7);
        articleRepository.save(article8);

        var countByDayList = articleRepository.getCountByDay();

        assertThat(countByDayList.size()).isEqualTo(7);
        assertThat(countByDayList.get(0).getDatePublished()).isEqualTo(article0.getPublishDate().toLocalDate());
        assertThat(countByDayList.get(0).getCountPublished()).isEqualTo(1);
        assertThat(countByDayList.get(1).getDatePublished()).isEqualTo(article1.getPublishDate().toLocalDate());
        assertThat(countByDayList.get(1).getCountPublished()).isEqualTo(2);
        assertThat(countByDayList.get(2).getDatePublished()).isEqualTo(article3.getPublishDate().toLocalDate());
        assertThat(countByDayList.get(2).getCountPublished()).isEqualTo(1);
        assertThat(countByDayList.get(3).getDatePublished()).isEqualTo(article4.getPublishDate().toLocalDate());
        assertThat(countByDayList.get(3).getCountPublished()).isEqualTo(1);
        assertThat(countByDayList.get(3).getDatePublished()).isEqualTo(article4.getPublishDate().toLocalDate());
        assertThat(countByDayList.get(3).getCountPublished()).isEqualTo(1);
        assertThat(countByDayList.get(4).getDatePublished()).isEqualTo(article5.getPublishDate().toLocalDate());
        assertThat(countByDayList.get(4).getCountPublished()).isEqualTo(1);
        assertThat(countByDayList.get(5).getDatePublished()).isEqualTo(article6.getPublishDate().toLocalDate());
        assertThat(countByDayList.get(5).getCountPublished()).isEqualTo(1);
        assertThat(countByDayList.get(6).getDatePublished()).isEqualTo(article7.getPublishDate().toLocalDate());
        assertThat(countByDayList.get(6).getCountPublished()).isEqualTo(1);
    }

    private ArticleEntity createArticle(Long daysBack) {
        String title = "title" + daysBack;
        String author = "author" + daysBack;
        String content = "content" + daysBack;
        LocalDateTime datePublished = LocalDateTime.now().minusDays(daysBack);
        return new ArticleEntity(null, title, author, content, datePublished);
    }
}