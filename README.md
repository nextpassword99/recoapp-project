# RecoApp - Sistema de Gestión de Residuos Sólidos

## 📱 Descripción del Proyecto

**RecoApp** es una aplicación móvil desarrollada para **ECOLIM S.A.C.** que digitaliza el proceso de recolección y registro de residuos sólidos, reemplazando el manejo manual en papel y hojas de cálculo por un sistema digital eficiente y confiable.

### 🎯 Objetivo General
Desarrollar un aplicativo móvil que permita registrar residuos sólidos en tiempo real, asegurando:
- ✅ Registro digital validado
- ✅ Reportes automáticos
- ✅ Trazabilidad de datos
- ✅ Cumplimiento de normativas ambientales
- ✅ Optimización del proceso manual actual

## 🏗️ Arquitectura del Proyecto

### Tecnologías Utilizadas
- **Lenguaje:** Kotlin (compatible con Java)
- **Framework:** Android SDK
- **UI:** XML Layouts con Material Design
- **Base de Datos:** SQLite con Room Database
- **Arquitectura:** MVVM (Model-View-ViewModel)
- **Navegación:** Navigation Component con Navigation Drawer
- **Async:** Kotlin Coroutines con LiveData

### Estructura del Proyecto
```
app/src/main/java/com/example/recoapp/
├── data/
│   ├── entity/           # Entidades de base de datos
│   ├── dao/             # Data Access Objects
│   ├── database/        # Configuración Room Database
│   ├── repository/      # Patrón Repository
│   └── converter/       # Convertidores de tipos
├── ui/
│   ├── home/           # Pantalla de inicio
│   ├── register/       # Registro de residuos
│   ├── history/        # Historial con RecyclerView
│   └── reports/        # Reportes y estadísticas
└── MainActivity.kt     # Actividad principal
```

## 📊 Modelo de Datos

### Entidad Principal: `Residuo`
```kotlin
@Entity(tableName = "residuos")
data class Residuo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tipo: String,           // Tipo de residuo
    val cantidad: Double,       // Cantidad en kilogramos
    val ubicacion: String,      // Ubicación de recolección
    val fecha: Date,           // Fecha y hora
    val comentarios: String    // Comentarios adicionales
)
```

### Tipos de Residuo Soportados
- 🥬 **Residuos Orgánicos**
- ♻️ **Residuos Reciclables**
- ⚠️ **Residuos Peligrosos**
- 🔌 **Residuos Electrónicos**
- 🗑️ **Residuos Generales**

## 🚀 Funcionalidades Principales

### 1. 🏠 Pantalla de Bienvenida
- Pantalla inicial con información de la aplicación
- Estadísticas rápidas de residuos registrados
- Navegación intuitiva mediante Navigation Drawer

### 2. 📝 Formulario de Registro de Residuos
- **Tipo de residuo:** Lista desplegable con validación
- **Cantidad:** Campo numérico con validación (kg)
- **Ubicación:** Campo de texto para la ubicación
- **Fecha:** DatePicker (no permite fechas futuras)
- **Comentarios:** Campo opcional de texto largo
- **Validaciones completas** con mensajes de error
- **Escaneo de código** (preparado para implementación futura)

### 3. 📚 Historial de Residuos
- **RecyclerView** con renderizado eficiente usando DiffUtil
- **Búsqueda en tiempo real** por tipo, ubicación o comentarios
- **Indicadores de color** por tipo de residuo
- **Acciones CRUD:** Editar y eliminar mediante long press
- **Estadísticas dinámicas** en la parte superior
- **Estado vacío** cuando no hay registros

### 4. 📊 Reportes y Estadísticas
- **Filtros avanzados:**
  - Por tipo de residuo
  - Por rango de fechas
  - Combinación de filtros
- **Estadísticas en tiempo real:**
  - Total de registros
  - Peso total recolectado
  - Desglose por tipo de residuo
- **Visualización de datos** con cards de Material Design
- **Preparado para exportación** de reportes

## 🔧 Operaciones CRUD Completas

### Create (Crear)
```kotlin
suspend fun insertResiduo(residuo: Residuo): Long
```

### Read (Leer)
```kotlin
fun getAllResiduos(): LiveData<List<Residuo>>
suspend fun getResiduoById(id: Long): Residuo?
```

### Update (Actualizar)
```kotlin
suspend fun updateResiduo(residuo: Residuo)
```

### Delete (Eliminar)
```kotlin
suspend fun deleteResiduo(residuo: Residuo)
```

## 📱 Navegación y UX

### Navigation Drawer
- **Inicio:** Pantalla principal con estadísticas
- **Registrar Residuo:** Formulario de nuevo registro
- **Historial:** Lista completa con búsqueda
- **Reportes:** Estadísticas y filtros
- **Configuración:** Opciones de la aplicación

### Material Design
- Componentes consistentes de Material Design 3
- Tema verde acorde a la temática ambiental
- Iconografía clara y comprensible
- Feedback visual para todas las acciones

## 🔒 Permisos Android

El archivo `AndroidManifest.xml` incluye los permisos necesarios:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

## 🛠️ Instalación y Configuración

### Prerrequisitos
- Android Studio Arctic Fox o superior
- JDK 8 o superior
- Android SDK API 24+ (Android 7.0)
- Gradle 7.0+

### Pasos de Instalación

1. **Clonar el repositorio:**
```bash
git clone https://github.com/nextpassword99/recoapp-project.git
cd recoapp-project
```

2. **Abrir en Android Studio:**
   - File → Open → Seleccionar la carpeta del proyecto

3. **Sincronizar dependencias:**
   - Gradle sync automático o Build → Clean Project

4. **Ejecutar la aplicación:**
   - Conectar dispositivo Android o usar emulador
   - Run → Run 'app'

### Dependencias Principales

```kotlin
// Base de datos
implementation("androidx.room:room-runtime:2.5.0")
kapt("androidx.room:room-compiler:2.5.0")

// Navegación
implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
implementation("androidx.navigation:navigation-ui-ktx:2.6.0")

// RecyclerView
implementation("androidx.recyclerview:recyclerview:1.3.0")

// Material Design
implementation("com.google.android.material:material:1.9.0")
```

## 🔬 Testing

### Estructura de Testing
```
app/src/test/java/                    # Unit Tests
app/src/androidTest/java/            # Instrumented Tests
```

### Tipos de Test Implementados
- **Unit Tests:** Lógica de negocio y ViewModels
- **Integration Tests:** Base de datos y Repository
- **UI Tests:** Navegación y interfaz de usuario

### Ejecutar Tests
```bash
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest    # UI tests
```

## 🚀 Funcionalidades Futuras

### Próximas Implementaciones
- [ ] **Sincronización con API RESTful** usando Retrofit
- [ ] **Escáner de código QR/Barcode** para identificación rápida
- [ ] **Geolocalización automática** para ubicaciones
- [ ] **Notificaciones push** para recordatorios
- [ ] **Exportación de reportes** en PDF/CSV
- [ ] **Gráficos estadísticos** con bibliotecas de charts
- [ ] **Modo offline** con sincronización posterior
- [ ] **Autenticación de usuarios** multi-empresa

## 📈 Simulación Google Play

### Preparación para Publicación

1. **Build Release:**
```bash
./gradlew assembleRelease
```

2. **Información de la App:**
   - **Nombre:** RecoApp - Gestión de Residuos
   - **Categoría:** Productividad / Negocios
   - **Versión:** 1.0.0
   - **Target SDK:** 33
   - **Min SDK:** 24 (Android 7.0+)

3. **Assets para Store:**
   - ✅ Icono de la aplicación (1024x1024)
   - ✅ Screenshots de funcionalidades principales
   - ✅ Descripción en español
   - ✅ Política de privacidad

### Metadata Sugerida

**Título:** RecoApp - Sistema de Gestión de Residuos Sólidos

**Descripción Corta:**
Digitaliza la recolección de residuos sólidos con registro en tiempo real, reportes automáticos y cumplimiento normativo.

**Características Destacadas:**
- 📱 Registro digital de residuos sólidos
- 📊 Reportes automáticos con filtros avanzados
- 🔄 Funcionamiento offline con base de datos local
- ♻️ Categorización por tipo de residuo
- 📈 Estadísticas en tiempo real
- 🎯 Diseñado específicamente para empresas de limpieza

## 👥 Desarrollado para

**ECOLIM S.A.C.**
- Empresa especializada en gestión de residuos sólidos
- Necesidad de digitalizar procesos manuales
- Mejora en trazabilidad y reportes
- Cumplimiento de normativas ambientales

## 📞 Soporte y Contacto

Para soporte técnico o consultas sobre la aplicación:
- **Email:** soporte@recoapp.com
- **Teléfono:** +51 XXX XXX XXX
- **Sitio Web:** www.recoapp.com

---

**RecoApp v1.0** - Desarrollado con 💚 para un futuro más sostenible