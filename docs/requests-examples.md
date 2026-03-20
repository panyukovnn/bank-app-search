
## Suggested

Запрос с обязательными полями:
```shell
curl -X POST http://localhost:8080/bank-app-search/api/v1/search/suggested \
  -H 'Content-Type: application/json' \
  -d '{
    "data": {
      "clientId": "client-001",
      "platform": "ios"
    }
  }'
```

Запрос со всеми полями:
```shell
curl -X POST http://localhost:8080/bank-app-search/api/v1/search/suggested \
  -H 'Content-Type: application/json' \
  -d '{
    "data": {
      "clientId": "client-001",
      "platform": "android",
      "clientVersion": "5.12.0"
    }
  }'
```

Запрос без data (вернёт ошибку валидации):
```shell
curl -X POST http://localhost:8080/bank-app-search/api/v1/search/suggested \
  -H 'Content-Type: application/json' \
  -d '{}'
```

Запрос с пустыми обязательными полями (вернёт ошибку валидации):
```shell
curl -X POST http://localhost:8080/bank-app-search/api/v1/search/suggested \
  -H 'Content-Type: application/json' \
  -d '{
    "data": {
      "clientId": "",
      "platform": ""
    }
  }'
```