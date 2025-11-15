package com.company.messaging;

import com.company.common.BaseEvent;
import com.company.model.dto.OrderDto;
import com.company.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.company.model.constant.RabbitConstant.STOCK_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private final ProductService productService;

    @RabbitListener(queues = STOCK_QUEUE)
    private void handleOrderCreated(BaseEvent<OrderDto> event) {
        productService.processOrderCreated(event);
    }

}
