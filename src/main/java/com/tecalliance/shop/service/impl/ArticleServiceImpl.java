package com.tecalliance.shop.service.impl;

import com.tecalliance.shop.dto.ArticleDto;
import com.tecalliance.shop.dto.DiscountDto;
import com.tecalliance.shop.entity.Article;
import com.tecalliance.shop.entity.Discount;
import com.tecalliance.shop.exception.CustomException;
import com.tecalliance.shop.repository.ArticleRepository;
import com.tecalliance.shop.repository.DiscountRepository;
import com.tecalliance.shop.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    DiscountRepository discountRepository;

    @Override
    public Page<Article> search(Integer page, Integer size) {
        return null;
    }

    @Override
    public Article save(ArticleDto data) {
        if(articleRepository.findByName(data.getName()).isPresent())
            throw new CustomException(HttpStatus.CONFLICT, "Duplicated data");
        return articleRepository.save(toEntity(data));
    }

    private Article toEntity(ArticleDto data) {
        Article result = new Article();
        BeanUtils.copyProperties(data, result);
        return result;
    }

    @Override
    public Optional<Article> getById(int id) {
        return articleRepository.findById(id);
    }

    @Override
    public Optional<Article> getByName(String name){
        return articleRepository.findByName(name);
    }

    @Override
    public Discount saveNewDiscount(int id, DiscountDto data) {
        Optional<Article> article = articleRepository.findById(id);
        if(!article.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        if(article.get().getMaxDiscountPercent() < data.getPercent()){
            throw new CustomException(HttpStatus.BAD_REQUEST, "Out of discount range, " +
                    "the max discount percent of articleId:" + id + " is:" + article.get().getMaxDiscountPercent());
        }
        Discount entity = new Discount();
        BeanUtils.copyProperties(data, entity);
        entity.setArticleId(id);
        discountRepository.save(entity);
        return entity;
    }
}
