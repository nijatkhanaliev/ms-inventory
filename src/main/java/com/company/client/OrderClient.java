package com.company.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ORDER-SERVICE", url = "http://localhost:8086/api/v1/orders")
public interface OrderClient {

    @PostMapping("/{id}/cancel")
    void cancelOrder(@RequestHeader("current-user-id") Long userId,
                     @PathVariable Long id);

}
