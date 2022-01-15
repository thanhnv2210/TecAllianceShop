package com.tecalliance.shop.controller;

import com.tecalliance.shop.dto.ArticleDto;
import com.tecalliance.shop.dto.DiscountArticleDto;
import com.tecalliance.shop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    ShopService shopService;

    @GetMapping("/articles/priceByDate")
    public ResponseEntity<List<DiscountArticleDto>> get(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date at) {
        return ResponseEntity.ok(shopService.listBestPriceByDate(at));
    }

    @GetMapping("/articles/{id}/priceByDate")
    public ResponseEntity<DiscountArticleDto> get(@PathVariable int id
            , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date at) {
        return shopService.getBestPriceByDate(id,at)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
