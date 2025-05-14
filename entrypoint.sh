#!/bin/bash
set -e

# Required environment variables
REQUIRED_VARS=(
"MONGO_USERNAME"
"MONGO_PASSWORD"
"MONGO_HOST"
"MONGO_PORT"
"MONGO_DATABASE"
"MONGO_AUTH_DATABASE"
"JWT_EXP_TIME"
"JWT_SECRET"
"ALLOWED_ORIGINS"
"MQTT_BROKER_URL"
"MQTT_CLIENT_ID"
"MQTT_PASSWORD"
)

# Check if required environment variables are set
for env_var in "${REQUIRED_VARS[@]}"; do
  if [ -z "${!env_var}" ]; then
    echo "❌ The environment variable $env_var is required but is not set."
    exit 1
  fi
done

echo "✅ All ok, app is ready to run"

# Exec the command passed to the entrypoint
exec "$@"