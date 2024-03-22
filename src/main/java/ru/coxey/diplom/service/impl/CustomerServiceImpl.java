package ru.coxey.diplom.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.repository.CustomerRepository;
import ru.coxey.diplom.repository.OrderRepository;
import ru.coxey.diplom.service.CustomerService;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final OrderRepository orderRepository;

    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** Метод возвращает всех заказчиков */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /** Метод возвращает все заказы по ID заказчика */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Order> getOrdersByCustomer(int id) {
        Optional<Customer> customerById = customerRepository.findById(id);
        if (customerById.isPresent()) {
            return orderRepository.findOrdersByCustomer_Id(customerById.get().getId());
        }
        return List.of();
    }

    /** Метод возвращает заказчика по его ID */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Customer getCustomerById(int id) {
        Optional<Customer> customerById = customerRepository.findById(id);
        if (customerById.isPresent()) {
            return customerById.get();
        } else {
            throw new IllegalArgumentException("Такого пользователя не существует");
        }
    }

    /** Метод удаляет заказчика по его ID */
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }
}
