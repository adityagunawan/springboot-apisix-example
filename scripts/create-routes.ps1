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

Put 'upstreams/identity-service' @{ type = 'roundrobin'; nodes = @{ 'identity-service:8081' = 1 } }

Put 'routes/identity-service' @{
  uris        = @('/auth', '/auth/*')
  methods     = @('GET', 'POST')
  name        = 'identity-service-route'
  priority    = 20
  upstream_id = 'identity-service'
}

Put 'upstreams/cms-service' @{ type = 'roundrobin'; nodes = @{ 'cms-service:8082' = 1 } }

Put 'routes/cms-service' @{
  uris        = @('/cms', '/cms/*')
  methods     = @('GET')
  name        = 'cms-service-route'
  priority    = 10
  upstream_id = 'cms-service'
}

Put 'upstreams/mpos-service' @{ type = 'roundrobin'; nodes = @{ 'mpos-service:8083' = 1 } }

Put 'routes/mpos-service' @{
  uris        = @('/mpos', '/mpos/*')
  methods     = @('GET')
  name        = 'mpos-service-route'
  priority    = 10
  upstream_id = 'mpos-service'
}

Write-Host "Routes created for identity-service, cms-service, and mpos-service."
