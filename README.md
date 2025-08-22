# RecoApp

Aplicación Android para registrar, consultar y reportar residuos, con sincronización opcional a un backend Node.js/Express + PostgreSQL.

## 🧭 Índice
- Descripción general
- Arquitectura del proyecto
- App Android
- API/Servidor
- Desarrollo local (cómo correr todo)
- Configuración (env vars)
- Estilo/Temas y modo oscuro
- Pruebas
- Distribución (APK/AAB)
- Publicación en Play Store (resumen)
- Roadmap

Documentos relacionados:
- Diagramas ER: ./docs/er-diagrams.md
- Proceso manual vs digital: ./docs/manual-vs-digital.md

---

## 📝 Descripción general
RecoApp permite:
- Registrar residuos (tipo, cantidad, ubicación, fecha, comentario).
- Ver historial y generar reportes filtrados por tipo y rango de fecha.
- Sincronizar con un backend (pull/push) cuando hay conectividad.

Tecnologías clave:
- Android (Kotlin, ViewBinding, Material Components, Room, Coroutines).
- Compose habilitado para futuras pantallas (no bloqueante).
- Backend Node.js (Express), Sequelize y PostgreSQL.

---

## 🧱 Arquitectura del proyecto
```
recoapp-project/
├─ app/                    # Módulo Android
│  ├─ src/main/
│  │  ├─ java/com/example/recoapp/
│  │  │  ├─ MainActivity, LoginActivity, SignupActivity,
│  │  │  ├─ RegisterActivity, HistoryActivity, ReportsActivity
│  │  │  └─ data/ (Room: AppDatabase, DAO, entidades)
│  │  ├─ res/ (layouts, drawables, themes, colors)
│  │  └─ AndroidManifest.xml
│  └─ build.gradle.kts
├─ server/                 # Backend Express + Sequelize
│  ├─ src/
│  │  ├─ index.js          # Bootstrap Express + health + routers
│  │  ├─ lib/database.js   # Conexión Sequelize (PostgreSQL)
│  │  ├─ models/           # User, Waste
│  │  └─ routes/           # auth, sync
│  └─ package.json
├─ build.gradle.kts        # Gradle (raíz)
├─ settings.gradle.kts
└─ README.md
```

Comunicación app-backend (alto nivel):
- Autenticación: JWT via `/api/auth/login` y `/api/auth/register`.
- Sincronización: `/api/sync/wastes` con modelo de upsert por `modifiedAt`.

---

## 📱 App Android
- Min SDK 24, Target SDK 34.
- Tema: `Theme.RecoApp` basado en `MaterialComponents DayNight`.
- Dependencias principales: AppCompat, Material, Room (runtime/ktx/kapt), Retrofit + OkHttp, Lifecycle KTX, RecyclerView.
- ViewBinding activado; Compose habilitado para opt-in.

Pantallas relevantes:
- `LoginActivity`/`SignupActivity`: autenticación local contra backend.
- `MainActivity`: accesos a registro, historial, reportes, sync.
- `RegisterActivity`: alta de residuo.
- `HistoryActivity`: listado local (Room).
- `ReportsActivity`: filtros por tipo/fechas y resumen.

Datos locales:
- Room (`AppDatabase`) con entidades de residuos. DAO con filtros por rango de fechas y tipo.

Red/sync:
- Retrofit/OkHttp para API.
- Endpoints de sync: GET/POST `/api/sync/wastes`.

Build features:
- `viewBinding = true`
- `compose = true`

---

## 🌐 API/Servidor
Node.js (ESM) + Express + Sequelize + PostgreSQL.

- `src/index.js`: levanta Express, sincroniza DB, aplica CORS/JSON, health, routers.
- `src/lib/database.js`: conecta a `process.env.DATABASE_URL` (Postgres, SSL permitido) con `pg` como `dialectModule`.
- `src/models/User.js`: usuario con `email` único y `passwordHash` (bcryptjs).
- `src/models/Waste.js`: residuos con campos `id`, `type`, `quantity`, `location`, `date`, `comment`, `deleted`, `modifiedAt` y `index(modifiedAt)`.
- `src/routes/auth.js`:
  - `POST /api/auth/register` — alta de usuario, retorna JWT.
  - `POST /api/auth/login` — login, retorna JWT.
- `src/routes/sync.js`:
  - `GET /api/sync/wastes?since=<ts>` — delta por `modifiedAt`.
  - `POST /api/sync/wastes` — upsert por lote, ignora si `modifiedAt` es más viejo.

Scripts:
- `npm run dev` (nodemon)
- `npm start`
- `npm run db:init` (opcional según implementación)

---

## 🛠️ Desarrollo local
Requisitos:
- Android Studio Iguana+ y JDK 17 (Gradle Wrapper maneja la build).
- Node.js 18+ y PostgreSQL 14+.

1) Backend
- Crear `.env` en `server/` con:
```
DATABASE_URL=postgres://USER:PASS@HOST:5432/DBNAME
JWT_SECRET=pon_un_secreto_fuerte
PORT=3000
```
- Instalar deps y arrancar:
```
cd server
npm install
npm run dev
```
- Healthcheck: http://localhost:3000/health

2) App Android
- Abrir `recoapp-project/` en Android Studio.
- Editar `app/src/main/res/values/strings.xml` para apuntar a la URL del backend si aplica.
- Ejecutar en emulador/dispositivo (Build Variant: debug).

---

## ⚙️ Configuración
Gradle (app):
- `compileSdk = 34`, `targetSdk = 34`, `minSdk = 24`.
- Room con KAPT; Coroutines; Retrofit+Gson; OkHttp logging en debug.

Manifest:
- `android.permission.INTERNET` habilitado.
- `android:usesCleartextTraffic="true"` (si se usa HTTP, ideal mover a HTTPS en prod).

---

## 🎨 Estilo, temas y modo oscuro
- Tema base: `Theme.MaterialComponents.DayNight.NoActionBar`.
- Mapeos en `values/themes.xml`:
  - `colorSurface` -> `@color/surface`
  - `colorOnSurface` -> `@color/on_surface`
  - `colorBackground` / `android:colorBackground` -> `@color/background`
- Paleta light (`values/colors.xml`):
  - `background` #F5F5F5, `surface` #FFFFFF, `on_surface` #212121, `primary` #4CAF50
- Paleta dark (`values-night/colors.xml`):
  - `background` #121212, `surface` #1E1E1E, `on_surface` #FFFFFF

Ajustes de layouts:
- Raíces usan `?attr/colorBackground`.
- Tarjetas/áreas usan `?attr/colorSurface` y texto `?attr/colorOnSurface`.
- Toolbar usa `?attr/colorPrimary` con `titleTextColor=?attr/colorOnPrimary`.

---

## 🧪 Pruebas
- Unit: JUnit 4 para lógica de negocio.
- Instrumented: AndroidX Test + Espresso (placeholders incluidos).
- Backend: se recomienda supertest para endpoints (no incluido aún).

Ejecutar pruebas Android: desde Android Studio (panel de tests) o Gradle tasks.

---

## 📦 Distribución
- Debug APK: Build > Build APK(s).
- Release AAB (recomendado para Play):
  - Configurar firma (App Signing): keystore y `signingConfig` en `build.gradle.kts` o Play App Signing.
  - Build > Generate Signed Bundle/APK > Android App Bundle.

ProGuard/R8: reglas en `app/proguard-rules.pro` (minify desactivado por ahora en release).

---

## 🚀 Publicación en Play Store (resumen corto)
1) Crea cuenta en Google Play Console y un “app listing”.
2) Sube un AAB firmado (release) y completa la ficha: nombre, descripción, capturas, ícono, clasificaciones.
3) Define políticas, contenido y pruebas; habilita “Testing” interno/abierto si deseas.
4) Envía para revisión y despliegue gradual.

Sugerencias:
- Usa versionCode/Name incrementales por release.
- Activa Crashlytics/Analytics en futuras iteraciones.
- Revisa el target API level y permisos antes de enviar.

---

## 🗺️ Roadmap
- Pantallas 100% Compose.
- Sincronización offline-first con colas y reintentos.
- Tests de integración backend con supertest y seed DB.
- Autenticación en endpoints de sync mediante middleware JWT.
- Theming dinámico (Material You) en Android 12+.
