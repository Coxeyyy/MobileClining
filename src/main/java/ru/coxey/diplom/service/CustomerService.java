package ru.coxey.diplom.service;

import org.springframework.transaction.annotation.Transactional;
import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.model.Order;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomers();

    List<Order> getOrdersByCustomer(int id);

    Customer getCustomerById(int id);

    @Transactional
    void deleteCustomer(int id);
}
