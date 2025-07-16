package com.ren.orderingSystem.Mappers;

import com.ren.orderingSystem.ApiContracts.RequestDto.AddRestaurantAddressRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.UpdateRestaurantAddressRequest;
import com.ren.orderingSystem.ApiContracts.ResponseDto.RestaurantAddressInfoResponse;
import com.ren.orderingSystem.Entity.RestaurantAddress;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class RestaurantAddressMapper {

    public static RestaurantAddress toEntity(AddRestaurantAddressRequest newRestaurantAddress) {
        RestaurantAddress restaurantAddress = new RestaurantAddress();
        restaurantAddress.setCountry(newRestaurantAddress.getCountry());
        restaurantAddress.setCity(newRestaurantAddress.getCity());
        restaurantAddress.setStreetName(newRestaurantAddress.getStreetName());
        restaurantAddress.setStreetAddress(newRestaurantAddress.getStreetAddress());
        restaurantAddress.setPinCode(newRestaurantAddress.getPinCode());
        restaurantAddress.setProvince(newRestaurantAddress.getProvince());
        return restaurantAddress;
    }

    public static RestaurantAddressInfoResponse toResponseDto(RestaurantAddress restaurantAddress) {
        RestaurantAddressInfoResponse responseDto = new RestaurantAddressInfoResponse();
        responseDto.setRestaurantAddressId(restaurantAddress.getRestaurantAddressId());
        responseDto.setCountry(restaurantAddress.getCountry());
        responseDto.setCity(restaurantAddress.getCity());
        responseDto.setStreetName(restaurantAddress.getStreetName());
        responseDto.setStreetAddress(restaurantAddress.getStreetAddress());
        responseDto.setPinCode(restaurantAddress.getPinCode());
        responseDto.setProvince(restaurantAddress.getProvince());
        return responseDto;
    }

//    public static RestaurantAddress updateToEntity(Set<RestaurantAddress> restaurantAddress, UpdateRestaurantAddressRequest updateRestaurantAddressRequest) {
//        Optional<RestaurantAddress> exists = restaurantAddress.stream()
//                .filter(addr ->
//                        updateRestaurantAddressRequest.getRestaurantAddressId() != null &&
//                                updateRestaurantAddressRequest.getRestaurantAddressId().equals(addr.getRestaurantAddressId()))
//                .findFirst();
//
//        RestaurantAddress restaurantAddressEntity = exists.get();
//
//        if(updateRestaurantAddressRequest.getStreetAddress() != null && !updateRestaurantAddressRequest.getStreetAddress().isBlank()) {
//            restaurantAddressEntity.setStreetAddress(updateRestaurantAddressRequest.getStreetAddress());
//        }
//
//        if(updateRestaurantAddressRequest.getStreetName() != null && !updateRestaurantAddressRequest.getStreetName().isBlank()) {
//            restaurantAddressEntity.setStreetName(updateRestaurantAddressRequest.getStreetName());
//        }
//
//        if(updateRestaurantAddressRequest.getCity() != null && !updateRestaurantAddressRequest.getCity().isBlank()) {
//            restaurantAddressEntity.setCity(updateRestaurantAddressRequest.getCity());
//        }
//
//        if(updateRestaurantAddressRequest.getCountry() != null && !updateRestaurantAddressRequest.getCountry().isBlank()) {
//            restaurantAddressEntity.setCountry(updateRestaurantAddressRequest.getCountry());
//        }
//
//        if(updateRestaurantAddressRequest.getPinCode() != null && !updateRestaurantAddressRequest.getPinCode().isBlank()) {
//            restaurantAddressEntity.setPinCode(updateRestaurantAddressRequest.getPinCode());
//        }
//
//        if(updateRestaurantAddressRequest.getProvince() != null && !updateRestaurantAddressRequest.getProvince().isBlank()) {
//            restaurantAddressEntity.setProvince(updateRestaurantAddressRequest.getProvince());
//        }
//
//
//
//    }
}
