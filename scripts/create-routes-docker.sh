#!/usr/bin/env bash
set -euo pipefail

ADMIN_URL="${APISIX_ADMIN:-http://localhost:9180/apisix/admin}"
ADMIN_KEY="myadminkey"

echo "Creating upstreams and routes for FULL DOCKER deployment via ${ADMIN_URL}"
echo "Using Docker service names for upstream nodes..."

# Identity Service
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

# Master Data Service
curl -sSf -X PUT "$ADMIN_URL/upstreams/master-data-service" \
  -H "X-API-KEY: ${ADMIN_KEY}" \
  -H "Content-Type: application/json" \
  -d @- <<JSON
{
  "type": "roundrobin",
  "nodes": { "master-data-service:8084": 1 }
}
JSON

curl -sSf -X PUT "$ADMIN_URL/routes/master-data-service" \
  -H "X-API-KEY: ${ADMIN_KEY}" \
  -H "Content-Type: application/json" \
  -d @- <<JSON
{
  "uris": ["/master-data", "/master-data/*"],
  "methods": ["GET", "POST", "PUT", "DELETE"],
  "name": "master-data-service-route",
  "priority": 10,
  "upstream_id": "master-data-service"
}
JSON

# CMS Service
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

# MPOS Service
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

echo ""
echo "✅ Routes created successfully for FULL DOCKER deployment!"
echo "📋 Services registered:"
echo "   - identity-service (Docker: identity-service:8081)"
echo "   - master-data-service (Docker: master-data-service:8084)"
echo "   - cms-service (Docker: cms-service:8082)"
echo "   - mpos-service (Docker: mpos-service:8083)"
echo ""
echo "🧪 Test with: curl -X POST http://localhost:9080/auth/login -H 'Content-Type: application/json' -d '{\"username\":\"admin@example.com\",\"password\":\"password\"}'"