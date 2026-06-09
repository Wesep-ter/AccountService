package com.example.accountservice.producer;

import com.example.accountservice.dto.event.TransferResultEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountTransferProducer {

    private final KafkaTemplate<String, Object> accountKafkaTemplate;

    public void sendTransferResult(TransferResultEvent resultEvent) {
        log.info("Отправка финального статуса ({}) для транзакции {} в топик 'transfer-results'",
                resultEvent.getStatus(), resultEvent.getTransactionId());
        accountKafkaTemplate.send("transfer-results", resultEvent.getTransactionId(), resultEvent);
    }
}
