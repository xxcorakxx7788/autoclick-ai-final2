#!/usr/bin/env sh
set -e
# If gradle is installed via setup-gradle action, use it
if command -v gradle > /dev/null 2>&1; then
  exec gradle "$@"
else
  echo "Gradle not found"
  exit 1
fi
