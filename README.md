# Sistema Arka - Gestión de Inventario y Ventas

## Descripción del Proyecto

Arka es un sistema de gestión de inventario y ventas desarrollado para una empresa colombiana distribuidora de accesorios para PC. El sistema está diseñado para automatizar los procesos de abastecimiento, venta, actualización de stock, generación de reportes y notificaciones.

## Contexto del Negocio

**Arka** es una empresa colombiana distribuidora de accesorios para PC que:
- Atiende almacenes en las principales ciudades de Colombia
- Se encuentra en expansión hacia Ecuador, Perú y Chile
- Maneja productos de diferentes marcas con amplia gama de categorías
- Requiere especificación detallada de atributos de productos
- Busca autogestión de clientes para reducir costos operativos

## Problemática a Resolver

- **Gestión manual de inventario** que genera errores
- **Sobreventas por concurrencia** de órdenes simultáneas
- **Falta de reportes** de compras y ventas
- **Tiempos de entrega** insatisfactorios
- **Carritos abandonados** sin seguimiento
- **Ausencia de notificaciones** de cambios de estado

## Funcionalidades Principales

### ✅ Implementadas
- [x] Gestión de categorías de productos
- [x] Gestión de productos con atributos detallados
- [x] Gestión de clientes (almacenes)
- [x] Gestión de proveedores
- [x] Sistema de órdenes de compra (clientes)
- [x] Sistema de órdenes de compra a proveedores
- [x] Control de stock con validaciones
- [x] API REST con documentación automática

### 🚧 En Desarrollo
- [ ] Reportes de ventas semanales
- [ ] Reportes de productos por abastecer
- [ ] Sistema de notificaciones
- [ ] Seguimiento de carritos abandonados
- [ ] Dashboard de métricas
- [ ] Sistema de autenticación y autorización

### 📋 Por Implementar
- [ ] Integración con pasarelas de pago
- [ ] Sistema de facturación
- [ ] Integración con sistemas de envío
- [ ] App móvil para vendedores
- [ ] Chatbot de atención al cliente

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

El proyecto sigue una **arquitectura en capas** (Layered Architecture):

```
├── Controller Layer (REST API)
├── Service Layer (Business Logic)
├── Repository Layer (Data Access)
└── Model Layer (Entities)
```

### Estructura de Paquetes

```
com.arka.system/
├── controller/     # Controladores REST
├── service/        # Lógica de negocio
├── repository/     # Acceso a datos
├── model/          # Entidades JPA
├── dto/            # Data Transfer Objects
├── exception/      # Manejo de excepciones
└── config/         # Configuraciones
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

## Contribución

### Estándares de Código

- Seguir convenciones de Java (Google Style Guide)
- Cobertura de tests mínima: 80%
- Documentación JavaDoc en métodos públicos
- Validaciones con Bean Validation
- Manejo de errores con @ControllerAdvice

### Proceso de Desarrollo

1. Crear feature branch desde `main`
2. Implementar funcionalidad con tests
3. Ejecutar tests y verificar cobertura
4. Crear Pull Request con descripción detallada
5. Code review por al menos 2 desarrolladores
6. Merge después de aprobación

## Contacto y Soporte

- **Equipo de Desarrollo**: desarrollo@arka.com.co
- **Product Owner**: po@arka.com.co
- **Scrum Master**: scrum@arka.com.co

## Licencia

© 2025 Arka Colombia. Todos los derechos reservados.
