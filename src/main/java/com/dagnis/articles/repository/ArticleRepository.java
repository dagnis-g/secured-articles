package com.dagnis.articles.repository;

import com.dagnis.articles.dto.StatisticsDto;
import com.dagnis.articles.model.ArticleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends PagingAndSortingRepository<ArticleEntity, Long> {

    @Query(value = "select date(article.publish_date) as datePublished, count(*) as countPublished from article " +
            "where article.publish_date > current_date - 7 day " +
            "group by date(article.publish_date) " +
            "order by date(article.publish_date) desc",
            nativeQuery = true)
    List<StatisticsDto> getCountByDay();
    
}
