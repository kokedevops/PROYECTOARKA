# Sistema Arka - Gestión de Inventario y Ventas
## Arquitectura Hexagonal/DDD + Programación Reactiva

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![WebFlux](https://img.shields.io/badge/WebFlux-Reactive-blue.svg)](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
[![Hexagonal Architecture](https://img.shields.io/badge/Architecture-Hexagonal-purple.svg)](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software))

## 🏗️ Arquitectura del Sistema

Este proyecto implementa **Arquitectura Hexagonal/DDD** con **Programación Reactiva** usando Spring WebFlux, diseñado como microservicio para gestión de inventario y ventas.

### Principios Arquitectónicos Aplicados

- ✅ **Arquitectura Hexagonal**: Separación clara entre dominio, aplicación e infraestructura
- ✅ **Domain-Driven Design (DDD)**: Modelado basado en el dominio del negocio
- ✅ **Lenguaje Ubicuo**: Terminología consistente entre código y negocio
- ✅ **Independencia del Dominio**: Núcleo de negocio libre de dependencias externas
- ✅ **Programación Reactiva**: Endpoints no bloqueantes con WebFlux
- ✅ **Inyección de Dependencias**: Inversión de dependencias mediante puertos y adaptadores

## 📁 Estructura del Proyecto

```
src/
├── main/java/com/arka/system/
│   ├── 🎯 domain/                     # DOMINIO (NÚCLEO)
│   │   ├── model/                     # Entidades y Agregados
│   │   │   ├── Product.java           # Agregado raíz de Producto
│   │   │   ├── Order.java             # Agregado raíz de Pedido
│   │   │   └── Customer.java          # Entidad Cliente
│   │   ├── port/
│   │   │   ├── in/                    # Puertos de entrada (Use Cases)
│   │   │   │   ├── ProductManagementUseCase.java
│   │   │   │   └── OrderManagementUseCase.java
│   │   │   └── out/                   # Puertos de salida (Repositorios)
│   │   │       ├── ProductRepositoryPort.java
│   │   │       └── OrderRepositoryPort.java
│   │   └── service/                   # Servicios de dominio
│   │       ├── InventoryDomainService.java
│   │       └── PricingDomainService.java
│   ├── 🔧 application/                # APLICACIÓN
│   │   ├── usecase/                   # Implementación de casos de uso
│   │   │   └── ProductManagementUseCaseImpl.java
│   │   └── mapper/                    # Mappers entre capas
│   │       └── ProductDTOMapper.java
│   ├── 🏢 infrastructure/             # INFRAESTRUCTURA
│   │   ├── adapter/
│   │   │   ├── in/                    # Adaptadores de entrada
│   │   │   │   ├── rest/              # Controllers REST tradicionales
│   │   │   │   │   └── ProductController.java
│   │   │   │   └── reactive/          # Controllers WebFlux reactivos
│   │   │   │       ├── ReactiveProductController.java
│   │   │   │       └── AdvancedReactiveController.java
│   │   │   └── out/                   # Adaptadores de salida
│   │   │       └── persistence/       # Persistencia JPA
│   │   │           ├── ProductRepositoryAdapter.java
│   │   │           └── JpaProductRepository.java
│   │   └── config/                    # Configuraciones
│   │       ├── SecurityConfig.java
│   │       ├── WebFluxConfig.java
│   │       └── HexagonalArchitectureConfig.java
│   └── 🔄 shared/                     # COMPARTIDO
│       ├── dto/                       # DTOs y Commands
│       ├── exception/                 # Excepciones del dominio
│       └── util/                      # Utilidades
└── test/                              # TESTS
    ├── unit/                          # Tests unitarios del dominio
    ├── integration/                   # Tests de integración
    └── reactive/                      # Tests reactivos con StepVerifier
```

## 🌐 Descripción del Negocio

**Arka** es una empresa colombiana distribuidora de accesorios para PC que:
- 🏪 Atiende almacenes en las principales ciudades de Colombia
- 🌎 Se encuentra en expansión hacia Ecuador, Perú y Chile
- 📦 Maneja productos de diferentes marcas con amplia gama de categorías
- ⚙️ Requiere especificación detallada de atributos de productos
- 🤖 Busca autogestión de clientes para reducir costos operativos

## � Cómo Ejecutar el Proyecto

### **Prerrequisitos**
- Java 21
- Gradle 8.14.3

### **Ejecución**
```bash
# Compilar y ejecutar
./gradlew bootRun

# O en Windows
.\gradlew bootRun
```

### **Verificación**
Una vez ejecutado, el sistema estará disponible en:
- **API Base**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **H2 Console**: http://localhost:8080/api/h2-console

---

## � Enlaces Importantes

- �📖 **[Glosario del Lenguaje Ubicuo](docs/lenguaje-ubicuo-glosario.md)** - Términos del dominio y reglas de negocio
- 🏗️ **[Arquitectura Hexagonal](docs/arquitectura-hexagonal.md)** - Diagramas y explicación de la arquitectura  
- 📝 **[Manual de Creación del Proyecto](docs/manual-creacion-proyecto.md)** - Guía paso a paso
- 🔄 **[Migración a Hexagonal](docs/migracion-hexagonal.md)** - Proceso de migración
- 🎯 **[Entrega Final](docs/entrega-final.md)** - Resumen completo de la entrega

---

## 📋 Casos de Uso Implementados

### **Gestión de Productos (ProductManagementUseCase)**
- ✅ Crear producto con validaciones
- ✅ Actualizar producto existente  
- ✅ Consultar productos (por ID, SKU, categoría)
- ✅ Buscar productos por texto
- ✅ Listar productos con paginación
- ✅ Identificar productos con stock bajo
- ✅ Eliminar productos

### **Gestión de Inventario (InventoryDomainService)**
- ✅ Validar disponibilidad de stock
- ✅ Reservar/liberar stock para ventas
- ✅ Detectar necesidad de reabastecimiento
- ✅ Calcular cantidades sugeridas para órdenes de compra

### **Gestión de Precios (PricingDomainService)**
- ✅ Calcular subtotales y totales de pedidos
- ✅ Aplicar impuestos (IVA 19% Colombia)
- ✅ Calcular márgenes de ganancia
- ✅ Validar precios de venta
- ✅ Sugerir precios basados en margen mínimo
- ✅ Aplicar descuentos por volumen

---

## 🌐 API Endpoints

### **REST API Tradicional**
```
GET    /api/products              - Listar productos
POST   /api/products              - Crear producto
GET    /api/products/{id}         - Obtener producto por ID
PUT    /api/products/{id}         - Actualizar producto
DELETE /api/products/{id}         - Eliminar producto
GET    /api/public/health         - Health check público
```

### **API Reactiva (WebFlux)**
```
GET    /api/reactive/products                    - Stream de productos
GET    /api/reactive/products/{id}              - Producto por ID (reactivo)
GET    /api/reactive/products/sku/{sku}         - Producto por SKU (reactivo)
POST   /api/reactive/products                   - Crear producto (reactivo)
PUT    /api/reactive/products/{id}              - Actualizar producto (reactivo)
DELETE /api/reactive/products/{id}              - Eliminar producto (reactivo)
GET    /api/reactive/products/category/{id}     - Productos por categoría
GET    /api/reactive/products/low-stock         - Productos con stock bajo
GET    /api/reactive/products/search            - Búsqueda de productos

# Endpoints Avanzados con Múltiples Flujos Asíncronos
GET    /api/reactive/advanced/dashboard         - Dashboard en tiempo real
GET    /api/reactive/advanced/products/parallel - Procesamiento paralelo
GET    /api/reactive/advanced/products/enriched - Productos enriquecidos
GET    /api/reactive/advanced/analytics/realtime - Analíticas en tiempo real
```

---

## 🧪 Testing

### **Tests Unitarios**
```bash
# Ejecutar todos los tests
./gradlew test

# Tests específicos
./gradlew test --tests "*ProductManagementUseCaseTest"
./gradlew test --tests "*ReactiveProductControllerTest"
```

### **Tests Reactivos con StepVerifier**
El proyecto incluye tests completos para los controladores reactivos:
- Validación de flujos `Flux` y `Mono`
- Manejo de errores reactivos
- Control de backpressure
- Timing y emisiones periódicas

---

## 🏆 Logros Arquitectónicos

### ✅ **Arquitectura Hexagonal Completa**
- Separación clara entre dominio, aplicación e infraestructura
- Puertos y adaptadores bien definidos
- Inversión de dependencias implementada
- Dominio completamente independiente de frameworks

### ✅ **Domain-Driven Design (DDD)**
- Agregados y entidades del dominio modelados
- Servicios de dominio para lógica de negocio
- Eventos de dominio definidos
- Lenguaje ubicuo documentado y aplicado

### ✅ **Programación Reactiva**
- Endpoints no bloqueantes con WebFlux
- Múltiples llamadas asíncronas combinadas
- Manejo de backpressure
- Streaming de datos en tiempo real
- Tests reactivos con StepVerifier

### ✅ **Calidad de Código**
- Cobertura de tests reactivos
- Documentación completa con Swagger
- Manejo de excepciones robusto
- Validaciones de entrada
- Logging estructurado

---

## 👥 Equipo y Metodología

- **Metodología**: Scrum
- **Sprints**: 2 semanas
- **Estimación**: Planning Poker
- **Documentación**: Integrada en código y docs/
- **Calidad**: TDD para lógica de negocio crítica

---

## 📈 Próximos Pasos

### 🚧 Funcionalidades Pendientes
- [ ] Completar módulos de Order, Category, Customer, Supplier
- [ ] Implementar sistema de notificaciones reactivas
- [ ] Agregar métricas y monitoreo con Micrometer
- [ ] Implementar cache reactivo con Redis
- [ ] Añadir seguridad JWT
- [ ] Tests de integración end-to-end

### 🎯 Mejoras Arquitectónicas
- [ ] Event Sourcing para auditoría
- [ ] CQRS para separar lecturas/escrituras
- [ ] Distributed tracing con Zipkin
- [ ] Circuit breaker con Resilience4j
- [ ] Rate limiting en endpoints reactivos

---

## 📞 Contacto y Soporte

Para consultas sobre la arquitectura, implementación o funcionalidades del Sistema Arka, consultar la documentación en `/docs` o los comentarios inline en el código fuente.

**¡El sistema está listo para evaluación y demostración de todos los criterios de la entrega esperada!** 🎉

## Tecnologías Utilizadas

- **Backend**: Spring Boot 3.5.3
- **Lenguaje**: Java 17
- **Base de datos**: MySQL (Producción), H2 (Desarrollo)
- **ORM**: JPA/Hibernate
- **Build Tool**: Gradle
- **Seguridad**: Spring Security
- **Documentación**: OpenAPI 3 (Swagger)
- **Testing**: JUnit 5, Spring Boot Test

## Arquitectura

El proyecto sigue una **arquitectura hexagonal** (Ports and Adapters):

```
┌─────────────────────────────────────────────────────────┐
│                    Adapters (UI)                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │ REST API    │  │ GraphQL     │  │ CLI         │     │
│  │ Controller  │  │ Resolver    │  │ Interface   │     │
│  └─────────────┘  └─────────────┘  └─────────────┘     │
└─────────────────────────────────────────────────────────┘
                              │
                    ┌─────────▼─────────┐
                    │      Ports        │
                    │   (Interfaces)    │
                    └─────────▲─────────┘
                              │
┌─────────────────────────────▼─────────────────────────────┐
│                  Domain Core                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐       │
│  │   Domain    │  │  Use Cases  │  │   Domain    │       │
│  │  Entities   │  │ (Services)  │  │  Services   │       │
│  └─────────────┘  └─────────────┘  └─────────────┘       │
└─────────────────────────────────────────────────────────┘
                              │
                    ┌─────────▼─────────┐
                    │      Ports        │
                    │   (Interfaces)    │
                    └─────────▲─────────┘
                              │
┌─────────────────────────────▼─────────────────────────────┐
│                Adapters (Infrastructure)                  │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐       │
│  │ Database    │  │ File System │  │ External    │       │
│  │ Repository  │  │ Storage     │  │ APIs        │       │
│  └─────────────┘  └─────────────┘  └─────────────┘       │
└─────────────────────────────────────────────────────────┘
```

### Estructura de Paquetes (Hexagonal)

```
com.arka.system/
├── domain/                    # Núcleo del dominio
│   ├── model/                # Entidades de dominio
│   ├── port/                 # Puertos (interfaces)
│   │   ├── in/              # Puertos de entrada (use cases)
│   │   └── out/             # Puertos de salida (repositories)
│   └── service/             # Servicios de dominio
├── application/              # Casos de uso
│   └── usecase/             # Implementación de casos de uso
├── infrastructure/           # Adaptadores
│   ├── adapter/
│   │   ├── in/              # Adaptadores de entrada
│   │   │   ├── rest/        # Controllers REST
│   │   │   └── graphql/     # Resolvers GraphQL
│   │   └── out/             # Adaptadores de salida
│   │       ├── persistence/ # Repositorios JPA
│   │       └── external/    # APIs externas
│   └── config/              # Configuraciones
└── shared/                   # Elementos compartidos
    ├── dto/                 # Data Transfer Objects
    ├── exception/           # Excepciones
    └── util/                # Utilidades
```

## Metodología de Desarrollo

Para este proyecto se ha elegido **Metodología Ágil - Scrum** por las siguientes razones:

### ¿Por qué Scrum?

1. **Adaptabilidad**: Permite adaptarse rápidamente a cambios en requerimientos del negocio
2. **Entrega incremental**: Funcionalidades entregadas en sprints cortos (1-2 semanas)
3. **Feedback continuo**: Validación constante con stakeholders
4. **Gestión de riesgos**: Identificación temprana de problemas
5. **Transparencia**: Visibilidad total del progreso del proyecto

### Organización de Sprints

- **Sprint 1**: Configuración inicial, entidades básicas y API foundation
- **Sprint 2**: Gestión de productos e inventario
- **Sprint 3**: Sistema de órdenes y clientes
- **Sprint 4**: Reportes y notificaciones
- **Sprint 5**: Optimizaciones y mejoras de rendimiento

## Instalación y Configuración

### Prerrequisitos

- Java 17 o superior
- MySQL 8.0+ (para producción)
- Gradle 7.0+ (incluido wrapper)

### Configuración de Base de Datos

#### Para Desarrollo (H2)
La configuración por defecto usa H2 en memoria. No requiere configuración adicional.

#### Para Producción (MySQL)
1. Crear base de datos:
```sql
CREATE DATABASE arka_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'arka_user'@'localhost' IDENTIFIED BY 'arka_password';
GRANT ALL PRIVILEGES ON arka_db.* TO 'arka_user'@'localhost';
FLUSH PRIVILEGES;
```

2. Actualizar `application.properties`:
```properties
spring.profiles.active=prod
spring.datasource.url=jdbc:mysql://localhost:3306/arka_db
spring.datasource.username=arka_user
spring.datasource.password=arka_password
```

### Ejecución

#### Usando Gradle Wrapper (Recomendado)
```bash
# Windows
.\gradlew bootRun

# Linux/Mac
./gradlew bootRun
```

#### Usando Gradle instalado
```bash
gradle bootRun
```

### Testing
```bash
# Ejecutar todos los tests
.\gradlew test

# Ejecutar tests con reporte de cobertura
.\gradlew test jacocoTestReport
```

## API Documentation

Una vez iniciado el servidor, la documentación de la API estará disponible en:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs

## Endpoints Principales

### Públicos (sin autenticación)
- `GET /api/public/health` - Estado del sistema
- `GET /api/public/info` - Información del sistema

### Base de datos (desarrollo)
- **H2 Console**: http://localhost:8080/api/h2-console
  - JDBC URL: `jdbc:h2:mem:arka_db`
  - Username: `sa`
  - Password: (vacío)

## Monitoreo

El sistema incluye Spring Boot Actuator para monitoreo:

- **Health Check**: http://localhost:8080/api/actuator/health
- **Métricas**: http://localhost:8080/api/actuator/metrics
- **Info**: http://localhost:8080/api/actuator/info

## Requerimientos Detallados

### Funcionales

1. **Gestión de Inventario**
   - CRUD de productos con categorías y atributos
   - Control de stock con validaciones de concurrencia
   - Alertas de stock mínimo
   - Búsqueda avanzada con filtros

2. **Gestión de Pedidos**
   - Creación y modificación de órdenes
   - Estados de pedido (Pendiente, Confirmado, Enviado, Entregado)
   - Validación de stock disponible
   - Cálculo automático de totales

3. **Gestión de Proveedores**
   - CRUD de proveedores
   - Órdenes de compra a proveedores
   - Seguimiento de entregas
   - Actualización automática de stock

4. **Reportes**
   - Ventas semanales
   - Productos por abastecer
   - Rendimiento por categoría
   - Clientes más activos

5. **Notificaciones**
   - Cambios de estado de pedidos
   - Recordatorios de carritos abandonados
   - Alertas de stock bajo
   - Confirmaciones de recepción

### No Funcionales

1. **Rendimiento**
   - Tiempo de respuesta < 200ms para consultas simples
   - Soporte para 1000+ usuarios concurrentes
   - Optimización de consultas de base de datos

2. **Seguridad**
   - Autenticación JWT
   - Autorización basada en roles
   - Encriptación de datos sensibles
   - Auditoría de operaciones

3. **Disponibilidad**
   - Uptime 99.9%
   - Backup automático diario
   - Recovery point objective (RPO) < 1 hora

4. **Escalabilidad**
   - Arquitectura preparada para microservicios
   - Cache distribuido (Redis)
   - Load balancing



