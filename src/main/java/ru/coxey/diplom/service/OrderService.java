package ru.coxey.diplom.service;

import ru.coxey.diplom.model.Order;

import java.util.List;


public interface OrderService {

    List<Order> getAllOrders();

    List<Order> getActiveOrders();

    Order getOrderById(int id);
}
