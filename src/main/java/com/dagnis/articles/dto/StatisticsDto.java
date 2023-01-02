package com.dagnis.articles.dto;

import java.time.LocalDate;

public interface StatisticsDto {

    LocalDate getDatePublished();

    long getCountPublished();

}
