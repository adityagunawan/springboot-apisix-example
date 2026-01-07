# Spring Boot + APISIX Boilerplate

microservice Spring Boot (identity-service, cms-service, mpos-service) di belakang API Gateway Apache APISIX menggunakan Docker Compose.

## Prasyarat
- Docker & Docker Compose.
- Cara mendaftarkan route:
  - Unix/Git Bash/WSL: `scripts/create-routes.sh`
  - PowerShell: `scripts/create-routes.ps1`
- Java 21 & Maven hanya jika ingin menjalankan service tanpa Docker.

## Menjalankan dengan Docker Compose
1) Jalankan seluruh stack
```bash
docker compose up -d --build
# atau: docker-compose up -d --build
```
2) Pastikan container jalan
```bash
docker compose ps
```
3) Daftarkan upstream & route di APISIX
   **Untuk deployment FULL DOCKER (semua service di Docker):**
   - Bash/WSL: `./scripts/create-routes-docker.sh`  
   - PowerShell: `pwsh -File .\scripts\create-routes-docker.ps1`

   **Untuk deployment MIXED (service lokal + APISIX Docker):**
   - Bash/WSL: `./scripts/create-routes-local.sh`
   - PowerShell: `pwsh -File .\scripts\create-routes-local.ps1`

   - Admin URL default: `http://localhost:9180/apisix/admin`
   - Admin key default: `myadminkey` (lihat `apisix/config.yaml`)

4) Uji cepat lewat gateway (port 9080)
```bash
# Login (cred demo: admin/password) untuk dapat JWT
curl -X POST http://localhost:9080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'
# response contoh:
# { "token": "<jwt>", "type": "bearer", "expiresInSeconds": 3600,
#   "user": { "username": "admin", "email": "admin@example.com", "fullName": "Admin User", "organization": "Example Corp" } }

# Gunakan JWT ke endpoint cms
curl http://localhost:9080/cms/items \
  -H "Authorization: Bearer <jwt>"

# Contoh ambil data transaksi mpos
curl http://localhost:9080/mpos/transactions \
  -H "Authorization: Bearer <jwt>"

# Profil current user dari token
curl http://localhost:9080/auth/profile \
  -H "Authorization: Bearer <jwt>"

```

## Menjalankan service langsung (opsional, tanpa Docker)

### Prasyarat
- Java 21 & Maven terinstall
- Docker untuk menjalankan APISIX + etcd

### Langkah-langkah:

#### 1. Jalankan APISIX + etcd via Docker
```bash
# Hanya jalankan APISIX dan etcd, tanpa service Spring Boot
docker compose up -d etcd apisix
```

#### 2. Jalankan master-data-service (port 8084)
```bash
# Terminal 1 - Master Data Service (harus jalan dulu)
cd services/master-data-service
mvn spring-boot:run

# Atau dengan environment variable custom:
# SERVER_PORT=8084 mvn spring-boot:run
```

#### 3. Jalankan identity-service (port 8081)  
```bash
# Terminal 2 - Identity Service
cd services/identity-service
mvn spring-boot:run

# Dengan custom config:
# SERVER_PORT=8081 API_TOKEN=dev-secret-token MASTER_DATA_SERVICE_URL=http://localhost:8084 mvn spring-boot:run
```

#### 4. Jalankan cms-service (port 8082)
```bash
# Terminal 3 - CMS Service  
cd services/cms-service
mvn spring-boot:run

# Dengan custom config:
# SERVER_PORT=8082 API_TOKEN=dev-secret-token mvn spring-boot:run
```

#### 5. Jalankan mpos-service (port 8083)
```bash
# Terminal 4 - MPOS Service
cd services/mpos-service  
mvn spring-boot:run

# Dengan custom config:
# SERVER_PORT=8083 API_TOKEN=dev-secret-token mvn spring-boot:run
```

#### 6. Daftarkan route di APISIX
```bash
# Untuk service LOKAL + APISIX Docker, gunakan script khusus:
./scripts/create-routes-local.sh

# Atau PowerShell:
# pwsh -File .\scripts\create-routes-local.ps1

# JANGAN gunakan create-routes-docker.sh karena itu untuk full Docker!
```

#### 7. Test koneksi
```bash
# Cek semua service aktif
curl http://localhost:8081/actuator/health  # identity-service
curl http://localhost:8082/actuator/health  # cms-service  
curl http://localhost:8083/actuator/health  # mpos-service
curl http://localhost:8084/actuator/health  # master-data-service

# Test login via gateway APISIX (port 9080)
curl -X POST http://localhost:9080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'
```

### Catatan Penting:
- **Urutan start service penting**: master-data-service harus jalan dulu sebelum identity-service
- Service akan berjalan di port default: master-data (8084), identity (8081), cms (8082), mpos (8083)  
- Gateway APISIX tetap di port 9080 untuk akses eksternal
- Pastikan tidak ada konflik port dengan aplikasi lain

## Struktur penting
- `docker-compose.yml` — orkestrasi APISIX, etcd, dan keempat service.
- `apisix/config.yaml` — konfigurasi APISIX admin & koneksi etcd.
- `scripts/create-routes-docker.sh|.ps1` — route script untuk full Docker deployment.
- `scripts/create-routes-local.sh|.ps1` — route script untuk service lokal + APISIX Docker.
- `scripts/create-routes.sh|.ps1` — script lama (sama dengan docker version).
- `services/identity-service`, `services/cms-service`, `services/mpos-service`, `services/master-data-service` — kode Spring Boot.

## Catatan
- Header `Authorization: Bearer <token>` wajib untuk semua endpoint selain `/auth/login` dan `/actuator`.
- Ganti `API_TOKEN` di `docker-compose.yml` bila perlu; sesuaikan `create-routes.sh` jika mengubah admin URL/key.
- Port etcd tidak diekspos ke host (menghindari bentrok port 2379 Windows); APISIX mengakses langsung lewat jaringan internal Docker.
- JWT secret default: `JWT_SECRET=dev-jwt-secret-change-me-please-32-chars` (set di `docker-compose.yml`); gunakan nilai minimal 32 karakter untuk HS256.
- Ketiga service kini memakai H2 in-memory + Spring Data JPA; data awal ada di `src/main/resources/data.sql`.
- Untuk debugging service secara terpisah, Anda bisa hanya menyalakan APISIX + etcd dengan `docker compose up -d etcd apisix`, jalankan service Spring Boot secara lokal (mvn spring-boot:run), lalu jalankan `scripts/create-routes-local.sh`.

## Pemilihan Script Route

| Deployment Mode | Script yang Digunakan | Keterangan |
|-----------------|----------------------|------------|
| **Full Docker** | `create-routes-docker.sh/.ps1` | Semua service berjalan di Docker |
| **Mixed (Lokal + APISIX Docker)** | `create-routes-local.sh/.ps1` | Service lokal, APISIX di Docker |
| **Legacy** | `create-routes.sh/.ps1` | Sama dengan docker version |

### ⚠️ Penting:
- **Jangan salah pilih script** - akan menyebabkan error 503
- **Full Docker**: gunakan service name (`identity-service:8081`)
- **Mixed Mode**: gunakan `host.docker.internal:8081`
