
# Сервис OTP-кодов

## Описание

Данный сервис предоставляет возможность защиты операций с помощью временных кодов (OTP).  
Основные функции:
- Регистрация и аутентификация пользователей (JWT).
- Генерация и отправка OTP-кодов через SMS (эмулятор SMPP), Email (JavaMail), Telegram (бот).
- Валидация OTP-кодов.
- Административное управление конфигурацией OTP (длина, время жизни).
- Логи операций.
- Сохранение OTP-кодов в файл.

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

## Установка и запуск

1. Склонируйте репозиторий:
   ```bash
   git clone <URL вашего репозитория>
   cd otp-service
   ```

2. Настройте базу данных PostgreSQL:
   - Создайте базу данных `otpdb`.
   - Выполните SQL-скрипт для создания таблиц (см. `schema.sql`).

3. В файле `src/main/resources/email.properties` пропишите настройки вашей почты.
4. В файле `src/main/resources/sms.properties` укажите настройки SMPP эмулятора (localhost:2775).
5. В файле `src/main/resources/telegram.properties` пропишите токен бота и chatId.

6. Соберите проект:
   ```bash
   ./gradlew build
   ```

7. Запустите приложение:
   ```bash
   ./gradlew run
   ```

Приложение запустится на порту 8000.

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

## Логи

Логи пишутся в консоль. Для настройки используйте `logback.xml` или другую предпочитаемую библиотеку.
