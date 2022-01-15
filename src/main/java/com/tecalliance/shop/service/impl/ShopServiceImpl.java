package com.tecalliance.shop.service.impl;

import com.tecalliance.shop.dto.DiscountArticleDto;
import com.tecalliance.shop.entity.Article;
import com.tecalliance.shop.entity.Discount;
import com.tecalliance.shop.repository.ArticleRepository;
import com.tecalliance.shop.repository.DiscountRepository;
import com.tecalliance.shop.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    DiscountRepository discountRepository;

    @Override
    public List<DiscountArticleDto> listBestPriceByDate(Date at) {
        return discountRepository.listArticleWithDiscount(at);
    }

    @Override
    public Optional<DiscountArticleDto> getBestPriceByDate(int id, Date at) {
        Optional<Article> article = articleRepository.findById(id);
        if(!article.isPresent()){
            return Optional.empty();
        }
        //Optional<Discount> bestDiscount = discountRepository.findBestDiscount(article.get().getId(), at);
        List<Discount> discounts = discountRepository.listDiscountOrderByPercentDesc(article.get().getId(), at);
        return Optional.of(getDiscountInfo(article.get(), discounts, at));
    }

    private DiscountArticleDto getDiscountInfo(Article article, List<Discount> discounts, Date at) {
        // Because we only can apply 1 discount for the article
        Optional<Discount> bestDiscount = !discounts.isEmpty() ? Optional.ofNullable(discounts.get(0)) : Optional.empty();
        DiscountArticleDto result = new DiscountArticleDto();
        BeanUtils.copyProperties(article, result);
        result.setArticleId(article.getId());
        result.setDiscountId(bestDiscount.map(Discount::getId).orElse(null));
        result.setPercent(bestDiscount.map(Discount::getPercent).orElse(null));
        return result;
    }
}
