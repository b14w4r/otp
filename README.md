## Структура проекта

- `src/main/java/com/promoit`  
  - `model` — классы сущностей (User, OTPConfig, OTPCode)  
  - `dao` — DAO-классы для работы с PostgreSQL через JDBC  
  - `service` — бизнес-логика (UserService, OTPService, NotificationService)  
  - `handler` — HTTP-хендлеры, реализующие API (регистрация, логин, OTP)  
  - `util` — утилитарные классы (JWTUtil, DBConnection)  
  - `Main.java` — точка входа, запускает HTTP-сервер  

- `src/main/resources`  
  - `email.properties` — конфигурация почты  
  - `sms.properties` — конфигурация SMPP эмулятора  
  - `telegram.properties` — конфигурация Telegram-бота  

## Требования

- Java 17
- PostgreSQL 17
- Gradle

## API

### Публичное
- `POST /api/register` — регистрация нового пользователя.
- `POST /api/login` — вход пользователя, возвращает JWT.

### Администратор
- `GET /api/admin/users` — получить список всех пользователей (кроме админов).
- `DELETE /api/admin/users/{id}` — удалить пользователя и все его OTP-коды.
- `PUT /api/admin/config` — изменить конфигурацию OTP (JSON: `{ "codeLength": 6, "ttlSeconds": 300 }`).

### Пользователь
- `POST /api/otp/generate` — генерирует OTP для операции. Тело запроса: `{ "operationId": "123", "channel": "email", "destination": "user@example.com" }`.
- `POST /api/otp/validate` — проверяет OTP. Тело запроса: `{ "operationId": "123", "code": "123456" }`.

Все защищенные эндпоинты требуют заголовок `Authorization: Bearer <token>`.

## Тестирование

- Для SMS используйте SMPPSim эмулятор: запустите `startsmppsim.bat`, настройте `sms.properties`.
- Для Email настройте рабочий SMTP в `email.properties`.
- Для Telegram создайте бота через @BotFather, настройте `telegram.properties`.


