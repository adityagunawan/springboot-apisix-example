#!/usr/bin/env bash
set -euo pipefail

ADMIN_URL="${APISIX_ADMIN:-http://localhost:9180/apisix/admin}"
ADMIN_KEY="myadminkey"

echo "Creating upstreams and routes for LOCAL services via ${ADMIN_URL}"
echo "Using host.docker.internal for upstream nodes (APISIX in Docker, services on localhost)..."

# Identity Service
curl -sSf -X PUT "$ADMIN_URL/upstreams/identity-service" \
  -H "X-API-KEY: ${ADMIN_KEY}" \
  -H "Content-Type: application/json" \
  -d @- <<JSON
{
  "type": "roundrobin",
  "nodes": { "host.docker.internal:8081": 1 }
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
  "nodes": { "host.docker.internal:8084": 1 }
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
  "nodes": { "host.docker.internal:8082": 1 }
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
  "nodes": { "host.docker.internal:8083": 1 }
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
echo "✅ Routes created successfully for LOCAL services!"
echo "📋 Services registered:"
echo "   - identity-service (Local: localhost:8081)"
echo "   - master-data-service (Local: localhost:8084)"
echo "   - cms-service (Local: localhost:8082)"
echo "   - mpos-service (Local: localhost:8083)"
echo ""
echo "🧪 Test with: curl -X POST http://localhost:9080/auth/login -H 'Content-Type: application/json' -d '{\"username\":\"admin@example.com\",\"password\":\"password\"}'"
echo ""
echo "⚠️  Make sure your services are running locally first:"
echo "   curl http://localhost:8081/actuator/health  # identity-service"
echo "   curl http://localhost:8082/actuator/health  # cms-service"
echo "   curl http://localhost:8083/actuator/health  # mpos-service"
echo "   curl http://localhost:8084/actuator/health  # master-data-service"