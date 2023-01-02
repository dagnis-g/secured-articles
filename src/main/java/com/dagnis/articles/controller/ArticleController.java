package com.dagnis.articles.controller;

import com.dagnis.articles.dto.ArticleDto;
import com.dagnis.articles.dto.StatisticsDto;
import com.dagnis.articles.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(description = "Save article")
    @PostMapping("/save")
    public ArticleDto saveArticle(@Valid @RequestBody ArticleDto article) {
        System.out.println(article);
        return articleService.saveArticle(article);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(description = "Get paginated list of articles")
    @GetMapping("/{page}")
    public Page<ArticleDto> getArticles(@PathVariable Integer page) {
        return articleService.getPageOfArticles(page);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(description = "Get published article count in past 7 days by day")
    @GetMapping(value = "/statistics")
    public List<StatisticsDto> getStatistics() {
        return articleService.getArticleCountForLast7Days();
    }

}
