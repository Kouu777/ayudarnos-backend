# Ayudarnos.cl — Backend REST API

Backend desarrollado en **Spring Boot 4 / Java 21+** y **PostgreSQL** para la plataforma de donaciones de catástrofes y emergencias **Ayudarnos.cl**, basado en los requerimientos y maquetas de la plataforma.

---

## 🚀 Características Principales

1. **Autenticación y Registro Diferenciado**:
   - **Donantes (`ROLE_DONOR`)**: Registro con nombre, apellido, correo electrónico y contraseña.
   - **Organizaciones (`ROLE_ORGANIZATION`)**: Registro con datos institucionales (tipo: Municipalidad, Bomberos, Cruz Roja, ONG, Fundación; RUT, persona de contacto, teléfono, dirección, región y comuna).
   - Autenticación con **JWT (JSON Web Token)** y encriptación de contraseñas con **BCrypt**.

2. **Homepage y Catálogo de Necesidades Activas**:
   - Muestra las necesidades activas en emergencias y catástrofes con porcentaje de avance, metas, insumos requeridos y filtros por región, categoría y catástrofe.
   - Precargado con los datos de las maquetas:
     - *Kits de higiene niños* (Municipalidad de Concepción — Biobío) - **95%**
     - *Alimentos no perecibles familias evacuadas* (Bomberos de Chile, 3ª Cía Quilpué — Valparaíso) - **40%**
     - *Frazadas, colchones familias* (Cruz Roja Chilena, Temuco — Araucanía) - **77%**
     - *Agua potable damnificados* (Fundación Un Techo para Chile — Coquimbo) - **12%**

3. **Flujo de Donación ("Tu ayuda empieza aquí")**:
   - **Paso 1: ¿Qué vas a donar?**: Lista dinámica de productos con nombre, cantidad y presentación (ej: "Pack de 25 unidades", "Bolsas de 2 kg").
   - **Paso 2: Coordinación de entrega**: Modalidad presencial (`DROP_OFF`) o a convenir (`PICKUP_COORDINATION`), comuna y disponibilidad horaria.
   - **Paso 3: Datos de contacto**: Nombre completo, correo y teléfono (soporta donaciones públicas como invitado o vinculadas a cuenta de donante).
   - **Paso 4: Confirmación y Seguimiento**: Generación de un **código de seguimiento único** (ej: `AYUD-2026-X8K9P`) para consultar el avance del envío en cualquier momento.
   - **Gestión para Organizaciones**: Visualización de donaciones recibidas para sus necesidades y actualización de estado logístico (`PLEDGED` ➔ `COORDINATING` ➔ `RECEIVED` / `CANCELLED`).

---

## 🛠 Requisitos y Puesta en Marcha

### 1. Iniciar Base de Datos con Docker
El proyecto incluye un archivo `docker-compose.yml` listo para usar:
```bash
docker compose up -d
```
Esto levantará PostgreSQL 16 en el puerto `5432` con la base de datos `ayudarnos_db`.

### 2. Ejecutar la Aplicación Spring Boot
Para compilar y correr la aplicación:
```powershell
.\mvnw.cmd spring-boot:run
```
La API quedará escuchando en `http://localhost:8080`.

*(Nota: En modo pruebas o si no se cuenta con PostgreSQL activo, el sistema incluye soporte H2 in-memory para ejecución y tests inmediatos).*

### 3. Ejecutar los Tests
```powershell
.\mvnw.cmd test
```

---

## 🔑 Credenciales Precargadas para Pruebas

| Rol | Correo Electrónico | Contraseña | Entidad / Nombre |
|---|---|---|---|
| **ORGANIZATION** | `concepcion@ayudarnos.cl` | `password123` | Municipalidad de Concepción |
| **ORGANIZATION** | `quilpue@bomberos.cl` | `password123` | Bomberos de Chile, 3ª Cía Quilpué |
| **ORGANIZATION** | `temuco@cruzroja.cl` | `password123` | Cruz Roja Chilena, filial Temuco |
| **ORGANIZATION** | `coquimbo@techo.cl` | `password123` | Fundación Un Techo para Chile |
| **DONOR** | `pedro.jorquera@ejemplo.cl` | `password123` | Pedro Jorquera |
| **DONOR** | `camila.gonzalez@ejemplo.cl` | `password123` | Camila González |
| **ADMIN** | `admin@ayudarnos.cl` | `admin123` | Administrador General |

---

## 📡 Endpoints de la API REST

### 1. Autenticación (`/api/v1/auth`)
- `POST /api/v1/auth/register/donor`: Registro de donantes.
- `POST /api/v1/auth/register/organization`: Registro de organizaciones.
- `POST /api/v1/auth/login`: Inicio de sesión (retorna token Bearer JWT).
- `GET /api/v1/auth/me`: Perfil del usuario autenticado.

### 2. Necesidades Activas (`/api/v1/needs`)
- `GET /api/v1/needs`: Listado de necesidades activas (filtros opcionales: `region`, `category`, `status`, `catastropheId`).
- `GET /api/v1/needs/{id}`: Detalle de una necesidad con insumos requeridos.
- `POST /api/v1/needs`: Publicar necesidad (requiere rol `ORGANIZATION` o `ADMIN`).
- `PUT /api/v1/needs/{id}`: Actualizar necesidad.
- `DELETE /api/v1/needs/{id}`: Cancelar necesidad.

### 3. Catástrofes (`/api/v1/catastrophes`)
- `GET /api/v1/catastrophes`: Listado de catástrofes y emergencias activas.
- `POST /api/v1/catastrophes`: Registrar nueva catástrofe (`ADMIN`).

### 4. Donaciones e Intenciones de Ayuda (`/api/v1/donations`)
- `POST /api/v1/donations`: Enviar intención de donación (público o autenticado). Retorna el código de seguimiento `trackingCode`.
- `GET /api/v1/donations/track/{trackingCode}`: Consultar estado de la donación mediante el código de seguimiento.
- `GET /api/v1/donations/my-donations`: Historial de donaciones del donante en sesión (`DONOR`).
- `GET /api/v1/donations/need/{needId}`: Donaciones de una necesidad específica (`ORGANIZATION` dueña).
- `GET /api/v1/donations/organization/all`: Todas las donaciones recibidas por la organización.
- `PATCH /api/v1/donations/{id}/status`: Actualizar estado de donación (`COORDINATING`, `RECEIVED`, `CANCELLED`) con notas de coordinación.
