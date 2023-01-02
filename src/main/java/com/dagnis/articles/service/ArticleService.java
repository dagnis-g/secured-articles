package com.dagnis.articles.service;

import com.dagnis.articles.dto.ArticleDto;
import com.dagnis.articles.dto.StatisticsDto;
import com.dagnis.articles.model.ArticleEntity;
import com.dagnis.articles.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

    private final ModelMapper modelMapper;
    private final ArticleRepository articleRepository;

    public ArticleDto saveArticle(ArticleDto articleDto) {
        ArticleEntity articleEntity = modelMapper.map(articleDto, ArticleEntity.class);
        ArticleEntity savedArticle = articleRepository.save(articleEntity);
        log.info("Saved article: {}", savedArticle);

        return modelMapper.map(savedArticle, ArticleDto.class);
    }
    
    public Page<ArticleDto> getPageOfArticles(Integer page) {
        Page<ArticleEntity> pageOfArticles = articleRepository.findAll(PageRequest.of(page, 3, Sort.by("publishDate").descending()));
        log.info("Returned articles: {}", pageOfArticles.getContent());

        return pageOfArticles.map(article -> modelMapper.map(article, ArticleDto.class));
    }

    public List<StatisticsDto> getArticleCountForLast7Days() {
        return articleRepository.getCountByDay();
    }

}
