# Glosario - Lenguaje Ubicuo del Dominio Arka

## Contextos Delimitados (Bounded Contexts)

### 1. **Contexto de Gestión de Productos (Product Management)**
### 2. **Contexto de Gestión de Pedidos (Order Management)** 
### 3. **Contexto de Gestión de Inventario (Inventory Management)**

---

## Términos del Dominio

### **Producto (Product)**
**Definición**: Artículo físico de tecnología (accesorios para PC) que la empresa distribuye.
- **Propiedades**: SKU único, nombre, descripción, marca, categoría, precios, stock, atributos técnicos
- **Reglas de Negocio**: 
  - Debe tener un SKU único en el sistema
  - Debe pertenecer a una categoría válida
  - Precio de venta debe ser mayor al precio de compra
  - Stock mínimo debe ser positivo

### **SKU (Stock Keeping Unit)**
**Definición**: Código único alfanumérico que identifica un producto específico en el inventario.
- **Formato**: Generalmente 8-12 caracteres (ej: "KB-LOG-001")
- **Unicidad**: No puede repetirse en el sistema
- **Uso**: Identificación rápida en pedidos y control de inventario

### **Categoría (Category)**
**Definición**: Agrupación lógica de productos por tipo o función tecnológica.
- **Ejemplos**: "Teclados", "Mouse", "Auriculares", "Cámaras Web", "Cables"
- **Jerarquía**: Puede tener subcategorías
- **Atributos**: Nombre, descripción, estado activo/inactivo

### **Atributo de Producto (Product Attribute)**
**Definición**: Característica técnica específica que describe propiedades del producto.
- **Ejemplos**: "Conectividad: Bluetooth", "Compatibilidad: Windows/Mac", "Color: Negro"
- **Estructura**: Par clave-valor (nombre_atributo: valor_atributo)
- **Uso**: Filtrado, búsqueda y especificaciones técnicas

### **Inventario (Inventory)**
**Definición**: Control y seguimiento de la cantidad disponible de productos en stock.
- **Stock Actual**: Cantidad física disponible
- **Stock Mínimo**: Nivel crítico que requiere reabastecimiento
- **Estado**: Disponible, Agotado, Descontinuado

### **Pedido/Orden (Order)**
**Definición**: Solicitud formal de compra realizada por un cliente para adquirir productos.
- **Estados**: Pendiente → Confirmado → En Preparación → Enviado → Entregado → Cancelado
- **Composición**: Cliente, fecha, items de pedido, total, notas
- **Validaciones**: Stock disponible, cliente válido, montos correctos

### **Item de Pedido (Order Item)**
**Definición**: Línea individual dentro de un pedido que especifica un producto y cantidad.
- **Propiedades**: Producto, cantidad solicitada, precio unitario, subtotal
- **Cálculos**: Subtotal = cantidad × precio_unitario

### **Cliente (Customer)**
**Definición**: Empresa o almacén que realiza compras al por mayor a la distribuidora.
- **Tipo**: Empresas/almacenes (B2B), no consumidores finales
- **Datos**: Razón social, NIT/RUT, contacto, dirección, país
- **Relación**: Un cliente puede tener múltiples pedidos

### **Proveedor (Supplier)**
**Definición**: Empresa fabricante o mayorista que suministra productos a la distribuidora.
- **Relación**: Proporciona productos a través de órdenes de compra
- **Datos**: Empresa, contacto, condiciones comerciales, productos suministrados

### **Orden de Compra (Purchase Order)**
**Definición**: Pedido realizado por la distribuidora a un proveedor para reabastecer inventario.
- **Flujo**: Borrador → Enviada → Confirmada → Recibida → Cancelada
- **Propósito**: Controlar el flujo de reabastecimiento

### **Reabastecimiento (Replenishment)**
**Definición**: Proceso de renovar el stock de productos cuando llegan al stock mínimo.
- **Trigger**: Stock actual ≤ Stock mínimo
- **Acción**: Generar orden de compra al proveedor
- **Objetivo**: Mantener disponibilidad continua

### **Distribución (Distribution)**
**Definición**: Actividad principal del negocio de revender productos de tecnología.
- **Modelo**: Compra al por mayor, venta a almacenes/tiendas
- **Geografía**: Colombia (base), expansión a Ecuador, Perú, Chile
- **Valor**: Logística, disponibilidad, servicio al cliente B2B

---

## Reglas de Negocio del Dominio

### **Gestión de Productos**
1. **Unicidad de SKU**: No pueden existir dos productos con el mismo SKU
2. **Precios**: El precio de venta debe ser mayor al precio de compra  
3. **Stock Mínimo**: Debe ser un valor positivo mayor a cero
4. **Categorización**: Todo producto debe pertenecer a una categoría activa

### **Gestión de Pedidos**
1. **Validación de Stock**: No se puede confirmar un pedido sin stock suficiente
2. **Cliente Activo**: Solo clientes activos pueden realizar pedidos
3. **Cálculo de Totales**: Total pedido = Σ(cantidad × precio_unitario) por item
4. **Estados Válidos**: Un pedido no puede pasar de "Entregado" a "En Preparación"

### **Gestión de Inventario**
1. **Control de Stock**: Stock no puede ser negativo
2. **Alerta de Reabastecimiento**: Se genera cuando stock ≤ stock_mínimo
3. **Movimientos**: Todo cambio de stock debe ser auditado
4. **Disponibilidad**: Solo productos activos están disponibles para venta

### **Gestión de Clientes**
1. **Documento Único**: NIT/RUT debe ser único por país
2. **Email Único**: Un email no puede estar asociado a múltiples clientes
3. **Geolocalización**: Cliente debe tener país, ciudad y dirección válidos

---

## Eventos del Dominio

### **ProductEvents**
- `ProductCreated`: Nuevo producto agregado al catálogo
- `ProductUpdated`: Información de producto modificada  
- `StockLevelChanged`: Cambio en cantidad de stock
- `LowStockAlert`: Stock alcanzó nivel mínimo

### **OrderEvents**
- `OrderCreated`: Nuevo pedido registrado
- `OrderConfirmed`: Pedido validado y confirmado
- `OrderShipped`: Pedido enviado al cliente
- `OrderDelivered`: Pedido entregado exitosamente
- `OrderCancelled`: Pedido cancelado

### **InventoryEvents**
- `StockMovement`: Registro de entrada/salida de inventario
- `ReplenishmentRequired`: Necesidad de reabastecimiento detectada
- `PurchaseOrderCreated`: Orden de compra generada

---

## Agregados del Dominio

### **Product Aggregate**
- **Raíz**: Product
- **Entidades**: ProductAttribute (collection)
- **Responsabilidad**: Gestionar información y atributos del producto

### **Order Aggregate**  
- **Raíz**: Order
- **Entidades**: OrderItem (collection)
- **Responsabilidad**: Gestionar pedido completo y sus items

### **Customer Aggregate**
- **Raíz**: Customer  
- **Responsabilidad**: Gestionar información del cliente

### **Inventory Aggregate**
- **Raíz**: Product (desde perspectiva de stock)
- **Responsabilidad**: Controlar niveles de inventario y movimientos

---

## Value Objects

### **Money (Dinero)**
- **Propiedades**: amount (BigDecimal), currency (String)
- **Inmutable**: No puede cambiar después de creación
- **Operaciones**: add, subtract, multiply, comparisons

### **Address (Dirección)**  
- **Propiedades**: street, city, country, postalCode
- **Validaciones**: Formato según país

### **Email**
- **Validación**: Formato RFC válido
- **Unicidad**: En contexto de clientes

### **SKU**
- **Formato**: Patrón específico de la empresa
- **Validación**: Alfanumérico, longitud específica

---

## Casos de Uso (Use Cases)

### **Gestión de Productos**
- `CreateProduct`: Crear nuevo producto en catálogo
- `UpdateProduct`: Modificar información de producto existente  
- `FindProductBySku`: Buscar producto por código SKU
- `ListProductsByCategory`: Obtener productos de una categoría
- `CheckStockAvailability`: Verificar disponibilidad de stock

### **Gestión de Pedidos**
- `CreateOrder`: Registrar nuevo pedido de cliente
- `ConfirmOrder`: Validar y confirmar pedido
- `UpdateOrderStatus`: Cambiar estado de pedido
- `CalculateOrderTotal`: Calcular total del pedido
- `CancelOrder`: Cancelar pedido antes del envío

### **Gestión de Inventario**
- `UpdateStock`: Actualizar cantidad en inventario
- `CheckLowStock`: Identificar productos con stock bajo
- `GenerateReplenishmentOrder`: Crear orden de reabastecimiento
- `RecordStockMovement`: Registrar movimiento de inventario

Este glosario representa el **lenguaje ubicuo** compartido entre el equipo de desarrollo y los expertos del dominio, asegurando una comunicación clara y consistente en todo el proyecto.
