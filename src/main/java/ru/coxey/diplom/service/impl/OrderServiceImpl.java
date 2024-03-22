package ru.coxey.diplom.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.model.enums.Status;
import ru.coxey.diplom.repository.OrderRepository;
import ru.coxey.diplom.service.OrderService;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /** Метод возвращает список заказов */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /** Метод возвращает список заказов, которые имеют статус в процессе */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Order> getActiveOrders() {
        return orderRepository.findByStatus(Status.IN_PROCESS);
    }

    /** Возвращает заказ по его ID */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Order getOrderById(int id) {
        Optional<Order> orderById = orderRepository.findById(id);
        if (orderById.isPresent()) {
            return orderById.get();
        } else {
            throw new IllegalArgumentException("Такого заказа не существует");
        }
    }
}
