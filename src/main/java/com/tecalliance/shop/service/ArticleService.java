package com.tecalliance.shop.service;

import com.tecalliance.shop.dto.ArticleDto;
import com.tecalliance.shop.dto.DiscountArticleDto;
import com.tecalliance.shop.dto.DiscountDto;
import com.tecalliance.shop.entity.Article;
import com.tecalliance.shop.entity.Discount;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ArticleService {
    Page<Article> search(Integer page, Integer size);
    Article save(ArticleDto data);
    Optional<Article> getById(int id);
    Optional<Article> getByName(String name);
    Discount saveNewDiscount(int id, DiscountDto data);
}
