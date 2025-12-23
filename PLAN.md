# Rencana Pengembangan Lanjutan (Spring Boot + APISIX Boilerplate)

## 1) Basis Data & Migrasi
- Ganti H2 dengan Postgres untuk environment non-dev; gunakan profile `dev` (H2) vs `prod` (Postgres).
- Tambah migrasi skema dengan Flyway (schema versi users, orders).
- Tambah pooling config & observability DB (metrics).

## 2) Auth & Security
- Bangun user store (tabel users) dengan password hashing (BCrypt), roles/claims.
- Implement refresh token + rotation; blacklisting/expiry check.
- Perketat JWT filter: clock skew, key rotation, audience/issuer check.
- Gateway hardening: kunci APISIX admin, IP allowlist, rate limiting (plugin), CORS ketat.

## 3) API & Dokumentasi
- Tambah springdoc-openapi di tiap service; expose `/swagger-ui.html` + `/v3/api-docs`.
- Siapkan Swagger UI terpisah (static) dengan `urls` array ke kedua spec, atau agregator bila perlu.
- Tambah versioning API (`/api/v1`), naming dan konsistensi DTO/response.

## 4) Testing
- Kembalikan/unit-test service (user & order): service layer, web slice (MockMvc), validasi.
- Integration test dengan DB nyata (Postgres testcontainer) & Flyway.
- Smoke test via APISIX untuk route utama (login/users/orders CRUD).
- Pertimbangkan contract tests antar service jika ada inter-service call.

## 5) Observability
- Micrometer + Prometheus endpoint, dashboards dasar.
- OpenTelemetry tracing (HTTP server, DB), log enrichment (traceId).
- Audit log untuk event auth/login dan perubahan data.

## 6) Gateway & Infra
- Jadikan definisi route/policy APISIX deklaratif (file/ IaC) alih-alih skrip imperatif.
- Tambah plugin: rate limit, circuit breaker/retries, request/response size limit, mTLS upstream jika perlu.
- Persistensi etcd sudah ada; pastikan backup/restore strategi.

## 7) Resiliensi & Client
- Set timeout/retry/circuit breaker untuk HTTP client antar service (Resilience4j).
- Validasi input ketat, sanitasi, dan batas ukuran payload.

## 8) Build, CI/CD, Container
- Tambah pipeline (GitHub Actions/GitLab CI): lint/test/build jar, build & scan image (Trivy/Snyk), push registry.
- Gunakan Maven Wrapper; pin versi plugin.
- Hardening container: base image slim, `USER` non-root, healthcheck, JVM opts via env.

## 9) Data & Seed
- Pisahkan seed `dev` vs `prod`; gunakan Flyway callback atau profile-based data load.
- Tambah sample data minimal untuk demo, tapi non-prod only.

## 10) Governance & Praktik
- Konsisten dengan standard response (sudah ada), tambahkan kode error domain.
- Logging terstruktur (JSON) untuk produksi.
- Dokumentasi ops: cara rotate key JWT/APISIX admin, backup etcd, restore DB, runbook incident.
