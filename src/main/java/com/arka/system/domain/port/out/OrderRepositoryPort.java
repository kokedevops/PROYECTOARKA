package com.arka.system.domain.port.out;

import com.arka.system.domain.model.Order;
import com.arka.system.domain.model.Order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de órdenes.
 * Define las operaciones de acceso a datos para órdenes.
 */
public interface OrderRepositoryPort {
    
    /**
     * Guardar una orden
     * @param order Orden a guardar
     * @return Orden guardada con ID asignado
     */
    Order save(Order order);
    
    /**
     * Buscar orden por ID
     * @param id ID de la orden
     * @return Optional con la orden si existe
     */
    Optional<Order> findById(Long id);
    
    /**
     * Buscar órdenes por cliente
     * @param customerId ID del cliente
     * @return Lista de órdenes del cliente
     */
    List<Order> findByCustomerId(Long customerId);
    
    /**
     * Buscar órdenes por estado
     * @param status Estado de las órdenes
     * @return Lista de órdenes con el estado especificado
     */
    List<Order> findByStatus(OrderStatus status);
    
    /**
     * Buscar órdenes por cliente y estado
     * @param customerId ID del cliente
     * @param status Estado de las órdenes
     * @return Lista de órdenes del cliente con el estado especificado
     */
    List<Order> findByCustomerIdAndStatus(Long customerId, OrderStatus status);
    
    /**
     * Buscar órdenes creadas antes de una fecha específica
     * @param date Fecha límite
     * @return Lista de órdenes creadas antes de la fecha
     */
    List<Order> findByOrderDateBefore(LocalDateTime date);
    
    /**
     * Buscar órdenes pendientes creadas antes de una fecha específica (carritos abandonados)
     * @param date Fecha límite
     * @return Lista de órdenes pendientes creadas antes de la fecha
     */
    List<Order> findByStatusAndOrderDateBefore(OrderStatus status, LocalDateTime date);
    
    /**
     * Buscar órdenes entre fechas
     * @param startDate Fecha de inicio
     * @param endDate Fecha de fin
     * @return Lista de órdenes en el rango de fechas
     */
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Buscar órdenes por estado entre fechas
     * @param status Estado de las órdenes
     * @param startDate Fecha de inicio
     * @param endDate Fecha de fin
     * @return Lista de órdenes con el estado especificado en el rango de fechas
     */
    List<Order> findByStatusAndOrderDateBetween(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Eliminar una orden
     * @param id ID de la orden a eliminar
     */
    void deleteById(Long id);
    
    /**
     * Contar órdenes por estado
     * @param status Estado de las órdenes
     * @return Número de órdenes con el estado especificado
     */
    long countByStatus(OrderStatus status);
    
    /**
     * Contar órdenes de un cliente
     * @param customerId ID del cliente
     * @return Número de órdenes del cliente
     */
    long countByCustomerId(Long customerId);
    
    /**
     * Obtener total de ventas entre fechas
     * @param startDate Fecha de inicio
     * @param endDate Fecha de fin
     * @return Suma total de las órdenes confirmadas/entregadas en el período
     */
    java.math.BigDecimal getTotalSalesBetween(LocalDateTime startDate, LocalDateTime endDate);
}
