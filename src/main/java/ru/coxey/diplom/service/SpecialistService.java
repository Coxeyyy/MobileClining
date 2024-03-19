package ru.coxey.diplom.service;

import org.springframework.transaction.annotation.Transactional;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.Order;

import java.util.List;


public interface SpecialistService {

    List<Employee> getAllSpecialists();

    @Transactional
    void setSpecialistToOrder(int id, String specialistId);

    List<Order> getOrdersBySpecialist(int id);

    List<Order> getOrders();

    List<Order> getActiveOrders();

    Order getOrderById(int id);

    @Transactional
    void setStatusToOrder(int id, String statusOrder);
}
