package com.tecalliance.shop.service;

import com.tecalliance.shop.dto.DiscountArticleDto;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ShopService {
    List<DiscountArticleDto> listBestPriceByDate(Date at);
    Optional<DiscountArticleDto> getBestPriceByDate(int id, Date at);
}
