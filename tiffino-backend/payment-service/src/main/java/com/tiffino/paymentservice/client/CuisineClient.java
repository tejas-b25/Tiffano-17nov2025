package com.tiffino.paymentservice.client;

import com.tiffino.paymentservice.dto.CuisineDTO;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.*;

@FeignClient(name = "MENU-SERVICE")

public interface CuisineClient {

    @GetMapping("/cuisines/{id}")

    CuisineDTO getCuisineById(@PathVariable("id") Long cuisineId);

}



