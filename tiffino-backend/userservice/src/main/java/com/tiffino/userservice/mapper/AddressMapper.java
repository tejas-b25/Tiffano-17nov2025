package com.tiffino.userservice.mapper;

import com.tiffino.userservice.dto.AddressResponse;
import com.tiffino.userservice.entity.Address;

public class AddressMapper {
    public static AddressResponse toResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .pinCode(address.getPinCode())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .isDefault(address.getIsDefault())
                .addressType(address.getAddressType())
                .build();
    }
}
