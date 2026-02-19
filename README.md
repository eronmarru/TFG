## UnderworldsTrack TFG

Aplicación Android desarrollada como Trabajo de Fin de Grado para gestionar contenido de **Warhammer Underworlds**.

La app permite consultar bandas, ver sus miniaturas, gestionar mazos de cartas (propios y de Rivals) y disponer de una utilidad de tirada de dados con iconografía propia del juego.

### Características principales

- Listado de bandas con imagen de líder y facción asociada.
- Vista de detalle de banda con galería de luchadores.
- Gestor de mazos de usuario (creación, listado y detalle).
- Listado de mazos Rivals predefinidos.
- Visualización de cartas con sus imágenes.
- Tirador de dados de ataque y defensa con probabilidades adaptadas a Underworlds:
  - Distingue entre dados de Ataque y Defensa.
  - Permite tiradas por botón o por movimiento (acelerómetro).
  - Re–tirada individual al pulsar sobre un dado.
- Almacenamiento local mediante base de datos SQLite con datos iniciales precargados (bandas, luchadores, mazos, cartas).

### Requisitos

- Android Studio reciente (Giraffe o superior recomendado).
- JDK compatible con la versión de Gradle usada por el proyecto.
- Dispositivo o emulador Android con API mínima según `build.gradle` (consulta el módulo `app`).

### Cómo abrir el proyecto

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/eronmarru/TFG.git
   cd TFG
   ```

2. Abrir la carpeta del proyecto en **Android Studio**.
3. Esperar a que se sincronicen las dependencias de Gradle.
4. Ejecutar la aplicación:
   - Selecciona un dispositivo físico o emulador.
   - Pulsa en **Run** ▶.

### Estructura básica

- `app/src/main/java/com/example/underworldstrack/`
  - `MainActivity` y actividades de navegación principal.
  - `DatabaseHelper`: lógica de creación y carga de la base de datos SQLite.
  - Actividades de bandas, mazos y tirador de dados.
- `app/src/main/res/layout/`
  - Diseños XML de las pantallas (bandas, mazos, dados, etc.).
- `app/src/main/res/drawable/`
  - Imágenes de bandas, luchadores y caras de dados.

### Notas

- El proyecto está pensado como herramienta de apoyo para jugadores de Warhammer Underworlds, no es una aplicación oficial.
- Algunas partes del contenido (bandas, cartas, imágenes) pueden ampliarse o ajustarse en futuras versiones.

