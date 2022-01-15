package com.tecalliance.shop.repository;

import com.tecalliance.shop.dto.DiscountArticleDto;
import com.tecalliance.shop.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    @Query(value = "select d from Discount d where d.articleId = ?1 and d.fromDate <= ?2 and d.toDate >= ?2 order by d.percent desc")
    List<Discount> listDiscountOrderByPercentDesc(Integer id, Date at);
    @Query(value = "select new com.tecalliance.shop.dto.DiscountArticleDto(art.name, art.slogan, art.salePrice, art.vat, MAX(d.percent))" +
            "from Article art left join Discount d on d.articleId = art.id and d.fromDate <= ?1 and d.toDate >= ?1" +
            "group by art.id")
    List<DiscountArticleDto> listArticleWithDiscount(Date at);//, Integer page, Integer size);
}
