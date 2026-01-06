#!/usr/bin/env bash
set -euo pipefail

ADMIN_URL="${APISIX_ADMIN:-http://localhost:9180/apisix/admin}"
ADMIN_KEY="myadminkey"

echo "Creating upstreams and routes via ${ADMIN_URL}"

curl -sSf -X PUT "$ADMIN_URL/upstreams/identity-service" \
  -H "X-API-KEY: ${ADMIN_KEY}" \
  -H "Content-Type: application/json" \
  -d @- <<JSON
{
  "type": "roundrobin",
  "nodes": { "identity-service:8081": 1 }
}
JSON

curl -sSf -X PUT "$ADMIN_URL/routes/identity-service" \
  -H "X-API-KEY: ${ADMIN_KEY}" \
  -H "Content-Type: application/json" \
  -d @- <<JSON
{
  "uris": ["/auth", "/auth/*"],
  "methods": ["GET", "POST"],
  "name": "identity-service-route",
  "priority": 20,
  "upstream_id": "identity-service"
}
JSON

curl -sSf -X PUT "$ADMIN_URL/upstreams/cms-service" \
  -H "X-API-KEY: ${ADMIN_KEY}" \
  -H "Content-Type: application/json" \
  -d @- <<JSON
{
  "type": "roundrobin",
  "nodes": { "cms-service:8082": 1 }
}
JSON

curl -sSf -X PUT "$ADMIN_URL/routes/cms-service" \
  -H "X-API-KEY: ${ADMIN_KEY}" \
  -H "Content-Type: application/json" \
  -d @- <<JSON
{
  "uris": ["/cms", "/cms/*"],
  "methods": ["GET"],
  "name": "cms-service-route",
  "priority": 10,
  "upstream_id": "cms-service"
}
JSON

curl -sSf -X PUT "$ADMIN_URL/upstreams/mpos-service" \
  -H "X-API-KEY: ${ADMIN_KEY}" \
  -H "Content-Type: application/json" \
  -d @- <<JSON
{
  "type": "roundrobin",
  "nodes": { "mpos-service:8083": 1 }
}
JSON

curl -sSf -X PUT "$ADMIN_URL/routes/mpos-service" \
  -H "X-API-KEY: ${ADMIN_KEY}" \
  -H "Content-Type: application/json" \
  -d @- <<JSON
{
  "uris": ["/mpos", "/mpos/*"],
  "methods": ["GET"],
  "name": "mpos-service-route",
  "upstream_id": "mpos-service"
}
JSON

echo "Routes created for identity-service, cms-service, and mpos-service."
