# PowerShell script for LOCAL services (APISIX in Docker, services on localhost)

$ADMIN_URL = if ($env:APISIX_ADMIN) { $env:APISIX_ADMIN } else { "http://localhost:9180/apisix/admin" }
$ADMIN_KEY = "myadminkey"

Write-Host "Creating upstreams and routes for LOCAL services via $ADMIN_URL" -ForegroundColor Green
Write-Host "Using host.docker.internal for upstream nodes (APISIX in Docker, services on localhost)..." -ForegroundColor Yellow

$headers = @{
    "X-API-KEY" = $ADMIN_KEY
    "Content-Type" = "application/json"
}

# Identity Service
$identityUpstream = @{
    type = "roundrobin"
    nodes = @{ "host.docker.internal:8081" = 1 }
} | ConvertTo-Json -Depth 3

$identityRoute = @{
    uris = @("/auth", "/auth/*")
    methods = @("GET", "POST")
    name = "identity-service-route"
    priority = 20
    upstream_id = "identity-service"
} | ConvertTo-Json -Depth 3

try {
    Invoke-RestMethod -Uri "$ADMIN_URL/upstreams/identity-service" -Method PUT -Headers $headers -Body $identityUpstream
    Invoke-RestMethod -Uri "$ADMIN_URL/routes/identity-service" -Method PUT -Headers $headers -Body $identityRoute
    Write-Host "✅ identity-service routes created" -ForegroundColor Green
} catch {
    Write-Host "❌ Error creating identity-service routes: $_" -ForegroundColor Red
}

# Master Data Service
$masterDataUpstream = @{
    type = "roundrobin"
    nodes = @{ "host.docker.internal:8084" = 1 }
} | ConvertTo-Json -Depth 3

$masterDataRoute = @{
    uris = @("/master-data", "/master-data/*")
    methods = @("GET", "POST", "PUT", "DELETE")
    name = "master-data-service-route"
    priority = 10
    upstream_id = "master-data-service"
} | ConvertTo-Json -Depth 3

try {
    Invoke-RestMethod -Uri "$ADMIN_URL/upstreams/master-data-service" -Method PUT -Headers $headers -Body $masterDataUpstream
    Invoke-RestMethod -Uri "$ADMIN_URL/routes/master-data-service" -Method PUT -Headers $headers -Body $masterDataRoute
    Write-Host "✅ master-data-service routes created" -ForegroundColor Green
} catch {
    Write-Host "❌ Error creating master-data-service routes: $_" -ForegroundColor Red
}

# CMS Service
$cmsUpstream = @{
    type = "roundrobin"
    nodes = @{ "host.docker.internal:8082" = 1 }
} | ConvertTo-Json -Depth 3

$cmsRoute = @{
    uris = @("/cms", "/cms/*")
    methods = @("GET")
    name = "cms-service-route"
    priority = 10
    upstream_id = "cms-service"
} | ConvertTo-Json -Depth 3

try {
    Invoke-RestMethod -Uri "$ADMIN_URL/upstreams/cms-service" -Method PUT -Headers $headers -Body $cmsUpstream
    Invoke-RestMethod -Uri "$ADMIN_URL/routes/cms-service" -Method PUT -Headers $headers -Body $cmsRoute
    Write-Host "✅ cms-service routes created" -ForegroundColor Green
} catch {
    Write-Host "❌ Error creating cms-service routes: $_" -ForegroundColor Red
}

# MPOS Service
$mposUpstream = @{
    type = "roundrobin"
    nodes = @{ "host.docker.internal:8083" = 1 }
} | ConvertTo-Json -Depth 3

$mposRoute = @{
    uris = @("/mpos", "/mpos/*")
    methods = @("GET")
    name = "mpos-service-route"
    upstream_id = "mpos-service"
} | ConvertTo-Json -Depth 3

try {
    Invoke-RestMethod -Uri "$ADMIN_URL/upstreams/mpos-service" -Method PUT -Headers $headers -Body $mposUpstream
    Invoke-RestMethod -Uri "$ADMIN_URL/routes/mpos-service" -Method PUT -Headers $headers -Body $mposRoute
    Write-Host "✅ mpos-service routes created" -ForegroundColor Green
} catch {
    Write-Host "❌ Error creating mpos-service routes: $_" -ForegroundColor Red
}

Write-Host ""
Write-Host "✅ Routes created successfully for LOCAL services!" -ForegroundColor Green
Write-Host "📋 Services registered:" -ForegroundColor Cyan
Write-Host "   - identity-service (Local: localhost:8081)"
Write-Host "   - master-data-service (Local: localhost:8084)" 
Write-Host "   - cms-service (Local: localhost:8082)"
Write-Host "   - mpos-service (Local: localhost:8083)"
Write-Host ""
Write-Host "🧪 Test with PowerShell:" -ForegroundColor Yellow
Write-Host 'Invoke-RestMethod -Uri "http://localhost:9080/auth/login" -Method POST -ContentType "application/json" -Body ''{"username":"admin@example.com","password":"password"}'''
Write-Host ""
Write-Host "⚠️  Make sure your services are running locally first:" -ForegroundColor Red
Write-Host "   curl http://localhost:8081/actuator/health  # identity-service"
Write-Host "   curl http://localhost:8082/actuator/health  # cms-service"
Write-Host "   curl http://localhost:8083/actuator/health  # mpos-service"
Write-Host "   curl http://localhost:8084/actuator/health  # master-data-service"