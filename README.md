# LOGGING FRAMEWORK

This framework is used for automatically logging requests and responses with details.

## Installation

Using maven to install. (It is required maven installed)

```bash
mvn clean install
```

## Usage

Use annotation for automatically logging.
```bash
@EnableKachingLogging
```

## Model logging request
```bash
API_REQUEST_LOG:
{
  "appCode": "kaching",
  "timeStamp": "2021-08-22 21:13:05.389",
  "type": "kaching",
  "serviceId": null,
  "requestType": "GET",
  "msgUid": "cc184972-2f04-4061-9259-be2b7f89f972",
  "clientIp": "127.0.0.1",
  "statusCode": null,
  "errorClassification": null,
  "errorType": null,
  "executionTime": null,
  "requestPath": null,
  "traceId": "e88189ca-c579-4d14-bea6-6bb72e9cbe9b",
  "status": null,
  "userId": null
}
```

## Model logging response
```bash
API_RESPONSE_LOG:
{
  "appCode": "kaching",
  "timeStamp": "2021-08-22 21:13:05.440",
  "type": "kaching",
  "serviceId": null,
  "requestType": "GET",
  "msgUid": "dff0662e-7cf0-4078-8560-f1dc0a4fc3b4",
  "clientIp": "127.0.0.1",
  "statusCode": "200",
  "errorClassification": null,
  "errorType": "Failed to process request",
  "executionTime": 58,
  "requestPath": null,
  "traceId": "e88189ca-c579-4d14-bea6-6bb72e9cbe9b",
  "status": "SUCCESS",
  "userId": null
}
```