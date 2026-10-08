package com.takeOut.food.api.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class StatisticsOrderDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date dateTime;

    private double moneys;

    private Integer orders;

    private List<Date> dateList;
}
