package com.company.messaging;

import com.company.client.OrderClient;
import com.company.model.events.PaymentFailedEvent;
import com.company.service.impl.StockEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.company.config.RabbitMQConfig.PAYMENT_FAILED_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentFailedConsumer {

    private final OrderClient orderClient;
    private final StockEventPublisher stockEventPublisher;

    @RabbitListener(queues = PAYMENT_FAILED_QUEUE)
    private void consumePaymentFailed(PaymentFailedEvent event) {
        log.info("ORDER.CREATED.PAYMENT.FAILED, orderId {}, reason '{}'",
                event.getOrderId(), event.getReason());

        stockEventPublisher.handlePaymentFailed(event);
    }

}
