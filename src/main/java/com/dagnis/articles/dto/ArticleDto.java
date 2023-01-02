package com.dagnis.articles.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    @NotBlank
    @Size(max = 100)
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    private String content;
    @NotNull
    private LocalDateTime publishDate;

}
