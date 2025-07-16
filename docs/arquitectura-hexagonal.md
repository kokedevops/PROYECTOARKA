# Arquitectura Hexagonal - Sistema Arka

## Diagrama de Arquitectura

```
┌─────────────────────────────────────────────────────────────────┐
│                         HEXAGONAL ARCHITECTURE                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                    ADAPTERS (IN)                        │   │
│  │  ┌─────────────────┐  ┌─────────────────┐               │   │
│  │  │   REST API      │  │   GraphQL       │               │   │
│  │  │  Controllers    │  │   Resolvers     │               │   │
│  │  └─────────────────┘  └─────────────────┘               │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              │                                 │
│                              ▼                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                  APPLICATION LAYER                      │   │
│  │  ┌─────────────────┐  ┌─────────────────┐               │   │
│  │  │   Use Cases     │  │     Mappers     │               │   │
│  │  │ Implementation  │  │   & Validators  │               │   │
│  │  └─────────────────┘  └─────────────────┘               │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              │                                 │
│                              ▼                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                   DOMAIN CORE                           │   │
│  │  ┌─────────────────┐  ┌─────────────────┐               │   │
│  │  │    Entities     │  │   Value Objects │               │   │
│  │  │   & Aggregates  │  │  & Domain Events│               │   │
│  │  └─────────────────┘  └─────────────────┘               │   │
│  │  ┌─────────────────┐  ┌─────────────────┐               │   │
│  │  │   Domain        │  │     Ports       │               │   │
│  │  │   Services      │  │   (Interfaces)  │               │   │
│  │  └─────────────────┘  └─────────────────┘               │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              │                                 │
│                              ▼                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                   ADAPTERS (OUT)                        │   │
│  │  ┌─────────────────┐  ┌─────────────────┐               │   │
│  │  │   Database      │  │   External APIs │               │   │
│  │  │   Repositories  │  │   & Services    │               │   │
│  │  └─────────────────┘  └─────────────────┘               │   │
│  │  ┌─────────────────┐  ┌─────────────────┐               │   │
│  │  │   Messaging     │  │   File Storage  │               │   │
│  │  │   & Events      │  │   & External    │               │   │
│  │  └─────────────────┘  └─────────────────┘               │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

## Principios Aplicados

### 1. **Separación de Responsabilidades**
- **Dominio**: Lógica de negocio pura
- **Aplicación**: Orquestación de casos de uso
- **Infraestructura**: Detalles técnicos y adaptadores

### 2. **Inversión de Dependencias**
- El dominio no depende de la infraestructura
- Las dependencias apuntan hacia el centro (dominio)
- Uso de interfaces (puertos) para abstraer dependencias externas

### 3. **Testabilidad**
- Dominio completamente testeable de forma unitaria
- Adaptadores pueden ser mockeados fácilmente
- Separación clara entre lógica de negocio y detalles técnicos

## Estructura de Carpetas

```
src/
├── main/java/com/arka/system/
│   ├── domain/                     # DOMINIO (NÚCLEO)
│   │   ├── model/                  # Entidades y Value Objects
│   │   │   ├── Product.java        # Agregado raíz
│   │   │   ├── Category.java       # Entidad
│   │   │   ├── Order.java          # Agregado raíz
│   │   │   ├── Customer.java       # Entidad
│   │   │   └── Supplier.java       # Entidad
│   │   ├── port/
│   │   │   ├── in/                 # Puertos de entrada (Use Cases)
│   │   │   │   ├── ProductManagementUseCase.java
│   │   │   │   ├── OrderManagementUseCase.java
│   │   │   │   └── InventoryManagementUseCase.java
│   │   │   └── out/                # Puertos de salida (Repositorios)
│   │   │       ├── ProductRepositoryPort.java
│   │   │       ├── OrderRepositoryPort.java
│   │   │       └── NotificationPort.java
│   │   └── service/                # Servicios de dominio
│   │       ├── InventoryDomainService.java
│   │       └── PricingDomainService.java
│   ├── application/                # APLICACIÓN
│   │   ├── usecase/                # Implementación de casos de uso
│   │   │   ├── ProductManagementUseCaseImpl.java
│   │   │   └── OrderManagementUseCaseImpl.java
│   │   └── mapper/                 # Mappers entre capas
│   │       ├── ProductDTOMapper.java
│   │       └── OrderDTOMapper.java
│   ├── infrastructure/             # INFRAESTRUCTURA
│   │   ├── adapter/
│   │   │   ├── in/                 # Adaptadores de entrada
│   │   │   │   ├── rest/           # Controllers REST
│   │   │   │   │   ├── ProductController.java
│   │   │   │   │   └── OrderController.java
│   │   │   │   └── reactive/       # Controllers WebFlux
│   │   │   │       ├── ReactiveProductController.java
│   │   │   │       └── ReactiveOrderController.java
│   │   │   └── out/                # Adaptadores de salida
│   │   │       ├── persistence/    # Persistencia JPA
│   │   │       │   ├── ProductRepositoryAdapter.java
│   │   │       │   └── JpaProductRepository.java
│   │   │       ├── messaging/      # Eventos y notificaciones
│   │   │       │   └── NotificationAdapter.java
│   │   │       └── external/       # APIs externas
│   │   │           └── PaymentServiceAdapter.java
│   │   └── config/                 # Configuraciones
│   │       ├── DatabaseConfig.java
│   │       ├── SecurityConfig.java
│   │       └── WebFluxConfig.java
│   └── shared/                     # COMPARTIDO
│       ├── dto/                    # DTOs y Commands
│       ├── exception/              # Excepciones
│       └── util/                   # Utilidades
└── test/                           # TESTS
    ├── unit/                       # Tests unitarios del dominio
    ├── integration/                # Tests de integración
    └── e2e/                        # Tests end-to-end
```

## Beneficios de esta Arquitectura

1. **Mantenibilidad**: Separación clara de responsabilidades
2. **Testabilidad**: Dominio independiente y testeable
3. **Flexibilidad**: Fácil cambio de adaptadores sin afectar el dominio
4. **Escalabilidad**: Estructura clara para crecimiento del sistema
5. **Independencia**: El dominio no conoce detalles de implementación

## Casos de Uso Implementados

### ProductManagementUseCase
- Crear producto
- Actualizar producto
- Consultar productos
- Validar stock mínimo

### OrderManagementUseCase
- Crear orden
- Validar disponibilidad
- Calcular totales
- Gestionar estados

### InventoryManagementUseCase
- Controlar stock
- Generar alertas
- Auditar movimientos
