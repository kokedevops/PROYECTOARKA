package com.arka.system.infrastructure.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuración para el wiring de dependencias de la arquitectura hexagonal.
 * 
 * Los adaptadores de persistencia ya están marcados con @Component/@Repository,
 * por lo que Spring los registra automáticamente como beans.
 * 
 * Esta clase queda preparada para configuraciones adicionales específicas
 * de la arquitectura hexagonal cuando sean necesarias.
 */
@Configuration
public class HexagonalArchitectureConfig {
    
    // Los beans se crean automáticamente a través de las anotaciones @Component
    // en los adaptadores de infraestructura
    
}
