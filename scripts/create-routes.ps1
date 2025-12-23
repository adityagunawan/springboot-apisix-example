$ErrorActionPreference = 'Stop'

$adminUrl = if ($env:APISIX_ADMIN) { $env:APISIX_ADMIN } else { 'http://localhost:9180/apisix/admin' }
$adminKey = 'myadminkey'

function Put($path, $body) {
  Invoke-RestMethod `
    -Method Put `
    -Uri "$adminUrl/$path" `
    -Headers @{ 'X-API-KEY' = $adminKey } `
    -ContentType 'application/json' `
    -Body ($body | ConvertTo-Json -Depth 5)
}

Write-Host "Creating upstreams and routes via $adminUrl"

Put 'upstreams/user-service' @{ type = 'roundrobin'; nodes = @{ 'user-service:8081' = 1 } }

Put 'routes/user-service' @{
  uris        = @('/users', '/users/*', '/profile')
  methods     = @('GET')
  name        = 'user-service-route'
  priority    = 10
  upstream_id = 'user-service'
}

Put 'routes/user-login' @{
  uris        = @('/login')
  name        = 'user-login-route'
  methods     = @('POST')
  priority    = 20
  upstream_id = 'user-service'
}

Put 'upstreams/order-service' @{ type = 'roundrobin'; nodes = @{ 'order-service:8082' = 1 } }

Put 'routes/order-service' @{
  uris        = @('/orders', '/orders/*')
  methods     = @('GET', 'POST', 'PUT', 'DELETE')
  name        = 'order-service-route'
  upstream_id = 'order-service'
}

Write-Host "Routes created for user-service and order-service."
