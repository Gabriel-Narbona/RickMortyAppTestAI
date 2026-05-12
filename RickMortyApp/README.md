# Rick & Morty Android App

Una aplicación moderna de Android desarrollada en Kotlin que consume la API de Rick and Morty, implementando **Clean Architecture**, **MVVM + MVI**, y las últimas librerías de Jetpack como **Navigation 3** y **Compose**.

## 📱 Características Principales

*   **Splash Screen**: Pantalla de inicio con temporizador y diseño temático.
*   **Login**: Pantalla de autenticación simulada con validación de campos.
*   **Home con Bottom Navigation**:
    *   **Personajes**: Grid infinito con imágenes, buscador y filtros avanzados.
    *   **Capítulos**: Lista detallada de episodios con información fiel a la API.
*   **Detalle de Personaje**: Vista completa con información detallada y acciones.
*   **Gestión de Datos Local (Offline-First Híbrido)**:
    *   **Edición**: Modifica datos de personajes y guárdalos localmente en **Room**.
    *   **Borrado (Soft Delete)**: Elimina personajes de tu lista local sin afectar la API.
    *   **Persistencia**: Los cambios locales tienen prioridad sobre los datos de la API.
*   **Navegación Robusta**:
    *   Soporte para **rotación de pantalla** y cambios de configuración sin perder el estado de navegación.
    *   Manejo correcto del "Back Stack" (cerrar app desde Home, no volver a Login).
*   **UX Mejorada**:
    *   **Auto-refresh**: Las listas se actualizan automáticamente al volver de pantallas de detalle/edición.
    *   **Manejo de Errores 429**: Interceptor con *exponential backoff* para límites de API.
    *   **Icono Adaptativo**: Icono personalizado que se integra perfectamente con el sistema.

## 🛠 Tech Stack

*   **Lenguaje**: Kotlin
*   **UI**: Jetpack Compose (Material 3)
*   **Arquitectura**: Clean Architecture (Presentation, Domain, Data) + MVVM + MVI
*   **Navegación**: **Jetpack Navigation 3** (Type-safe, Serializable State)
*   **Inyección de Dependencias**: Hilt
*   **Red**: Retrofit + OkHttp + Gson
*   **Base de Datos**: Room (con KSP)
*   **Carga de Imágenes**: Coil
*   **Asincronía**: Coroutines + Flow

## 🏗 Arquitectura

El proyecto sigue una arquitectura limpia dividida en tres capas:

### 1. Domain Layer (Kotlin Puro)
Lógica de negocio agnóstica del framework.
*   **Modelos**: `Character`, `Episode`.
*   **Casos de Uso**: `GetCharactersUseCase`, `SaveCharacterUseCase`, `DeleteCharacterUseCase`, etc.
*   **Repositorios**: Interfaces que definen el contrato de datos.

### 2. Data Layer
Implementación de repositorios y fuentes de datos.
*   **Remote**: API de Rick & Morty con manejo de errores HTTP 429.
*   **Local**: Base de datos Room para guardar ediciones y estado de borrado (`isDeleted`).
*   **Repository Impl**: Lógica de fusión (Merge) que combina datos remotos con modificaciones locales, filtrando elementos borrados.

### 3. Presentation Layer (UI)
*   **MVVM + MVI**: ViewModels gestionan `UiState` y emiten `UiEvent`/`Effect`.
*   **Navigation 3**: Sistema de navegación basado en tipos (`NavKey`) que implementa `Serializable` para sobrevivir a la recreación del proceso/actividad.
*   **Lifecycle**: Uso de `LifecycleResumeEffect` para garantizar datos frescos.

## 📂 Estructura del Proyecto

```
com.example.rickmortyapp
├── data
│   ├── local          # Room (Dao, Entity, DB)
│   ├── remote         # Retrofit (Api, Dto, Mapper)
│   └── repository     # Single Source of Truth Logic
├── di                 # Módulos Hilt
├── domain
│   ├── model          # Modelos puros
│   ├── repository     # Interfaces
│   └── usecase        # Lógica de negocio atómica
└── ui
    ├── character      # Detalle y Edición
    ├── login          # Login
    ├── navigation     # Configuración Nav 3
    ├── series         # Home (Tabs, Listas, Filtros)
    ├── splash         # Splash
    └── theme          # Theme & Type
```

## 🚀 Configuración y Ejecución

1.  **Requisitos**: Android Studio Ladybug o superior, JDK 17.
2.  **Sincronizar**: Gradle Sync al abrir el proyecto.
3.  **Ejecutar**: Seleccionar el módulo `app`.

## 🔧 Soluciones Técnicas Destacadas

### Persistencia de Navegación
Utilizamos `rememberSaveable` con un `ListSaver` personalizado en el `NavHost` para guardar la pila de navegación (`backStack`) como objetos `Serializable`. Esto permite que la aplicación recuerde exactamente dónde estaba el usuario tras girar el dispositivo.

### Borrado Lógico
Para permitir borrar datos que provienen de una API de solo lectura:
1.  Se guarda una copia del personaje en local con `isDeleted = true`.
2.  El repositorio filtra activamente cualquier personaje con este flag al combinar los datos de la API y Room.

### Manejo de API Rate Limits
Un interceptor personalizado captura errores `429 Too Many Requests` y reintenta la petición automáticamente con tiempos de espera crecientes, evitando crasheos y mejorando la experiencia de usuario.
