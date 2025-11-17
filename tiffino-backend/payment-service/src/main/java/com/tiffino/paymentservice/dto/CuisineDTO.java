package com.tiffino.paymentservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuisineDTO {
    private Long id;
    private String name;
    private String imageUrl;
    private Long stateId;
}
