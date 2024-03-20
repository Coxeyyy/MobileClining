package ru.coxey.diplom.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Order> getOrdersByCustomer(int id) {
        Optional<Customer> customerById = customerRepository.findById(id);
        if (customerById.isPresent()) {
            return orderRepository.findOrdersByCustomer_Id(customerById.get().getId());
        }
        return List.of();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Customer getCustomerById(int id) {
        Optional<Customer> customerById = customerRepository.findById(id);
        if (customerById.isPresent()) {
            return customerById.get();
        } else {
            throw new IllegalArgumentException("Такого пользователя не существует");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCustomer(Customer customer, int id) {
        Optional<Customer> customerById = customerRepository.findById(id);
        if (customerById.isPresent()) {
            Customer customerFromDB = customerById.get();
            customerFromDB.setId(customer.getId());
            customerFromDB.setLogin(customer.getLogin());
            customerFromDB.setPassword(passwordEncoder.encode(customer.getPassword()));
            customerRepository.save(customerFromDB);
        } else {
            throw new IllegalArgumentException("Такого пользователя не существует");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }
}
