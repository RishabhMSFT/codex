#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT_DIR"

if [[ -z "${JAVA_HOME:-}" ]]; then
  echo "[ERROR] JAVA_HOME is not set. Use JDK 17 (recommended)."
  exit 1
fi

if ! command -v gradle >/dev/null 2>&1 && [[ ! -x "./gradlew" ]]; then
  echo "[ERROR] Neither gradle nor ./gradlew is available."
  exit 1
fi

GRADLE_CMD="gradle"
if [[ -x "./gradlew" ]]; then
  GRADLE_CMD="./gradlew"
fi

echo "[INFO] Building debug APK..."
"$GRADLE_CMD" --no-daemon assembleDebug

APK_PATH="$ROOT_DIR/app/build/outputs/apk/debug/app-debug.apk"
if [[ -f "$APK_PATH" ]]; then
  echo "[SUCCESS] APK generated: $APK_PATH"
else
  echo "[ERROR] Build command finished but APK not found at: $APK_PATH"
  exit 1
fi
