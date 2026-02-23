# UnderworldsTrack

Aplicación Android para jugadores de Warhammer Underworlds. Permite consultar bandas y sus luchadores, gestionar mazos personalizados y mazos Rivals, y utilizar un tirador de dados con la iconografía del juego.

## Funcionalidades

- Explorar bandas por alianza, con imagen del líder y facción correcta.
- Ver detalle de banda: descripción y galería de luchadores (base/inspirada).
- Mazos de usuario: crear, listar, ver detalle y eliminar.
- Mazos Rivals: catálogo predefinido consultable.
- Tirador de dados:
  - Ataque y defensa diferenciados.
  - Tirada por botón o agitando el dispositivo (acelerómetro).
  - Re-tirada individual tocando cada dado.
- Notificaciones al seleccionar banda con facción mostrada correctamente.
- Almacenamiento local en SQLite con datos precargados.

## Requisitos

- Android Studio (Giraffe o superior recomendado).
- JDK compatible con la versión de Gradle del proyecto.
- Dispositivo o emulador Android con API mínima indicada en el módulo `app`.

## Instalación y ejecución

```bash
git clone https://github.com/eronmarru/TFG.git
cd TFG
```

1. Abrir en Android Studio.
2. Sincronizar Gradle.
3. Ejecutar sobre emulador o dispositivo físico.

## Módulos principales

- `DatabaseHelper`: creación y carga de la base de datos SQLite (bandas, luchadores, mazos, cartas).
- `Bands`, `FactionBandsActivity`, `BandDetailActivity`: navegación y detalle de bandas.
- `DeckBuilderActivity`, `UserDecksActivity`, `UserDeckDetailActivity`: gestión de mazos de usuario.
- `RivalDecksListActivity`, `RivalDeckDetailActivity`: navegación de mazos Rivals.
- `DiceRollerActivity`: tirador de dados con acelerómetro y re-tiradas individuales.
- `VideosActivity`, `VideoActivity`: reproductor y listado de vídeos informativos.

## Estructura

- `app/src/main/java/com/example/underworldstrack/`: actividades y lógica.
- `app/src/main/res/layout/`: vistas XML.
- `app/src/main/res/drawable/`: imágenes de bandas, luchadores, cartas y dados.
- `AndroidManifest.xml`: declaración de actividades, icono y permisos.

## Notas

- Proyecto no oficial, orientado a uso personal de jugadores.
- Contenido (bandas, cartas, imágenes) puede ampliarse o ajustarse en versiones futuras.

