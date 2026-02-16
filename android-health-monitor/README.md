# Android Health Monitor

Android app that continuously monitors memory health metrics and uploads them every hour.

## Features

- Foreground service for persistent metric collection.
- Samples metrics every minute:
  - App used memory
  - App max memory
  - System available memory
  - System total memory
  - Low-memory flag
- Stores samples in Room DB.
- Uploads last 1-hour metrics to server via POST.
- Deletes uploaded samples after successful push.
- Restarts monitoring after reboot.

## Project structure

- `app/src/main/java/com/example/healthmonitor/service/HealthMonitorService.kt` – continuous sampler + periodic upload scheduling.
- `app/src/main/java/com/example/healthmonitor/worker/HourlyUploadWorker.kt` – hourly upload + cleanup.
- `app/src/main/java/com/example/healthmonitor/data/` – Room entities/DAO/repository.
- `app/src/main/java/com/example/healthmonitor/network/MetricsApiClient.kt` – HTTP client for metrics push.
- `INSTRUCTIONS.md` – full setup/build/sign/deploy guide from scratch.
- `scripts/build_apk.sh` – convenience build script.

## Build & deployment

Please follow:

- [`INSTRUCTIONS.md`](./INSTRUCTIONS.md)

This includes all steps from environment setup to producing/signing the final APK and deployment.

## Important note on Android behavior

Android does not permit truly hidden forever-running background apps. This app uses the supported, policy-compliant model:

1. Foreground service (visible ongoing notification)
2. WorkManager for periodic background uploads

## Payload example

```json
{
  "collectedAt": 1711111111111,
  "metrics": [
    {
      "timestampMs": 1711111111000,
      "appUsedMemoryMb": 120,
      "appMaxMemoryMb": 256,
      "systemAvailMemoryMb": 2048,
      "systemTotalMemoryMb": 8192,
      "lowMemory": false
    }
  ]
}
```

