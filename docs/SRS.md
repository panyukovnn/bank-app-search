# Software Requirements Specification (SRS)

## Микросервис: `bank-app-search`

### 1. Назначение

Микросервис предназначен для обеспечения функционала единого поиска в клиентском приложении банка.

Данный микросервис является демонстрационным и имеет ограниченный функционал.

### 2. Ключевые внешние зависимости
*   **База данных (PostgreSQL):** Хранит страницы приложения и последние запросы поиска. Реализован функционал полнотекстового поиска с использованием триграмм (`pg_trgm`). 
*   **HTTP интеграция (запланирована, не реализована):** `operation-history` — смежная система для поиска переводов и платежей.

### 3. Структура Базы Данных

```sql
-- Расширение для полнотекстового триграммного поиска
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Страницы (экраны) поиска
CREATE TABLE page (
    id UUID PRIMARY KEY,
    name VARCHAR,                          -- Название (например: "Перевод по номеру")
    version VARCHAR,                       -- Версия приложения
    platform VARCHAR,                      -- Платформа: ios, android, web
    link VARCHAR,                          -- link на экран приложения
    dictionary VARCHAR,                    -- Текст для полнотекстового триграммного поиска
    top_result BOOLEAN,                    -- Флаг отображения в популярном
    create_time TIMESTAMP NOT NULL,
    create_user VARCHAR NOT NULL,
    last_update_time TIMESTAMP NOT NULL,
    last_update_user VARCHAR NOT NULL
);

-- Последние поисковые запросы пользователей (история)
CREATE TABLE latest_result (
    id UUID PRIMARY KEY,
    client_id VARCHAR,                     -- Уникальный идентификатор клиента
    search_string VARCHAR,                 -- Введенный пользователем текст
    create_time TIMESTAMP NOT NULL,
    create_user VARCHAR NOT NULL,
    last_update_time TIMESTAMP NOT NULL,
    last_update_user VARCHAR NOT NULL
);
```

### 4. Контракты запросов/ответов (API)

#### Входящие модели (Запросы от клиента)

**1. Поиск (`POST /api/v1/search`)**
```json
{
  "data": {
    "clientId": "9fb5188b-35d5-4a80-994f-0d158bd260bd",
    "searchString": "перевод по номеру",
    "platform": "ios",
    "clientVersion": "1.22.3",
    "searchTypes": [
      "PAGE", 
      "HISTORY"
    ],
    "size": 10
  }
}
```
**(Параметр `pagination` опционален и используется, если требуется догрузить следующую страницу конкретной сущности)*.*

**2. Подсказки экрана поиска (`POST /api/v1/search/suggested`)**
Запрашивается при клике на строку поиска.
```json
{
  "data": {
    "clientId": "9fb5188b-35d5-4a80-994f-0d158bd260bd",
    "platform": "android",
    "clientVersion": "1.25.0"
  }
}
```

#### Исходящие модели (Ответы клиенту)

**1. Ответ поиска (`SearchResponse`)** *(эндпоинт ещё не реализован)*
Содержит агрегированные результаты из БД и внешних сервисов.
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-05-15T12:00:00.000Z",
  "data": {
    "section": [
      {
        "entityType": "SECTION",
        "entityName": "Разделы",
        "pages": [
          {
            "name": "Перевод по номеру телефона",
            "link": "https://host/transfer/phone",
            "icon": "ic_transfer"
          }
        ]
      }
    ],
    "externalSection": [
      {
        "entityType": "OPERATION",
        "records": [
          {
            "id": "pay_123",
            "name": "Оплата мобильной связи",
            "categoryCode": "COMMUNICATION",
            "typeCode": "MOBILE",
            "icon": "ic_mobile"
          }
        ]
      },
      {
        "entityType": "HISTORY",
        "operations": [
          {
            "id": "txn_890",
            "name": "Перевод Олегу Т.",
            "amount": -500.0,
            "currency": "RUB",
            "date": "2024-05-14T18:30:00Z"
          }
        ]
      }
    ]
  }
}
```

**2. Ответ стартового экрана (`SuggestedResponse`)**
Содержит последние запросы клиента и популярные разделы банка.
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-05-15T12:00:00.000Z",
  "data": {
    "section": [
      {
        "entityType": "TOP",
        "entityName": "Популярное",
        "pages": [
          {
            "name": "Кредиты",
            "link": "credit/main"
          }
        ]
      }
    ],
    "latestSearch": [
      {
        "id": "uuid-1234",
        "searchString": "оплата жкх"
      }
    ]
  }
}
```

### 5. Детальная логика API-эндпоинтов

#### `POST /api/v1/search` — Выполнение поиска *(эндпоинт ещё не реализован)*
1. **Валидация клиента:** `clientId` передаётся в теле запроса (поле `data.clientId`).
2. **Параллельный опрос источников (Агрегация):**
  - **БД (Экраны и приложения):** Если в `searchTypes` есть `PAGE`, выполняется SQL-запрос `LIKE` (или `<->` триграммы) к страницам `page` по их полю `dictionary`. Исключаются страницы с неподдерживаемой версией клиента. Результаты ранжируются по релевантности.
  - **История операций:** Если запрошен `HISTORY`, отправляется HTTP-запрос в `operation-history`.
3. **Обновление недавних результатов:** Если запрос больше или равен 3 символам, он асинхронно записывается в таблицу `latest_result` под `clientId` клиента, а старые запросы этого же клиента удаляются в рамках ротации (хранятся только последние 10 запросов).
4. **Сортировка и агрегация:** Найденные элементы объединяются в единый DTO (`SearchSectionDto` и `ExternalSectionDto`), сортируются в нужном порядке и оборачиваются в `CommonResponse` для ответа.

#### `POST /api/v1/search/suggested` — Отображение экрана поиска
Вызывается в момент клика на элемент поиска (до ввода текста).
1. Из базы данных извлекаются записи `page`, у которых проставлен флаг `top_result = true`. Эти данные фильтруются под текущие `platform` и `clientVersion` клиента.
2. Параллельно идет запрос в таблицу `latest_result` для получения последних введенных поисковых строк текущего `clientId`.
3. Сформированные топы (популярное) и недавние поиски отправляются в блоках `section` и `latestSearch` соответственно.
