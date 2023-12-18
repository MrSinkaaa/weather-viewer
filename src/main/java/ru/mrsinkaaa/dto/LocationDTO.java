package ru.mrsinkaaa.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LocationDTO {

    private int id;
    private String name;
    private int userId;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
