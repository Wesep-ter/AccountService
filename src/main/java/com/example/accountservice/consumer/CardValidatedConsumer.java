package com.example.accountservice.consumer;

import com.example.accountservice.dto.event.CardValidatedEvent;
import com.example.accountservice.dto.event.TransferResultEvent;
import com.example.accountservice.producer.AccountTransferProducer;
import com.example.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CardValidatedConsumer {

    private final AccountService accountService;
    private final AccountTransferProducer accountTransferProducer;

    @KafkaListener
    public void handleCardValidatedEvent(CardValidatedEvent event) {
        log.info("Получены проверенные данные карт. Обработка транзакции: {}", event.getTransactionId());

        try {
            accountService.executeMoneyTransfer(
                    event.getSourceAccountId(),
                    event.getTargetAccountId(),
                    event.getAmount()
            );

            TransferResultEvent successResult = TransferResultEvent.builder()
                    .transactionId(event.getTransactionId())
                    .status("SUCCESS")
                    .build();

            accountTransferProducer.sendTransferResult(successResult);

        } catch (Exception e) {
            log.error("Финансовая операция отклонена для транзакции {}: {}", event.getTransactionId(), e.getMessage());

            TransferResultEvent failedResult = TransferResultEvent.builder()
                    .transactionId(event.getTransactionId())
                    .status("FAILED")
                    .errorMessage(e.getMessage())
                    .build();

            accountTransferProducer.sendTransferResult(failedResult);
        }
    }
}
