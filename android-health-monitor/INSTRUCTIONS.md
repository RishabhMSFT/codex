# Instructions: Build APK from Scratch to Deployment

This file gives an end-to-end guide to produce the **final APK** and deploy it.

## 1) Prerequisites

- OS: Linux/macOS/Windows
- JDK: **17**
- Android SDK (platform + build tools for API 34)
- Gradle (or Gradle wrapper, if added later)

### Required env vars

```bash
export JAVA_HOME=/path/to/jdk-17
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin
```

## 2) Clone and enter project

```bash
git clone <your-repo-url>
cd codex/android-health-monitor
```

## 3) Create `local.properties`

```properties
sdk.dir=/absolute/path/to/Android/Sdk
```

## 4) Build debug APK (quick test)

```bash
./scripts/build_apk.sh
```

Expected output APK:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## 5) Build release APK

```bash
gradle --no-daemon assembleRelease
```

Output:

```text
app/build/outputs/apk/release/app-release-unsigned.apk
```

## 6) Sign release APK (for production)

Generate keystore (one-time):

```bash
keytool -genkeypair \
  -v \
  -keystore release-keystore.jks \
  -alias health-monitor \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

Sign APK:

```bash
apksigner sign \
  --ks release-keystore.jks \
  --out app/build/outputs/apk/release/app-release-signed.apk \
  app/build/outputs/apk/release/app-release-unsigned.apk
```

Verify signature:

```bash
apksigner verify app/build/outputs/apk/release/app-release-signed.apk
```

## 7) Install APK to device

Enable USB debugging and connect device:

```bash
adb install -r app/build/outputs/apk/release/app-release-signed.apk
```

## 8) Server integration

Update endpoint in:

- `app/src/main/java/com/example/healthmonitor/service/HealthMonitorService.kt`

Replace `DEFAULT_SERVER_URL` with your backend endpoint.

## 9) Post-install validation checklist

- App opens and "Start Monitoring" starts foreground service.
- Persistent notification is visible.
- DB receives sampled metrics every minute.
- Hourly upload worker posts payload to server.
- Uploaded records are deleted locally after successful push.

## 10) Deployment options

- **Internal distribution:** send signed APK to QA/stakeholders.
- **Managed app stores/MDM:** upload signed artifact per org policy.
- **Google Play:** prefer App Bundle (AAB), but signed APK can still be used for limited/internal scenarios.

---

## Troubleshooting

### Plugin resolution fails

If Gradle cannot download Android plugin/dependencies, ensure internet access to:

- `https://dl.google.com`
- `https://repo.maven.apache.org`
- `https://plugins.gradle.org`

### SDK not found

Check `local.properties` and `ANDROID_HOME` path.

### Wrong Java version

Use JDK 17:

```bash
java -version
```

