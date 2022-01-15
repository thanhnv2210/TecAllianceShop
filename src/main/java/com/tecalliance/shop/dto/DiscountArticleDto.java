package com.tecalliance.shop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountArticleDto {
    Integer articleId;
    String name;
    String slogan;
    @JsonIgnore
    Double netPrice;
    Double salePrice;
    Double vat;
    Integer discountId;
    Double percent;
    BigDecimal price;
    BigDecimal priceIncludeVat;

    public DiscountArticleDto(String name, String slogan, Double salePrice,Double vat, Double percent){
        this.name = name;
        this.slogan = slogan;
        this.salePrice = salePrice;
        this.vat = vat;
        this.percent = percent;
    }

    public BigDecimal getPrice(){
        return BigDecimal.valueOf(getPriceWithDiscount(false)).setScale(2, RoundingMode.FLOOR);
    }

    public BigDecimal getPriceIncludeVat(){
        return BigDecimal.valueOf(getPriceWithDiscount(true)).setScale(2, RoundingMode.FLOOR);
    }

    private Double getPriceWithDiscount(Boolean includeVat){
        return Optional.ofNullable(this.percent)
                .map(p -> getPriceWithVat(this.salePrice * (1D - p/100) , includeVat))
                .orElseGet(() -> getPriceWithVat(this.salePrice, includeVat));
    }

    private Double getPriceWithVat(Double price, Boolean includeVat){
        if(includeVat != null && includeVat)
            return price * (1D + this.vat);
        else
            return price;
    }
}
