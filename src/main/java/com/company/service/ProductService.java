package com.company.service;

import com.company.common.BaseEvent;
import com.company.common.PageResponse;
import com.company.model.dto.OrderDto;
import com.company.model.dto.request.ProductRequest;
import com.company.model.dto.response.ProductResponse;

import java.math.BigDecimal;

public interface ProductService {

    void addProduct(ProductRequest request);

    PageResponse<ProductResponse> getAllProduct(int page, int size);

    ProductResponse getProduct(Long id);

    ProductResponse updateStock(Long id, int quantity);

    BigDecimal getProductPriceById(Long id);

    void processOrderCreated(BaseEvent<OrderDto> event);
}
