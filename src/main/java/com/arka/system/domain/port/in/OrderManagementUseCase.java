package com.arka.system.domain.port.in;

// TODO: Crear estos DTOs cuando se implemente la funcionalidad de órdenes
//import com.arka.system.shared.dto.OrderDTO;
//import com.arka.system.shared.dto.CreateOrderCommand;
//import com.arka.system.shared.dto.UpdateOrderCommand;
import com.arka.system.domain.model.Order.OrderStatus;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para casos de uso de gestión de órdenes.
 * Define las operaciones disponibles para la gestión de órdenes de clientes.
 * 
 * TODO: Esta interfaz será implementada cuando se completen los DTOs de Order
 */
public interface OrderManagementUseCase {
    
    // TODO: Descomentar y implementar cuando se creen los DTOs necesarios
    
    /*
    OrderDTO createOrder(CreateOrderCommand command);
    OrderDTO addItemToOrder(Long orderId, Long productId, Integer quantity);
    OrderDTO removeItemFromOrder(Long orderId, Long productId);
    OrderDTO updateItemQuantity(Long orderId, Long productId, Integer newQuantity);
    Optional<OrderDTO> getOrderById(Long orderId);
    List<OrderDTO> getOrdersByCustomer(Long customerId);
    List<OrderDTO> getOrdersByStatus(OrderStatus status);
    OrderDTO confirmOrder(Long orderId);
    OrderDTO updateOrderStatus(Long orderId, OrderStatus newStatus);
    OrderDTO cancelOrder(Long orderId);
    OrderDTO markOrderAsDelivered(Long orderId);
    java.math.BigDecimal calculateOrderTotal(Long orderId);
    List<OrderDTO> getAbandonedCarts(int hoursOld);
    */
}
