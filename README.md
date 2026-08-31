# ProyectoEstructuras — Juego "Simón dice"
 
Implementación en Java (Swing) del clásico juego **Simón dice**: se muestra una secuencia de colores/sonidos que el jugador debe repetir; cada ronda agrega un paso nuevo y hay un límite de tiempo para responder.
 
## Arquitectura
 
Sigue el patrón **MVC**:
 
- **`Modelo`** — estado del juego (secuencia, nivel, puntos, tiempo restante), reproducción de sonidos (`javax.sound.sampled`) y carga de configuración inicial desde `CargadoDeArchivos.xml` (usando JDOM2).
- **`Vista`** — interfaz gráfica con Swing/AWT: ventana del juego, panel de botones (los "arcos" de colores) y labels de nivel, tiempo y puntuación.
- **`Controlador`** — conecta Modelo y Vista, maneja la lógica de eventos y el flujo de las rondas.
- **`ProyectoEstructuras`** — clase `main`, punto de entrada que instancia el patrón MVC.
## Configuración
 
`src/main/java/Modelo/CargadoDeArchivos.xml` define los parámetros iniciales del juego:
 
```xml
<Tempo>15</Tempo>                     <!-- segundos por ronda -->
<VelocidadDeSecuencia>1000</VelocidadDeSecuencia>  <!-- ms entre pasos -->
<MultiplicadorDePuntos>1</MultiplicadorDePuntos>
```
 
Los sonidos (`1.wav`, `2.wav`, `3.wav`, `4.wav`, `yes.wav`, `no.wav`, `gameOver.wav`, `start.wav`) están duplicados en la raíz del proyecto y en `src/main/resources/`.
 
## Requisitos para correrlo en VS Code
 
1. **JDK 19** instalado (el `pom.xml` fija `maven.compiler.source/target` en 19). Verificá con `java -version`.
2. **Apache Maven** instalado (o usar el wrapper de Maven si lo agregás).
3. En VS Code, instalar el **Extension Pack for Java** (incluye soporte de Maven, debugging y ejecución) — extensión `vscjava.vscode-java-pack`.
4. Abrir la carpeta `ProyectoEstructuras` (la que contiene `pom.xml`) como carpeta raíz del workspace, para que VS Code detecte el proyecto Maven automáticamente.
5. La única dependencia externa es **JDOM2** (`org.jdom:jdom2:2.0.6.1`), declarada en el `pom.xml`; Maven la descarga solo al compilar.
### Ejecutar
 
Desde la terminal integrada de VS Code, dentro de la carpeta del proyecto:
 
```bash
mvn compile exec:java
```
 
o bien usar el botón **Run** que aparece sobre el método `main` en `ProyectoEstructuras.java` una vez que la extensión de Java haya indexado el proyecto.
 
> ⚠️ El código carga el XML de configuración con una ruta relativa (`src/main/java/Modelo/CargadoDeArchivos.xml`), así que el programa debe ejecutarse con el **directorio raíz del proyecto** como working directory (que es el comportamiento por defecto en VS Code/Maven).
 
