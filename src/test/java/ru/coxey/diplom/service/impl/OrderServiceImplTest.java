package ru.coxey.diplom.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.model.enums.Status;
import ru.coxey.diplom.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void getAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(new Order(), new Order()));
        List<Order> allOrders = orderService.getAllOrders();
        assertEquals(2, allOrders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getActiveOrder() {
        when(orderRepository.findByStatus(Status.IN_PROCESS)).thenReturn(List.of(new Order()));
        List<Order> allActiveOrders = orderService.getActiveOrders();
        assertEquals(1, allActiveOrders.size());
        verify(orderRepository, times(1)).findByStatus(Status.IN_PROCESS);
    }

    @Test
    void getOrderById() {
        Order order = new Order();
        order.setOrderPrice(250.0);
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));
        Order orderById = orderService.getOrderById(1);
        assertEquals(order.getOrderPrice(), orderById.getOrderPrice());
        verify(orderRepository, times(1)).findById(1);
    }

    @Test
    void getOrderById_throwIllegalArgumentException() {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> orderService.getOrderById(1));
    }

}