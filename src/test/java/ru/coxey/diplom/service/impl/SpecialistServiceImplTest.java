package ru.coxey.diplom.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.model.Person;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.model.enums.Status;
import ru.coxey.diplom.repository.EmployeeRepository;
import ru.coxey.diplom.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
@WebMvcTest(SpecialistServiceImpl.class)
class SpecialistServiceImplTest {

//    @InjectMocks
    @Autowired
    private SpecialistServiceImpl specialistService;

    @MockBean
    private EmployeeRepository employeeRepository;
    @MockBean
    private OrderRepository orderRepository;

    @Mock
    private SpecialistServiceImpl mockService;

    @Test
    void getAllSpecialists() {
        when(employeeRepository.findEmployeesByRole(Role.SPECIALIST)).thenReturn(List.of(new Employee(), new Employee()));
        List<Employee> allSpecialists = specialistService.getAllSpecialists();
        assertEquals(2, allSpecialists.size());
    }

    @Test
    void setSpecialistToOrder() {
        Order order = new Order();
        Employee specialist = new Employee();
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));
        when(employeeRepository.findById(Integer.parseInt("1"))).thenReturn(Optional.of(specialist));
        specialistService.setSpecialistToOrder(1, "1");
        verify(orderRepository, times(1)).findById(1);
        verify(employeeRepository, times(1)).findById(Integer.parseInt("1"));
    }

    @Test
    void setSpecialistToOrder_throwIllegalArgumentException() {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(employeeRepository.findById(Integer.parseInt("1"))).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> specialistService.setSpecialistToOrder(1, "1"));
    }

    @Test
    void getOrdersBySpecialist() {
        Employee specialist = new Employee();
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(specialist));
        when(orderRepository.findOrdersByEmployee_Id(anyInt())).thenReturn(List.of(new Order(), new Order()));
        specialistService.getOrdersBySpecialist(1);
        verify(employeeRepository, times(1)).findById(1);
        verify(orderRepository, times(1)).findOrdersByEmployee_Id(0);
    }

    @Test
    void getOrdersBySpecialist_returnNullListOrders() {
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());
        List<Order> ordersBySpecialist = specialistService.getOrdersBySpecialist(1);
        verify(employeeRepository, times(1)).findById(1);
        assertNull(ordersBySpecialist);
    }

    @Test
    void getOrders() {
        Employee specialist = new Employee();

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(employeeRepository.findByLogin(anyString())).thenReturn(Optional.of(specialist));
        when(specialistService.getPerson()).thenReturn(Optional.of(specialist));
        when(orderRepository.findOrdersByEmployee_Id(anyInt())).thenReturn(List.of(new Order(), new Order()));
        List<Order> ordersFromService = specialistService.getOrders();
        assertEquals(2, ordersFromService.size());
    }

    @Test
    void getOrders_returnEmptyList() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(specialistService.getPerson()).thenReturn(Optional.empty());
        List<Order> ordersFromService = specialistService.getOrders();
        assertEquals(0, ordersFromService.size());
    }

    @Test
    void getActiveOrders() {
        Employee specialist = new Employee();

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(employeeRepository.findByLogin(anyString())).thenReturn(Optional.of(specialist));
        when(specialistService.getPerson()).thenReturn(Optional.of(specialist));
        when(orderRepository.findOrdersByEmployee_id_AndStatus(anyInt(), eq(Status.IN_PROCESS)))
                .thenReturn(List.of(new Order(), new Order()));
        List<Order> ordersFromService = specialistService.getActiveOrders();
        assertEquals(2, ordersFromService.size());

    }

    @Test
    void getActiveOrders_returnEmptyList() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(employeeRepository.findByLogin(anyString())).thenReturn(Optional.empty());
        when(specialistService.getPerson()).thenReturn(Optional.empty());
        List<Order> ordersFromService = specialistService.getActiveOrders();
        assertEquals(0, ordersFromService.size());
    }

    @Test
    void getOrderById() {
        Order order = new Order();
        order.setOrderPrice(500.0);
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));
        Order orderByIdFromService = specialistService.getOrderById(1);
        assertEquals(order.getOrderPrice(), orderByIdFromService.getOrderPrice());
    }

    @Test
    void getOrderById_throwIllegalArgumentException() {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> specialistService.getOrderById(1));
    }

    @Test
    void setStatusToOrder() {
        Order order = new Order();
        order.setOrderPrice(500.0);
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));
        specialistService.setStatusToOrder(1, "READY");
        verify(orderRepository, times(1)).findById(1);
    }

    @Test
    void setStatusToOrder_throwIllegalArgumentException() {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> specialistService.setStatusToOrder(1, "READY"));
    }

}