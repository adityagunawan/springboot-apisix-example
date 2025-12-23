#!/usr/bin/env bash
set -euo pipefail

ADMIN_URL="${APISIX_ADMIN:-http://localhost:9180/apisix/admin}"
ADMIN_KEY="myadminkey"

echo "Creating upstreams and routes via ${ADMIN_URL}"

curl -sSf -X PUT "$ADMIN_URL/upstreams/user-service" \
  -H "X-API-KEY: ${ADMIN_KEY}" \
  -H "Content-Type: application/json" \
  -d @- <<JSON
{
  "type": "roundrobin",
  "nodes": { "user-service:8081": 1 }
}
JSON

curl -sSf -X PUT "$ADMIN_URL/routes/user-service" \
  -H "X-API-KEY: ${ADMIN_KEY}" \
  -H "Content-Type: application/json" \
  -d @- <<JSON
{
  "uris": ["/users", "/users/*", "/profile"],
  "methods": ["GET"],
  "name": "user-service-route",
  "priority": 10,
  "upstream_id": "user-service"
}
JSON

curl -sSf -X PUT "$ADMIN_URL/routes/user-login" \
  -H "X-API-KEY: ${ADMIN_KEY}" \
  -H "Content-Type: application/json" \
  -d @- <<JSON
{
  "uris": ["/login"],
  "name": "user-login-route",
  "methods": ["POST"],
  "priority": 20,
  "upstream_id": "user-service"
}
JSON

curl -sSf -X PUT "$ADMIN_URL/upstreams/order-service" \
  -H "X-API-KEY: ${ADMIN_KEY}" \
  -H "Content-Type: application/json" \
  -d @- <<JSON
{
  "type": "roundrobin",
  "nodes": { "order-service:8082": 1 }
}
JSON

curl -sSf -X PUT "$ADMIN_URL/routes/order-service" \
  -H "X-API-KEY: ${ADMIN_KEY}" \
  -H "Content-Type: application/json" \
  -d @- <<JSON
{
  "uris": ["/orders", "/orders/*"],
  "methods": ["GET"],
  "name": "order-service-route",
  "upstream_id": "order-service"
}
JSON

echo "Routes created for user-service and order-service."
