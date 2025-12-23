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
# Login (cred demo: admin/password) untuk dapat bearer token
curl -X POST http://localhost:9080/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'

# Gunakan token (default: dev-secret-token) ke endpoint user
curl http://localhost:9080/users \
  -H "Authorization: Bearer dev-secret-token"

# Contoh ambil detail order
curl http://localhost:9080/orders/101 \
  -H "Authorization: Bearer dev-secret-token"
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
