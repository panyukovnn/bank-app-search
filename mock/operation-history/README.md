# Mock operation-history

## Запуск

```shell
java -jar ./lib/wiremock.jar --port 8081 --root-dir .
```

## Проверка

```shell
curl -s -X POST http://localhost:8081/api/v1/operations/search \
  -H 'Content-Type: application/json' \
  -d '{"clientId": "test", "searchString": "перевод", "size": 10}' | jq
```