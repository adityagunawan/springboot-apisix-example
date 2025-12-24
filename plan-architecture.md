# Microservice Architecture – CMS & Mpos Platform

## 1. Tujuan Dokumen

Dokumen ini menjelaskan segmentasi microservice untuk aplikasi yang melayani:
- CMS Web Application
- Mpos Application

Dokumen ini digunakan sebagai:
- Dokumentasi arsitektur backend
- Acuan implementasi service
- Prompt untuk AI Code Generator (Codex / LLM)
- Referensi onboarding developer

---

## 2. Prinsip Arsitektur

1. Menggunakan **Backend For Frontend (BFF)** untuk CMS dan Mpos
2. **Client (CMS & Mpos) tidak pernah memanggil Core Service secara langsung**
3. Core Service bersifat **channel-agnostic**
4. **Entity domain hanya berada di Core Service**
5. BFF hanya berisi:
    - Controller publik
    - DTO
    - Orkestrasi
6. Komunikasi antar service menggunakan:
    - REST + Feign (synchronous)
    - Message Queue (asynchronous, opsional)
7. API Gateway (APISIX) sebagai pintu masuk utama

---

## 3. Tabel Segmentasi Microservice (CMS + Mpos)

### 3.1 Edge & Access Layer

| Nama Service         | Tipe | Fungsi Utama | Handle |
|----------------------|------|--------------|--------|
| api-gateway (APISIX) | Edge | Gerbang request | JWT verification, routing, rate limit, inject header |
| cms-bff-service      | BFF | Backend khusus CMS | Endpoint CMS, channel validation (CMS), orchestration |
| mpos-bff-service     | BFF | Backend khusus Mpos | Endpoint Mpos, channel validation (Mpos), response shaping |

---

### 3.2 Identity & Access Layer

| Nama Service | Tipe | Fungsi Utama | Handle |
|--------------|------|--------------|--------|
| identity-service | Core | Authentication & Authorization | Login CMS local, login AD, login Mpos, JWT & refresh token, role & permission, token config |

Catatan:
- Identity Service **hanya expose endpoint auth**
- Tidak expose data domain ke client

---

### 3.3 Core Domain Layer

| Nama Service | Tipe | Fungsi Utama | Handle |
|--------------|------|--------------|--------|
| master-data-service | Core | Data referensi | Parameter, lookup, static config |
| form-builder-service | Core | Definisi produk / form | CRUD form, field, validation, versioning |
| submission-service | Core | Data transaksi | Submit form, attachment metadata, status |
| workflow-service | Core | Approval engine | Approval rule, approval step, history |

Catatan:
- Semua Core Service **hanya expose endpoint internal**
- Tidak memiliki controller publik untuk client

---

### 3.4 Integration & Infrastructure Layer

| Nama Service | Tipe | Fungsi Utama | Handle |
|--------------|------|--------------|--------|
| integration-service | Infra | External system integration | Call external API, AD sync, async submit |
| notification-service (optional) | Infra | Notifikasi | Email, SMS, push notification |
| file-storage / object-storage | Infra | Penyimpanan file | Upload, download attachment |

---

## 4. Tanggung Jawab Endpoint (Public vs Internal)

### 4.1 Endpoint Publik (Client-facing)

| Service          | Endpoint Prefix | Dipanggil Oleh |
|------------------|-----------------|---------------|
| cms-bff-service  | `/cms/**` | CMS Web |
| mpos-bff-service | `/mpos/**` | mpos App |
| identity-service | `/auth/**` | CMS & Mpos |

---

### 4.2 Endpoint Internal (Private)

| Service | Endpoint Prefix | Akses |
|-------|-----------------|-------|
| master-data-service | `/internal/**` | Feign |
| form-builder-service | `/internal/**` | Feign |
| submission-service | `/internal/**` | Feign |
| workflow-service | `/internal/**` | Feign |

Catatan:
- Endpoint internal **tidak boleh diakses langsung oleh client**
- Dilindungi oleh network policy / service credential

---

## 5. Alur Request Standar

### 5.1 Mpos Load Data Master


---

## 6. Aturan Model & Entity

1. **Entity domain hanya berada di Core Service**
2. BFF **tidak memiliki entity JPA**
3. BFF menggunakan DTO / View Model sesuai kebutuhan channel
4. Tidak ada shared `common-model` antar service

Contoh:
- CMS BFF: `CmsProductCreateRequest`
- Mpos BFF: `MposProductView`
- Core Service: `Product` (Entity)

---

## 7. Aturan Keamanan

1. JWT issued oleh identity-service
2. Token mengandung:
    - user_id
    - user_type
    - channel
    - roles / permissions
3. Channel validation dilakukan di:
    - CMS BFF
    - Mpos BFF
4. Core Service tidak melakukan validasi channel

---

## 8. Anti-Pattern yang Harus Dihindari

- Client memanggil Core Service langsung
- Core Service expose endpoint publik
- Shared entity antar service
- External API dipanggil dari Core Domain Service
- BFF menyimpan data bisnis

---

## 9. Catatan Implementasi (Spring Boot)

- Setiap service adalah **Spring Boot Application terpisah**
- Antar service communication menggunakan **OpenFeign**
- Security menggunakan **Spring Security + JWT**
- API Gateway menggunakan **APISIX**
- Konfigurasi per service melalui `application.yml`

---

## 10. Penutup

Dokumen ini menjadi acuan baku untuk:
- Implementasi backend
- Review arsitektur
- Eksekusi otomatis oleh AI code generator
- Pengembangan berkelanjutan

Perubahan arsitektur harus memperbarui dokumen ini.
