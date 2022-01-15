package com.tecalliance.shop.dto;

import com.tecalliance.shop.entity.Article;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ArticleDto {
    Integer id;
    @NotNull
    String name;
    @NotNull
    String slogan;
    @NotNull
    Double netPrice;
    @NotNull
    Double salePrice;
    @NotNull
    Double vat;

    public ArticleDto(String name, String slogan, Double netPrice, Double salePrice, Double vat) {
        this.name = name;
        this.slogan = slogan;
        this.netPrice = netPrice;
        this.salePrice = salePrice;
        this.vat = vat;
    }

    public Double getMaxDiscountPercent() {
        return (salePrice - netPrice) / salePrice * 100;
    }

    public static ArticleDto fromEntity(Article article) {
        ArticleDto result = new ArticleDto();
        BeanUtils.copyProperties(article, result);
        return result;
    }
}
