package com.tiffino.menuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CityDTO
{
    private Long cityId;
    private String cityName;
    private Long stateId;
}
