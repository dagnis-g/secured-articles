package com.dagnis.articles.controller;

import com.dagnis.articles.config.WebSecurityConfig;
import com.dagnis.articles.dto.ArticleDto;
import com.dagnis.articles.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebSecurityConfig.class, ArticleController.class})
@WebAppConfiguration
@WebMvcTest
class ArticleControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @MockBean
    private ArticleService articleService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void shouldRejectWithoutLoggingIn_Statistics() throws Exception {
        mvc.perform(get("/articles/statistics"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectWithoutLoggingIn_SaveArticle() throws Exception {
        mvc.perform(post("/articles/save"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectWithoutLoggingIn_ArticlesPage() throws Exception {
        mvc.perform(get("/articles/0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldBeAccessibleByAdmin_Statistics() throws Exception {
        UserDetails admin = createBasicUser("ADMIN");

        mvc.perform(get("/articles/statistics").with(user(admin)))
                .andExpect(status().isOk())
                .andReturn();

        verify(articleService).getArticleCountForLast7Days();
    }

    @Test
    void shouldNotBeAccessibleByUser_Statistics() throws Exception {
        UserDetails user = createBasicUser("USER");

        mvc.perform(get("/articles/statistics").with(user(user)))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(articleService, times(0)).getArticleCountForLast7Days();
    }
    
    @Test
    void shouldGetArticlesWithAuthorityUser() throws Exception {
        UserDetails user = createBasicUser("USER");
        ArticleDto articleDto = createArticleDto();
        List<ArticleDto> dtoList = new ArrayList<>();
        dtoList.add(articleDto);
        Page<ArticleDto> page = new PageImpl<>(dtoList);

        when(articleService.getPageOfArticles(0)).thenReturn(page);

        String result = mvc.perform(get("/articles/0").with(user(user)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(page));
    }

    @Test
    void shouldGetArticlesWithAuthorityAdmin() throws Exception {
        UserDetails user = createBasicUser("ADMIN");
        ArticleDto articleDto = createArticleDto();
        List<ArticleDto> dtoList = new ArrayList<>();
        dtoList.add(articleDto);
        Page<ArticleDto> page = new PageImpl<>(dtoList);

        when(articleService.getPageOfArticles(0)).thenReturn(page);

        String result = mvc.perform(get("/articles/0").with(user(user)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(page));
    }

    @Test
    void shouldBeAbleToSaveArticleByUser() throws Exception {
        UserDetails user = createBasicUser("USER");
        ArticleDto articleDto = createArticleDto();

        when(articleService.saveArticle(articleDto)).thenReturn(articleDto);

        String result = mvc.perform(post("/articles/save").with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(articleService).saveArticle(articleDto);
        assertThat(result).isEqualTo(objectMapper.writeValueAsString(articleDto));
    }

    @Test
    void shouldBeAbleToSaveArticleByAdmin() throws Exception {
        UserDetails user = createBasicUser("ADMIN");
        ArticleDto articleDto = createArticleDto();

        when(articleService.saveArticle(articleDto)).thenReturn(articleDto);

        String result = mvc.perform(post("/articles/save").with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(articleService).saveArticle(articleDto);
        assertThat(result).isEqualTo(objectMapper.writeValueAsString(articleDto));
    }

    private ArticleDto createArticleDto() {
        return ArticleDto.builder()
                .title("harry")
                .author("jk")
                .content("big story")
                .publishDate(LocalDateTime.parse("2022-12-29T14:17:30"))
                .build();
    }

    private UserDetails createBasicUser(String authority) {
        return User.builder()
                .username("admin")
                .password("asdasd")
                .authorities(authority)
                .build();
    }
}



