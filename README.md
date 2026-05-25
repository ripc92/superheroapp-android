# Superhero App

Una aplicación de Android moderna que permite explorar el universo de los superhéroes, construida con las últimas tecnologías de desarrollo y una estética **Premium Dark "Emerald Hero"**.

## Visión General
Este proyecto utiliza la [SuperHero API](https://superheroapi.com/) para mostrar información detallada sobre cientos de personajes. Está diseñado siguiendo los principios de **Clean Architecture** y **SOLID**, garantizando un código mantenible, escalable y testeable.

## Características Principales
- **Búsqueda en tiempo real:** Encuentra héroes y villanos por nombre.
- **Estadísticas de Poder:** Visualización clara de inteligencia, fuerza, velocidad y más.
- **Soporte Offline:** Cache local con Room para consultar héroes previamente buscados sin conexión.
- **Interfaz Premium:** Diseño minimalista con temática "Emerald Hero" (Verde esmeralda y petróleo).
- **Navegación Fluida:** Transiciones limpias entre el listado y el detalle.

## Tech Stack
- **Lenguaje:** Kotlin + Coroutines + Flow.
- **UI:** Jetpack Compose con Material 3.
- **Inyección de Dependencias:** Hilt.
- **Persistencia:** Room Database.
- **Networking:** Retrofit + OKHttp + GSON.
- **Imágenes:** Coil (con soporte para carga asíncrona).
- **Arquitectura:** Clean Architecture (Domain, Data, Presentation) + MVVM.

## Arquitectura
La app está dividida en tres capas principales:
1. **Domain:** Contiene los modelos de negocio, interfaces de repositorios y casos de uso puros.
2. **Data:** Implementa el acceso a datos (API/DB) y los mappers para transformar DTOs/Entities a modelos de dominio.
3. **Presentation:** ViewModels que gestionan el estado de la UI mediante `StateFlow` y pantallas en Compose.

## Configuración del Proyecto
Para ejecutar este proyecto, necesitas obtener una API Key gratuita de [superheroapi.com](https://superheroapi.com/).

1. Clona el repositorio.
2. Localiza el archivo `local.properties.example` en la raíz.
3. Renómbralo o cópialo como `local.properties`.
4. Añade tu token en la variable:
   ```properties
   SUPERHERO_API_TOKEN=TU_TOKEN_AQUI
   ```
5. Sincroniza el proyecto con Gradle y ejecuta la aplicación.

## Testing
El proyecto incluye:
- **Unit Tests:** Pruebas de lógica de negocio (UseCases) usando JUnit4, MockK y Coroutines Test.
- **UI Tests:** Pruebas instrumentadas para componentes de Compose.

---
Desarrollado para fans de los cómics y entusiastas de Android.
