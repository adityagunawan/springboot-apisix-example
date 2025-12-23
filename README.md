# Spring Boot + APISIX Boilerplate

Proyek contoh dua microservice Spring Boot (user-service & order-service) di belakang API Gateway Apache APISIX menggunakan Docker Compose.

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
   - Bash/WSL: `./scripts/create-routes.sh`  
   - PowerShell: `pwsh -File .\\scripts\\create-routes.ps1`
   - Admin URL default: `http://localhost:9180/apisix/admin`
   - Admin key default: `myadminkey` (lihat `apisix/config.yaml`)

4) Uji cepat lewat gateway (port 9080)
```bash
# Login (cred demo: admin/password) untuk dapat JWT
curl -X POST http://localhost:9080/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'
# response contoh:
# { "token": "<jwt>", "type": "bearer", "expiresInSeconds": 3600,
#   "user": { "username": "admin", "email": "admin@example.com", "fullName": "Admin User", "organization": "Example Corp" } }

# Gunakan JWT ke endpoint user
curl http://localhost:9080/users \
  -H "Authorization: Bearer <jwt>"

# Contoh ambil detail order
curl http://localhost:9080/orders/101 \
  -H "Authorization: Bearer <jwt>"

# Profil current user dari token
curl http://localhost:9080/profile \
  -H "Authorization: Bearer <jwt>"
```

## Menjalankan service langsung (opsional, tanpa Docker)
```bash
# Terminal 1
cd services/user-service
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Terminal 2
cd services/order-service
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
Gunakan env var bila perlu:
```bash
SERVER_PORT=8081 API_TOKEN=dev-secret-token mvn spring-boot:run
```
Gateway APISIX tetap harus dijalankan via Docker agar routing berjalan, lalu jalankan `scripts/create-routes.sh` seperti di atas.

## Struktur penting
- `docker-compose.yml` — orkestrasi APISIX, etcd, dan kedua service.
- `apisix/config.yaml` — konfigurasi APISIX admin & koneksi etcd.
- `scripts/create-routes.sh` — registrasi upstream/route di APISIX.
- `services/user-service`, `services/order-service` — kode Spring Boot.

## Catatan
- Header `Authorization: Bearer <token>` wajib untuk semua endpoint selain `/login` dan `/actuator`.
- Ganti `API_TOKEN` di `docker-compose.yml` bila perlu; sesuaikan `create-routes.sh` jika mengubah admin URL/key.
- Port etcd tidak diekspos ke host (menghindari bentrok port 2379 Windows); APISIX mengakses langsung lewat jaringan internal Docker.
- JWT secret default: `JWT_SECRET=dev-jwt-secret-change-me-please-32-chars` (set di `docker-compose.yml`); gunakan nilai minimal 32 karakter untuk HS256.
