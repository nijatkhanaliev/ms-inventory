package com.company.controller;

import com.company.common.PageResponse;
import com.company.model.dto.request.ProductRequest;
import com.company.model.dto.response.ProductResponse;
import com.company.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Void> addProduct(@Valid @RequestBody ProductRequest request) {
        productService.addProduct(request);
        return ResponseEntity.status(CREATED)
                .build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getAllProduct(
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(productService.getAllProduct(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductResponse> updateStock(@PathVariable Long id, @RequestParam int quantity) {
        return ResponseEntity.ok(productService.updateStock(id, quantity));
    }

    @GetMapping("/{id}/price")
    public ResponseEntity<BigDecimal> getProductPriceById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductPriceById(id));
    }

}
