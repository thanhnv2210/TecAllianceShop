package com.tecalliance.shop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tecalliance.shop.entity.Discount;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class DiscountDto {
    Integer id;
    Integer articleId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "fromDate is required")
    Date fromDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "toDate is required")
    Date toDate;
    @NotNull(message = "percent is required")
    Double percent;

    public DiscountDto(Date fromDate,Date toDate,Double percent){
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.percent = percent;
    }

    public static DiscountDto fromEntity(Discount entity) {
        DiscountDto result = new DiscountDto();
        BeanUtils.copyProperties(entity, result);
        return result;
    }
}
