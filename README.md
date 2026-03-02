# Sistem Pengelolaan Aset dan Pemeliharaan Perusahaan 
Final Project Backend – Check Point 1

Backend REST API untuk pengelolaan aset dan aktivitas maintenance menggunakan Spring Boot.

---

## 📌 Check Point 1 Scope

Dokumentasi ini mencakup:

1. Analisa kebutuhan sistem  
2. Flow Business  
3. Desain Database  
4. Desain Relational Database (ERD)  
5. Database Migration  
6. Create Database  
7. Initial Data (Seed)

---

# 1️⃣ Analisa Kebutuhan Sistem

### 🎯 Tujuan Sistem

Sistem ini dibuat untuk membantu perusahaan mencatat:

- Data aset
- Aktivitas maintenance aset
- Data teknisi/user

Sehingga proses monitoring aset menjadi lebih terstruktur.

---

### 👥 Actor

| Role | Deskripsi |
|------|----------|
Admin | Mengelola data aset  
Technician | Melakukan maintenance aset  

---

### ⚙ Kebutuhan Fungsional

- Registrasi user
- Login user
- CRUD Asset
- Create Maintenance
- View Maintenance

---

### 🔐 Kebutuhan Non-Fungsional

- Java Spring Boot
- Hibernate JPA
- MySQL
- Autentikasi Basic Auth (sementara)
- Arsitektur Layered (Controller → Service → Repository)

---

# 2️⃣ Flow Business

Pada tahap ini, flow aplikasi berjalan sebagai berikut:

1. User / Technician melakukan request API  
2. Request melewati Security Filter  
3. Controller menerima request  
4. Service menjalankan business logic  
5. Repository mengakses database  
6. Response JSON dikirim ke client  

---

<img width="2141" height="738" alt="image" src="https://github.com/user-attachments/assets/e3090cbc-c88d-489f-a964-9e0c96214383" />

---

# 3️⃣ Desain Database
Database terdiri dari 3 tabel utama:

### 📄 users
| Field | Type |
|------|----------|
id | bigint 
name | varchar
email | varchar
password | varchar
role | varchar
created_at | datetime

### 📄 assets
| Field | Type |
|------|----------|
id | bigint 
asset_name | varchar
category | varchar
purchase_date | date
condition | varchar
status | varchar
created_at | datetime

### 📄 maintenances
| Field | Type |
|------|----------|
id | bigint 
asset_id | bigint (FK)
technician_id | bigint (FK)
maintenance_date | date
description | text
status | varchar
created_at | datetime

---

# 4️⃣ Desain Relational Database (ERD)
Relasi antar tabel:
- users → maintenances (One to Many)
- assets → maintenances (One to Many)
  
<img width="764" height="646" alt="Untitled" src="https://github.com/user-attachments/assets/4bc1d8c9-0bbb-4360-833e-34f8e53d740c" />

---

# 5️⃣ Database Migration
Migration dilakukan otomatis oleh Hibernate:
```
spring.jpa.hibernate.ddl-auto=update

```
Spring Boot akan:
- Membuat tabel
- Menyesuaikan kolom
- Membuat relasi
tanpa perlu SQL manual.

---

# 6️⃣ Create Database
Database dibuat menggunakan MySQL:
```
CREATE DATABASE asset_management;
```
Kemudian dikoneksikan melalui:
```
spring.datasource.url=jdbc:mysql://localhost:3306/asset_management
spring.datasource.username=root
spring.datasource.password=
```

---

# 7️⃣ Initial Data (Seed)
User pertama dibuat melalui endpoint:
```
POST /auth/register
```
Contoh payload:
```
{
  "name": "Arsyad",
  "email": "user@gmail.com",
  "password": "123456"
}
```
