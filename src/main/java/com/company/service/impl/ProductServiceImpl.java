package com.company.service.impl;

import com.company.dao.entity.Product;
import com.company.dao.repository.ProductRepository;
import com.company.exception.InsufficientStockException;
import com.company.exception.NotFoundException;
import com.company.model.dto.PageResponse;
import com.company.model.dto.request.ProductRequest;
import com.company.model.dto.response.ProductResponse;
import com.company.model.mapper.ProductMapper;
import com.company.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.company.exception.constant.ErrorCode.DATA_NOT_FOUND;
import static com.company.exception.constant.ErrorCode.IN_SUFFICIENT_STOCK;
import static com.company.exception.constant.ErrorMessage.DATA_NOT_FOUND_MESSAGE;
import static com.company.exception.constant.ErrorMessage.IN_SUFFICIENT_STOCK_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public void addProduct(ProductRequest request) {
        log.info("Adding product, productName {}", request.getName());
        Product product = productMapper.toProduct(request);
        productRepository.save(product);
    }

    @Override
    public PageResponse<ProductResponse> getAllProduct(int page, int size) {
        log.info("Getting all product");
        Pageable pageable = PageRequest.of(page, size);

        Page<Product> allProduct = productRepository.findAll(pageable);
        List<ProductResponse> productResponses = productMapper.toProductResponses(allProduct.getContent());

        PageResponse<ProductResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(productResponses);
        pageResponse.setNumber(allProduct.getNumber());
        pageResponse.setSize(allProduct.getSize());
        pageResponse.setTotalElements(allProduct.getTotalElements());
        pageResponse.setTotalPages(allProduct.getTotalPages());
        pageResponse.setLast(allProduct.isLast());
        pageResponse.setFirst(allProduct.isFirst());

        return pageResponse;
    }

    @Override
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND_MESSAGE, DATA_NOT_FOUND));

        return productMapper.toProductResponse(product);
    }

    @Override
    public ProductResponse updateStock(Long id, int newStock) {
        log.info("Updating stock, productId {}, quantity {}", id, newStock);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND_MESSAGE, DATA_NOT_FOUND));

        if (newStock < 0) {
            throw new InsufficientStockException(
                    String.format(IN_SUFFICIENT_STOCK_MESSAGE,
                            newStock, product.getStock(), product.getId()),
                    IN_SUFFICIENT_STOCK
            );
        }

        if (newStock == 0) {
            product.setActive(false);
        }

        product.setStock(newStock);
        productRepository.save(product);

        return productMapper.toProductResponse(product);
    }

    @Override
    public BigDecimal getProductPriceById(Long id) {
        log.info("Getting product price, productId {}", id);
        return productRepository.findPriceById(id)
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND_MESSAGE, DATA_NOT_FOUND));
    }


}
