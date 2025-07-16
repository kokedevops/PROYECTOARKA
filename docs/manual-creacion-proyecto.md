# Manual: Cómo Crear el Proyecto Arka desde Cero

## Tabla de Contenidos
1. [Prerrequisitos](#prerrequisitos)
2. [Configuración del Entorno](#configuración-del-entorno)
3. [Creación del Proyecto Base](#creación-del-proyecto-base)
4. [Configuración Inicial](#configuración-inicial)
5. [Estructura de Paquetes](#estructura-de-paquetes)
6. [Modelo de Datos](#modelo-de-datos)
7. [Configuraciones del Sistema](#configuraciones-del-sistema)
8. [Controladores REST](#controladores-rest)
9. [Documentación](#documentación)
10. [Testing y Ejecución](#testing-y-ejecución)
11. [Configuración de VS Code](#configuración-de-vs-code)
12. [Siguiente Pasos](#siguientes-pasos)

---

## Prerrequisitos

### Software Requerido
- **Java 17+** (recomendado Java 21)
- **Visual Studio Code** con extensiones:
  - Extension Pack for Java
  - Spring Boot Extension Pack
  - GitHub Copilot (opcional)
- **Git** para control de versiones
- **MySQL** (para producción, opcional para desarrollo)

### Verificación de Instalación
```bash
# Verificar Java
java -version

# Verificar Git
git --version
```

---

## Configuración del Entorno

### 1. Crear Directorio del Proyecto
```bash
# Crear directorio padre
mkdir -p C:\proyectos\PROYECTOARKA
cd C:\proyectos\PROYECTOARKA
```

### 2. Abrir en VS Code
```bash
code .
```

---

## Creación del Proyecto Base

### 1. Descargar Template de Spring Boot

#### Opción A: Usando Spring Initializr Web
1. Ir a [https://start.spring.io](https://start.spring.io)
2. Configurar:
   - **Project**: Gradle - Groovy
   - **Language**: Java
   - **Spring Boot**: 3.5.3
   - **Group**: com.arka
   - **Artifact**: arka-system
   - **Name**: arka-system
   - **Description**: Sistema de gestión de inventario y ventas para Arka
   - **Package name**: com.arka.system
   - **Packaging**: Jar
   - **Java**: 21

3. **Dependencias a agregar**:
   - Spring Web
   - Spring Data JPA
   - MySQL Driver
   - Validation
   - Spring Boot Actuator
   - Spring Security

4. Hacer clic en "Generate" y descargar el ZIP

#### Opción B: Usando PowerShell (Windows)
```powershell
# Descargar usando PowerShell
Invoke-WebRequest -Uri "https://start.spring.io/starter.zip?type=gradle-project&language=java&platformVersion=3.5.3&packaging=jar&jvmVersion=21&groupId=com.arka&artifactId=arka-system&name=arka-system&description=Sistema%20de%20gestion%20de%20inventario%20y%20ventas%20para%20Arka&packageName=com.arka.system&dependencies=web,data-jpa,mysql,validation,actuator,security" -OutFile "arka-project.zip"

# Extraer proyecto
Expand-Archive -Path "arka-project.zip" -DestinationPath "." -Force

# Limpiar archivo zip
Remove-Item "arka-project.zip"
```

#### Opción C: Usando cURL (Linux/Mac)
```bash
# Descargar usando cURL
curl -X GET "https://start.spring.io/starter.zip?type=gradle-project&language=java&platformVersion=3.5.3&packaging=jar&jvmVersion=21&groupId=com.arka&artifactId=arka-system&name=arka-system&description=Sistema%20de%20gestion%20de%20inventario%20y%20ventas%20para%20Arka&packageName=com.arka.system&dependencies=web,data-jpa,mysql,validation,actuator,security" -o arka-project.zip

# Extraer proyecto
unzip arka-project.zip

# Limpiar archivo zip
rm arka-project.zip
```

---

## Configuración Inicial

### 1. Actualizar build.gradle

Editar el archivo `build.gradle` para agregar dependencias adicionales:

```gradle
plugins {
	id 'java'
	id 'org.springframework.boot' version '3.5.3'
	id 'io.spring.dependency-management' version '1.1.7'
}

group = 'com.arka'
version = '0.0.1-SNAPSHOT'

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-security'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	
	// Database
	runtimeOnly 'com.mysql:mysql-connector-j'
	runtimeOnly 'com.h2database:h2' // Para desarrollo y testing
	
	// Lombok for reducing boilerplate code
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
	
	// Documentation
	implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0'
	
	// Testing
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testImplementation 'org.springframework.security:spring-security-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
	testCompileOnly 'org.projectlombok:lombok'
	testAnnotationProcessor 'org.projectlombok:lombok'
}

tasks.named('test') {
	useJUnitPlatform()
}
```

### 2. Configurar application.properties

Reemplazar el contenido de `src/main/resources/application.properties`:

```properties
# Configuración de la aplicación Arka
spring.application.name=arka-system

# Server Configuration
server.port=8080
server.servlet.context-path=/api

# Database Configuration - H2 for Development
spring.profiles.active=dev
spring.datasource.url=jdbc:h2:mem:arka_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
spring.jpa.open-in-view=false

# Logging Configuration
logging.level.com.arka.system=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG

# Actuator Configuration
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always

# Jackson Configuration
spring.jackson.serialization.fail-on-empty-beans=false
spring.jackson.serialization.write-dates-as-timestamps=false

# Application Information
info.app.name=Arka System
info.app.description=Sistema de gestión de inventario y ventas para Arka
info.app.version=1.0.0
```

---

## Estructura de Paquetes

### 1. Crear Directorios de Paquetes

```bash
# Crear estructura de directorios
mkdir -p src/main/java/com/arka/system/controller
mkdir -p src/main/java/com/arka/system/service
mkdir -p src/main/java/com/arka/system/repository
mkdir -p src/main/java/com/arka/system/model
mkdir -p src/main/java/com/arka/system/dto
mkdir -p src/main/java/com/arka/system/exception
mkdir -p src/main/java/com/arka/system/config
```

### 2. Estructura Final del Proyecto
```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── arka/
│   │           └── system/
│   │               ├── ArkaSystemApplication.java
│   │               ├── controller/
│   │               ├── service/
│   │               ├── repository/
│   │               ├── model/
│   │               ├── dto/
│   │               ├── exception/
│   │               └── config/
│   └── resources/
│       ├── application.properties
│       └── static/
└── test/
```

---

## Modelo de Datos

### 1. Crear Entidad Category

Crear `src/main/java/com/arka/system/model/Category.java`:

```java
package com.arka.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa una categoría de productos en el sistema Arka.
 * Las categorías organizan los productos por tipo (ej: Procesadores, Tarjetas gráficas, etc.)
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 100, message = "El nombre de la categoría no puede exceder 100 caracteres")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;
}
```

### 2. Crear Entidad Product

Crear `src/main/java/com/arka/system/model/Product.java`:

```java
package com.arka.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa un producto en el sistema Arka.
 * Incluye información detallada del producto, stock, precios y relaciones.
 */
@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_product_sku", columnList = "sku", unique = true),
    @Index(name = "idx_product_brand", columnList = "brand"),
    @Index(name = "idx_product_category", columnList = "category_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El SKU es obligatorio")
    @Size(max = 50, message = "El SKU no puede exceder 50 caracteres")
    @Column(name = "sku", nullable = false, unique = true, length = 50)
    private String sku;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    @Column(name = "description", length = 1000)
    private String description;
    
    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 100, message = "La marca no puede exceder 100 caracteres")
    @Column(name = "brand", nullable = false, length = 100)
    private String brand;
    
    @NotNull(message = "El precio de compra es obligatorio")
    @PositiveOrZero(message = "El precio de compra debe ser positivo")
    @Column(name = "purchase_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal purchasePrice;
    
    @NotNull(message = "El precio de venta es obligatorio")
    @PositiveOrZero(message = "El precio de venta debe ser positivo")
    @Column(name = "sale_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal salePrice;
    
    @NotNull(message = "El stock actual es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0;
    
    @NotNull(message = "El stock mínimo es obligatorio")
    @PositiveOrZero(message = "El stock mínimo debe ser positivo")
    @Column(name = "minimum_stock", nullable = false)
    private Integer minimumStock = 0;
    
    @Column(name = "weight_kg", precision = 8, scale = 3)
    private BigDecimal weight;
    
    @Size(max = 100, message = "Las dimensiones no pueden exceder 100 caracteres")
    @Column(name = "dimensions", length = 100)
    private String dimensions;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
    
    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductAttribute> attributes;
}
```

### 3. Crear Entidad ProductAttribute

Crear `src/main/java/com/arka/system/model/ProductAttribute.java`:

```java
package com.arka.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entidad que representa los atributos adicionales de un producto.
 * Permite especificar características específicas como color, modelo, etc.
 */
@Entity
@Table(name = "product_attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del atributo es obligatorio")
    @Size(max = 100, message = "El nombre del atributo no puede exceder 100 caracteres")
    @Column(name = "attribute_name", nullable = false, length = 100)
    private String attributeName;
    
    @NotBlank(message = "El valor del atributo es obligatorio")
    @Size(max = 200, message = "El valor del atributo no puede exceder 200 caracteres")
    @Column(name = "attribute_value", nullable = false, length = 200)
    private String attributeValue;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
```

### 4. Crear Entidad Customer

Crear `src/main/java/com/arka/system/model/Customer.java`:

```java
package com.arka.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa un cliente del sistema Arka.
 * Los clientes son almacenes que compran productos en grandes cantidades.
 */
@Entity
@Table(name = "customers", indexes = {
    @Index(name = "idx_customer_email", columnList = "email", unique = true),
    @Index(name = "idx_customer_document", columnList = "document_number", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(max = 200, message = "El nombre de la empresa no puede exceder 200 caracteres")
    @Column(name = "company_name", nullable = false, length = 200)
    private String companyName;
    
    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 20, message = "El número de documento no puede exceder 20 caracteres")
    @Column(name = "document_number", nullable = false, unique = true, length = 20)
    private String documentNumber;
    
    @NotBlank(message = "El email es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Email inválido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Size(max = 300, message = "La dirección no puede exceder 300 caracteres")
    @Column(name = "address", length = 300)
    private String address;
    
    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    @Column(name = "city", nullable = false, length = 100)
    private String city;
    
    @NotBlank(message = "El país es obligatorio")
    @Size(max = 50, message = "El país no puede exceder 50 caracteres")
    @Column(name = "country", nullable = false, length = 50)
    private String country;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;
}
```

### 5. Crear Entidad Supplier

Crear `src/main/java/com/arka/system/model/Supplier.java`:

```java
package com.arka.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa un proveedor en el sistema Arka.
 * Los proveedores suministran productos para el inventario.
 */
@Entity
@Table(name = "suppliers", indexes = {
    @Index(name = "idx_supplier_email", columnList = "email", unique = true),
    @Index(name = "idx_supplier_document", columnList = "document_number", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(max = 200, message = "El nombre de la empresa no puede exceder 200 caracteres")
    @Column(name = "company_name", nullable = false, length = 200)
    private String companyName;
    
    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 20, message = "El número de documento no puede exceder 20 caracteres")
    @Column(name = "document_number", nullable = false, unique = true, length = 20)
    private String documentNumber;
    
    @NotBlank(message = "El email es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Email inválido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Size(max = 300, message = "La dirección no puede exceder 300 caracteres")
    @Column(name = "address", length = 300)
    private String address;
    
    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    @Column(name = "city", nullable = false, length = 100)
    private String city;
    
    @NotBlank(message = "El país es obligatorio")
    @Size(max = 50, message = "El país no puede exceder 50 caracteres")
    @Column(name = "country", nullable = false, length = 50)
    private String country;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchaseOrder> purchaseOrders;
}
```

### 6. Crear Entidad Order

Crear `src/main/java/com/arka/system/model/Order.java`:

```java
package com.arka.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa una orden de compra (pedido) de un cliente.
 */
@Entity
@Table(name = "orders", indexes = {
    @Index(name = "idx_order_customer", columnList = "customer_id"),
    @Index(name = "idx_order_status", columnList = "status"),
    @Index(name = "idx_order_date", columnList = "order_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING;
    
    @NotNull(message = "El total es obligatorio")
    @PositiveOrZero(message = "El total debe ser positivo")
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Column(name = "notes", length = 500)
    private String notes;
    
    @CreationTimestamp
    @Column(name = "order_date", nullable = false, updatable = false)
    private LocalDateTime orderDate;
    
    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;
    
    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
    
    public enum OrderStatus {
        PENDING,        // Pendiente
        CONFIRMED,      // Confirmado
        IN_PREPARATION, // En preparación
        SHIPPED,        // Enviado
        DELIVERED,      // Entregado
        CANCELLED       // Cancelado
    }
}
```

### 7. Crear Entidad OrderItem

Crear `src/main/java/com/arka/system/model/OrderItem.java`:

```java
package com.arka.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * Entidad que representa un item/línea de una orden de compra.
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La orden es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @NotNull(message = "El precio unitario es obligatorio")
    @PositiveOrZero(message = "El precio unitario debe ser positivo")
    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;
    
    @NotNull(message = "El subtotal es obligatorio")
    @PositiveOrZero(message = "El subtotal debe ser positivo")
    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;
    
    /**
     * Calcula el subtotal automáticamente basado en cantidad y precio unitario
     */
    @PrePersist
    @PreUpdate
    private void calculateSubtotal() {
        if (quantity != null && unitPrice != null) {
            this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
```

### 8. Crear Entidad PurchaseOrder

Crear `src/main/java/com/arka/system/model/PurchaseOrder.java`:

```java
package com.arka.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa una orden de compra a proveedor.
 */
@Entity
@Table(name = "purchase_orders", indexes = {
    @Index(name = "idx_purchase_order_supplier", columnList = "supplier_id"),
    @Index(name = "idx_purchase_order_status", columnList = "status"),
    @Index(name = "idx_purchase_order_date", columnList = "order_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El proveedor es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;
    
    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PurchaseOrderStatus status = PurchaseOrderStatus.DRAFT;
    
    @NotNull(message = "El total es obligatorio")
    @PositiveOrZero(message = "El total debe ser positivo")
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Column(name = "notes", length = 500)
    private String notes;
    
    @CreationTimestamp
    @Column(name = "order_date", nullable = false, updatable = false)
    private LocalDateTime orderDate;
    
    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;
    
    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchaseOrderItem> orderItems;
    
    public enum PurchaseOrderStatus {
        DRAFT,      // Borrador
        SENT,       // Enviado
        CONFIRMED,  // Confirmado
        RECEIVED,   // Recibido
        CANCELLED   // Cancelado
    }
}
```

### 9. Crear Entidad PurchaseOrderItem

Crear `src/main/java/com/arka/system/model/PurchaseOrderItem.java`:

```java
package com.arka.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * Entidad que representa un item de una orden de compra a proveedor.
 */
@Entity
@Table(name = "purchase_order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La orden de compra es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;
    
    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @NotNull(message = "El precio unitario es obligatorio")
    @PositiveOrZero(message = "El precio unitario debe ser positivo")
    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;
    
    @NotNull(message = "El subtotal es obligatorio")
    @PositiveOrZero(message = "El subtotal debe ser positivo")
    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "received_quantity")
    private Integer receivedQuantity = 0;
    
    /**
     * Calcula el subtotal automáticamente basado en cantidad y precio unitario
     */
    @PrePersist
    @PreUpdate
    private void calculateSubtotal() {
        if (quantity != null && unitPrice != null) {
            this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
```

---

## Configuraciones del Sistema

### 1. Configuración de Seguridad

Crear `src/main/java/com/arka/system/config/SecurityConfig.java`:

```java
package com.arka.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración de seguridad para el sistema Arka.
 * Configura autenticación, autorización y CORS.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Endpoints públicos
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // H2 Console para desarrollo
                .requestMatchers("/h2-console/**").permitAll()
                // Todos los demás endpoints requieren autenticación
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions().disable()); // Para H2 Console

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

---

## Controladores REST

### 1. Controlador Público

Crear `src/main/java/com/arka/system/controller/PublicController.java`:

```java
package com.arka.system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador público para endpoints que no requieren autenticación.
 */
@RestController
@RequestMapping("/public")
public class PublicController {

    /**
     * Endpoint de salud del sistema
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "Arka System");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    /**
     * Información básica del sistema
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "Sistema Arka");
        response.put("description", "Sistema de gestión de inventario y ventas para distribuidora de accesorios PC");
        response.put("company", "Arka Colombia");
        response.put("version", "1.0.0");
        response.put("features", new String[]{
            "Gestión de inventario",
            "Gestión de pedidos",
            "Gestión de proveedores",
            "Reportes de ventas",
            "Control de stock",
            "Notificaciones"
        });
        return ResponseEntity.ok(response);
    }
}
```

---

## Documentación

### 1. Configurar GitHub Copilot

Crear `.github/copilot-instructions.md`:

```markdown
# Instrucciones para GitHub Copilot - Proyecto Arka

<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

## Descripción del Proyecto

Arka es un sistema de gestión de inventario y ventas para una empresa distribuidora de accesorios para PC. El sistema está desarrollado con:

- **Backend**: Spring Boot 3.5.3 con Java 21
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
```

### 2. Crear README.md

Crear `README.md` (usar el contenido del archivo README.md que creé anteriormente)

---

## Testing y Ejecución

### 1. Compilar el Proyecto

```bash
# Windows
.\gradlew build

# Linux/Mac
./gradlew build
```

### 2. Ejecutar la Aplicación

```bash
# Windows
.\gradlew bootRun

# Linux/Mac
./gradlew bootRun
```

### 3. Verificar que Funciona

Abrir navegador en:
- **API Health**: http://localhost:8080/api/public/health
- **API Info**: http://localhost:8080/api/public/info
- **H2 Console**: http://localhost:8080/api/h2-console
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html

### 4. Ejecutar Tests

```bash
# Windows
.\gradlew test

# Linux/Mac
./gradlew test
```

---

## Configuración de VS Code

### 1. Crear Tareas de VS Code

Crear `.vscode/tasks.json`:

```json
{
	"version": "2.0.0",
	"tasks": [
		{
			"label": "Build Arka System",
			"type": "shell",
			"command": "./gradlew",
			"args": [
				"build"
			],
			"group": "build",
			"isBackground": false,
			"problemMatcher": [
				"$gradle"
			]
		},
		{
			"label": "Run Arka System",
			"type": "shell",
			"command": "./gradlew",
			"args": [
				"bootRun"
			],
			"group": {
				"kind": "build",
				"isDefault": true
			},
			"isBackground": true,
			"problemMatcher": [
				"$gradle"
			],
			"presentation": {
				"echo": true,
				"reveal": "always",
				"focus": false,
				"panel": "new"
			}
		},
		{
			"label": "Clean Arka System",
			"type": "shell",
			"command": "./gradlew",
			"args": [
				"clean"
			],
			"group": "build",
			"isBackground": false,
			"problemMatcher": [
				"$gradle"
			]
		},
		{
			"label": "Test Arka System",
			"type": "shell",
			"command": "./gradlew",
			"args": [
				"test"
			],
			"group": "test",
			"isBackground": false,
			"problemMatcher": [
				"$gradle"
			]
		}
	]
}
```

### 2. Configuración de Lanzamiento

Crear `.vscode/launch.json`:

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Debug Arka System",
            "request": "launch",
            "mainClass": "com.arka.system.ArkaSystemApplication",
            "projectName": "arka-system",
            "args": [],
            "vmArgs": "-Dspring.profiles.active=dev"
        }
    ]
}
```

---

## Siguientes Pasos

### 1. Implementar Repositorios

Crear interfaces en `src/main/java/com/arka/system/repository/`:

```java
// CategoryRepository.java
package com.arka.system.repository;

import com.arka.system.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Métodos personalizados aquí
}
```

### 2. Implementar Servicios

Crear clases en `src/main/java/com/arka/system/service/`:

```java
// CategoryService.java
package com.arka.system.service;

import com.arka.system.model.Category;
import com.arka.system.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    // Lógica de negocio aquí
}
```

### 3. Implementar Controladores REST

Crear controladores completos en `src/main/java/com/arka/system/controller/`:

```java
// CategoryController.java
package com.arka.system.controller;

import com.arka.system.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    // Endpoints REST aquí
}
```

### 4. Crear DTOs

Crear objetos de transferencia de datos en `src/main/java/com/arka/system/dto/`:

```java
// CategoryDTO.java
package com.arka.system.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CategoryDTO {
    private Long id;
    
    @NotBlank
    private String name;
    
    private String description;
    private Boolean active;
}
```

### 5. Manejo de Excepciones

Crear manejador global en `src/main/java/com/arka/system/exception/`:

```java
// GlobalExceptionHandler.java
package com.arka.system.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        // Manejo de errores
    }
}
```

---

## Comandos Útiles

### Gradle
```bash
# Limpiar proyecto
./gradlew clean

# Compilar
./gradlew build

# Ejecutar aplicación
./gradlew bootRun

# Ejecutar tests
./gradlew test

# Ver dependencias
./gradlew dependencies

# Ver tareas disponibles
./gradlew tasks
```

### Git
```bash
# Inicializar repositorio
git init

# Agregar archivos
git add .

# Primer commit
git commit -m "Initial commit: Arka system setup"

# Conectar con repositorio remoto
git remote add origin <url-del-repositorio>

# Subir cambios
git push -u origin main
```

---

## Configuración para Producción

### 1. Crear application-prod.properties

```properties
# Configuración de producción
spring.profiles.active=prod

# Database Configuration - MySQL Production
spring.datasource.url=jdbc:mysql://localhost:3306/arka_db?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Logging Configuration
logging.level.com.arka.system=INFO
logging.level.org.springframework.web=WARN
logging.level.org.hibernate.SQL=WARN

# Security
spring.security.require-ssl=true
```

### 2. Crear Dockerfile

```dockerfile
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/arka-system-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 3. Crear docker-compose.yml

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_USERNAME=arka_user
      - DB_PASSWORD=arka_password
    depends_on:
      - db

  db:
    image: mysql:8.0
    environment:
      - MYSQL_DATABASE=arka_db
      - MYSQL_USER=arka_user
      - MYSQL_PASSWORD=arka_password
      - MYSQL_ROOT_PASSWORD=root_password
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

volumes:
  mysql_data:
```

---

## Troubleshooting

### Problemas Comunes

1. **Error de Java Version**
   ```bash
   # Verificar versión
   java -version
   
   # Cambiar en build.gradle si es necesario
   java {
       toolchain {
           languageVersion = JavaLanguageVersion.of(17) // o 21
       }
   }
   ```

2. **Problemas de Puerto**
   ```properties
   # Cambiar puerto en application.properties
   server.port=8081
   ```

3. **Problemas de Base de Datos**
   ```properties
   # Verificar configuración de H2
   spring.h2.console.enabled=true
   spring.datasource.url=jdbc:h2:mem:arka_db
   ```

4. **Problemas de Dependencias**
   ```bash
   # Limpiar cache de Gradle
   ./gradlew clean
   ./gradlew build --refresh-dependencies
   ```

---

## Recursos Adicionales

### Documentación Oficial
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Security](https://docs.spring.io/spring-security/reference/)

### Herramientas Útiles
- [Spring Initializr](https://start.spring.io)
- [H2 Database Console](http://localhost:8080/api/h2-console)
- [Swagger UI](http://localhost:8080/api/swagger-ui.html)

---

**¡Felicidades!** 🎉 Has creado exitosamente el proyecto Arka desde cero. El sistema está listo para desarrollo y puedes comenzar a implementar las funcionalidades específicas del negocio.
