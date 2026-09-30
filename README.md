# account-service

Микросервис управления банковскими счетами.
Участвует в Kafka-саге переводов между счетами, взаимодействует с `card-service` через Kafka.

---

## Стек

- Spring Boot
- Spring Web (MVC)
- Spring Data JPA
- Spring Kafka (producer + consumer)
- PostgreSQL
- Lombok
- Maven

---

## API

| Метод | URL | Описание |
|---|---|---|
| GET | `/api/account/{id}` | Получить счёт по ID |
| POST | `/api/account` | Открыть счёт |
| POST | `/api/account/{id}/execution-balance` | Изменить баланс |
| PUT | `/api/account/changeStatus/{id}` | Сменить статус |
| PATCH | `/api/account/close/{id}` | Закрыть счёт |

**`AccountData` (открытие счёта):**
```json
{
  "userId": 1,
  "accountHolder": "IVAN IVANOV",
  "accountType": "CHECKING",
  "currency": "RUB"
}
```

**`AccountDto` (ответ):**
```json
{
  "id": 1,
  "userId": 1,
  "accountType": "CHECKING",
  "accountNumber": "40817810123456789012",
  "accountHolder": "IVAN IVANOV",
  "currency": "RUB",
  "balance": 0.00,
  "status": "ACTIVE",
  "statusChangeTime": null
}
```

**`BalanceUpdateRequest` (изменение баланса):**
```json
{
  "amount": 100.00,
  "currency": "RUB",
  "transactionId": "tx-123"
}
```

---

## Kafka

| Топик | Направление | Событие |
|---|---|---|
| `card-validated-events` | consume | `CardValidatedEvent` |
| `transfer-results` | produce | `TransferResultEvent` |

Consumer group: `account-service-group`.

`CardValidatedConsumer`:
1. Слушает `card-validated-events`.
2. Вызывает `accountService.executeMoneyTransfer(source, target, amount)`.
3. Публикует `TransferResultEvent` со статусом `SUCCESS` или `FAILED` в `transfer-results`.

`AccountTransferProducer` отправляет результат в топик `transfer-results`.

---

## Бизнес-логика

**Перевод (`executeMoneyTransfer`):**
- Списание с `sourceAccountId` (`updateBalance(source, -amount)`).
- Зачисление на `targetAccountId` (`updateBalance(target, +amount)`).
- Оба вызова — в одной `@Transactional`.

**`updateBalance`:**
- Загружает счёт с `PESSIMISTIC_WRITE` (`findByIdForUpdate`).
- Проверяет, что счёт не `BLOCKED`.
- Если новый баланс < 0 — `NotEnoughMoneyException`.
- Сохраняет `AuditLog`.

**`closeAccount`:**
- Запрещено, если баланс > 0 (`CloseAccountException`).
- Ставит `Status.CLOSED`, `statusChangeTime = now()`.

**`openAccount`:**
- Генерирует номер счёта через `GenerateAccountNumber`.
- Ставит `balance = 0`, `status = ACTIVE`.

**`changeStatus`:**
- Меняет `status` и `statusChangeTime`.

---

## Генерация номера счёта

`GenerateAccountNumberImpl`:
- Формат: `balanceCode` + `currencyCode` + `key` + `bikBranch` + `personalAccount`.
- `balanceCode` — из `AccountType` (40817, 42301, 42601, 45502).
- `currencyCode` — из `Currency` ISO (RUB=810, USD=840, EUR=978).
- `key` — по весам `{7,1,3,...}` от последних 3 цифр `bik-branch` + raw-счёта.
- `personalAccount` — 7 случайных цифр.

`bank.bik-branch` берётся из конфига (`BIK_BRANCH`).

---

## Сущности

### `Account` (таблица `accounts`)

| Поле | Тип |
|---|---|
| id | BIGSERIAL |
| user_id | BIGINT |
| account_number | VARCHAR |
| account_holder | VARCHAR |
| account_type | VARCHAR (enum) |
| balance | NUMERIC(19,2), default 0.00 |
| currency | VARCHAR (enum) |
| open_date | DATE |
| status | VARCHAR (enum) |
| closed_at | TIMESTAMP |

`@DynamicInsert` — в INSERT попадают только не-null поля.

### `AuditLog` (таблица `audit_logs`)

| Поле | Тип |
|---|---|
| id | SERIAL |
| account_id | BIGINT |
| amount | NUMERIC |
| created_at | TIMESTAMP |

### Enum
- `AccountType`: CHECKING (40817), SAVINGS (42301), DEPOSIT (42601), CREDIT (45502)
- `Currency`: RUB (810), USD (840), EUR (978)
- `Status`: BLOCKED, ACTIVE, CLOSED

---

## Обработка исключений

`GlobalExceptionHandler` обрабатывает:
- `AccountNotFoundException` → 400
- `BlockedAccountException` → 400
- `CloseAccountException` → 400
- `NotEnoughMoneyException` → 400

---

## Конфигурация

`application.yaml`:
```yaml
server:
  port: 7082
spring:
  datasource:
    url: jdbc:postgresql://localhost:5429/AccountDB
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: account-service-group
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: "*"
    producer:
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
bank:
  bik-branch: ${BIK_BRANCH}
```

---

## Запуск

Требуется:
- PostgreSQL на `localhost:5429`, БД `AccountDB`
- Kafka на `localhost:9092`

```bash
./mvnw spring-boot:run
```

Порт сервиса: `7082`.

---

## Структура

```
com.example.accountservice/
├── AccountServiceApplication.java
├── config/
│   ├── AccountProducerConfig.java
│   └── ApplicationConfig.java        # пустой класс
├── constant/
│   ├── AccountType.java
│   ├── Currency.java
│   └── Status.java
├── consumer/
│   └── CardValidatedConsumer.java
├── controller/
│   └── AccountController.java
├── dto/
│   ├── AccountData.java
│   ├── AccountDto.java
│   ├── BalanceUpdateRequest.java
│   ├── event/
│   │   ├── CardValidatedEvent.java
│   │   └── TransferResultEvent.java
│   └── mapper/
│       └── AccountMapper.java
├── entity/
│   ├── Account.java
│   └── AuditLog.java
├── exception/
│   ├── AccountNotFoundException.java
│   ├── BlockedAccountException.java
│   ├── CloseAccountException.java
│   └── NotEnoughMoneyException.java
├── handler/
│   └── GlobalExceptionHandler.java
├── producer/
│   └── AccountTransferProducer.java
├── repositories/
│   ├── AccountRepository.java
│   └── AuditLogRepository.java
├── service/
│   ├── AccountService.java
│   └── AccountServiceImpl.java
└── util/
    ├── GenerateAccountNumber.java
    └── GenerateAccountNumberImpl.java
```

---

## Тесты

`DemoApplicationTests` — проверка загрузки контекста (`@SpringBootTest`).

---

## Автор

Wesep-ter
