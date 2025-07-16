package com.arka.system.infrastructure.adapter.in.rest;

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
