package com.dagnis.articles.service;

import com.dagnis.articles.dto.ArticleDto;
import com.dagnis.articles.model.ArticleEntity;
import com.dagnis.articles.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Spy
    private ModelMapper modelMapper;
    @Mock
    private ArticleRepository articleRepository;
    @InjectMocks
    private ArticleService articleService;

    @Captor
    private ArgumentCaptor<ArticleEntity> articleCaptor;

    @Test
    void shouldSaveArticleAndReturnArticleDto() {
        var articleDto = ArticleDto.builder().title("title").author("author").content("big text").publishDate(LocalDateTime.now()).build();
        var articleEntity = new ArticleEntity(1L, articleDto.getTitle(), articleDto.getAuthor(), articleDto.getContent(), articleDto.getPublishDate());

        when(articleRepository.save(any())).thenReturn(articleEntity);

        var savedDto = articleService.saveArticle(articleDto);

        verify(articleRepository).save(articleCaptor.capture());
        var savedArticleEntity = articleCaptor.getValue();

        assertThat(articleDto).isEqualTo(savedDto);
        assertThat(savedArticleEntity.getTitle()).isEqualTo(articleDto.getTitle());
        assertThat(savedArticleEntity.getAuthor()).isEqualTo(articleDto.getAuthor());
        assertThat(savedArticleEntity.getContent()).isEqualTo(articleDto.getContent());
        assertThat(savedArticleEntity.getPublishDate()).isEqualTo(articleDto.getPublishDate());
    }

    @Test
    void shouldGetListOfArticles() {
        var articleEntity1 = new ArticleEntity(1L, "title1", "author1", "content1", LocalDateTime.now().minusDays(2));
        var articleEntity2 = new ArticleEntity(2L, "title2", "author2", "content2", LocalDateTime.now().minusDays(3));
        List<ArticleEntity> articleList = new ArrayList<>();
        articleList.add(articleEntity1);
        articleList.add(articleEntity2);
        Page<ArticleEntity> articlePage = new PageImpl<>(articleList);

        when(articleRepository.findAll(PageRequest.of(0, 3, Sort.by("publishDate").descending()))).thenReturn(articlePage);

        var returnedArticles = articleService.getPageOfArticles(0);

        assertThat(returnedArticles.getContent().get(0).getTitle()).isEqualTo(articleEntity1.getTitle());
        assertThat(returnedArticles.getContent().get(0).getAuthor()).isEqualTo(articleEntity1.getAuthor());
        assertThat(returnedArticles.getContent().get(0).getContent()).isEqualTo(articleEntity1.getContent());
        assertThat(returnedArticles.getContent().get(0).getPublishDate()).isEqualTo(articleEntity1.getPublishDate());
        assertThat(returnedArticles.getContent().get(1).getTitle()).isEqualTo(articleEntity2.getTitle());
        assertThat(returnedArticles.getContent().get(1).getAuthor()).isEqualTo(articleEntity2.getAuthor());
        assertThat(returnedArticles.getContent().get(1).getContent()).isEqualTo(articleEntity2.getContent());
        assertThat(returnedArticles.getContent().get(1).getPublishDate()).isEqualTo(articleEntity2.getPublishDate());
    }

    @Test
    void shouldGetCountByDay() {
        articleService.getArticleCountForLast7Days();

        verify(articleRepository).getCountByDay();
    }

}