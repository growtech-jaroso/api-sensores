#!/bin/bash
set -e

# Lista de variables obligatorias
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
)

# Verificar que todas las variables obligatorias estén definidas
for env_var in "${REQUIRED_VARS[@]}"; do
  if [ -z "${!env_var}" ]; then
    echo "❌ The environment variable $env_var is required but is not set."
    exit 1
  fi
done

echo "✅ All ok, app is ready to run"

# Ejecuta el comando original (Java en este caso)
exec "$@"