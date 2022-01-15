package com.tecalliance.shop.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Optional;

@Entity
@Data
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    String slogan;
    Double netPrice;
    Double salePrice;
    Double vat;

    public Double getMaxDiscountPercent() {
        return (salePrice - netPrice) / salePrice * 100;
    }

}
