# La Taberna del Bus de Eventos: "El Gólem Alquímico"

Sistema central de una taberna implementado con el patrón **Publisher/Subscriber**
mediante un **Event Bus** en Java 25 + JavaFX. Los componentes (pedidos, barra, cocina,
banda, sonido, panel LED, humo) no se conocen entre sí: solo se comunican por el bus.

## Requisitos
- JDK 25 o superior
- Maven (incluido mediante el wrapper `mvnw`)

## Cómo ejecutar
```bash
# Windows
mvnw.cmd javafx:run
# Linux / macOS
./mvnw javafx:run
```
También puedes abrir el proyecto en IntelliJ y ejecutar `TabernaApplication`.

## Cómo correr las pruebas
```bash
mvnw.cmd test
```

## Estructura del proyecto
- `logica/bus`: `EventBus`, `Evento`, `Suscriptor`
- `logica/eventos`: eventos inmutables (records)
- `logica/componentes`: Barra, Cocina, Banda, SistemaDePedidos, SistemaDeSonido, SistemaDeHumo
- `controlador`: `ControladorTaberna`, `PanelLED`
- `vista`: `PantallaTaberna` (solo nodos y getters)
- `docs/uml`: diagramas PlantUML (clases, secuencia, componentes)

## Uso de la aplicación
(1-3 líneas: elegir mesa, elegir items con Ctrl+clic, pulsar "Realizar pedido";
el panel LED y el sistema de sonido reaccionan solos; la banda toca cada 20 s.)

## Demostración de extensibilidad
`SistemaDeHumo` se agregó sin modificar `Banda` ni `EventBus`. Comprobación:
```bash
git diff --stat <commit-antes-del-humo> HEAD -- \
  src/main/java/com/example/sistemacentral/logica/componentes/Banda.java \
  src/main/java/com/example/sistemacentral/logica/bus/EventBus.java
```
(no imprime nada). Se registra en `TabernaApplication` con tres líneas.

## Decisiones de diseño
- Publicación síncrona; Barra y Cocina programan sus retardos con un scheduler.
- Colecciones concurrentes en el bus (publican varios hilos).
- Toda actualización de la UI pasa por `Platform.runLater`.