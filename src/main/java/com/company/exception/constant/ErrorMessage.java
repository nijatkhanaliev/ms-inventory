package com.company.exception.constant;

public interface ErrorMessage {
    String IN_SUFFICIENT_STOCK_MESSAGE = "Not enough stock available. Requested: %d, Available: %d, Product ID: %d";
    String DATA_NOT_FOUND_MESSAGE = "Data not found";
    String INVALID_ORDER_ITEMS_MESSAGE = "order items is invalid";
}
