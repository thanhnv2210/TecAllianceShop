package com.tecalliance.shop.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer articleId;
    @Temporal(TemporalType.DATE)
    Date fromDate;
    @Temporal(TemporalType.DATE)
    Date toDate;
    Double percent;
}
