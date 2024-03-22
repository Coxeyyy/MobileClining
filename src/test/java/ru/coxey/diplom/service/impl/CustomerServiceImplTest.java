package ru.coxey.diplom.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.repository.CustomerRepository;
import ru.coxey.diplom.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setPhoneNumber("8999555666");
    }

    @Test
    void getAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(new Customer(), new Customer()));
        List<Customer> allCustomersFromService = customerService.getAllCustomers();
        assertEquals(2, allCustomersFromService.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void getOrdersByCustomer() {
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customer));
        when(orderRepository.findOrdersByCustomer_Id(anyInt())).thenReturn(List.of(new Order(), new Order()));
        List<Order> listOrderFromService = customerService.getOrdersByCustomer(5);
        assertEquals(2, listOrderFromService.size());
        verify(customerRepository, times(1)).findById(anyInt());
        verify(orderRepository, times(1)).findOrdersByCustomer_Id(anyInt());
    }

    @Test
    void getOrdersByCustomer_returnEmptyList() {
        when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());
        List<Order> listOrderFromService = customerService.getOrdersByCustomer(5);
        assertEquals(0, listOrderFromService.size());
    }

    @Test
    void getCustomerById() {
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customer));
        Customer customerFromService = customerService.getCustomerById(5);
        assertEquals(customer.getPhoneNumber(), customerFromService.getPhoneNumber());
        verify(customerRepository, times(1)).findById(anyInt());
    }

    @Test
    void getCustomerById_throwIllegalArgumentException() {
        when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> customerService.getCustomerById(anyInt()));
    }

    @Test
    void deleteCustomer() {
        doNothing().when(customerRepository).deleteById(anyInt());
        customerService.deleteCustomer(5);
        verify(customerRepository, times(1)).deleteById(anyInt());
    }

}