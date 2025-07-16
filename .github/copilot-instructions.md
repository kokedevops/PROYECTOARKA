# Instrucciones para GitHub Copilot - Proyecto Arka

<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

## Descripción del Proyecto

Arka es un sistema de gestión de inventario y ventas para una empresa distribuidora de accesorios para PC. El sistema está desarrollado con:

- **Backend**: Spring Boot 3.5.3 con Java 17
- **Base de datos**: MySQL con JPA/Hibernate
- **Arquitectura**: REST API con arquitectura en capas (Controller, Service, Repository)
- **Seguridad**: Spring Security

## Contexto del Negocio

- Empresa distribuidora de accesorios para PC en Colombia
- Clientes: almacenes que compran en grandes cantidades
- Expansión: Ecuador, Perú y Chile
- Productos de múltiples marcas con diferentes categorías y atributos

## Funcionalidades Core

1. **Gestión de Inventario**: Control de stock, productos, categorías
2. **Gestión de Pedidos**: Creación, modificación, seguimiento de órdenes
3. **Gestión de Proveedores**: Abastecimiento y relación con proveedores
4. **Reportes**: Ventas semanales, productos por abastecer
5. **Notificaciones**: Cambios de estado, carritos abandonados

## Convenciones de Código

- Usar anotaciones de Spring Boot (@RestController, @Service, @Repository)
- DTOs para transferencia de datos
- Validaciones con Bean Validation (@Valid, @NotNull, etc.)
- Manejo de excepciones con @ControllerAdvice
- Documentación con comentarios JavaDoc
- Nombres de endpoints en inglés y snake_case para URLs
- Respuestas HTTP apropiadas (200, 201, 400, 404, 500)

## Estructura de Paquetes

- `controller`: Controladores REST
- `service`: Lógica de negocio
- `repository`: Acceso a datos
- `model`: Entidades JPA
- `dto`: Objetos de transferencia de datos
- `exception`: Manejo de excepciones
- `config`: Configuraciones

## Consideraciones Especiales

- Control de concurrencia para evitar sobreventas
- Validación de stock antes de confirmar pedidos
- Logging para auditoría
- Configuración de CORS para frontend futuro
- Manejo de transacciones con @Transactional
