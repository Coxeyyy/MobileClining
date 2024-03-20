package ru.coxey.diplom.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.repository.EmployeeRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private MainServiceImpl mainService;

    private Authentication authentication;

    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
    }

    @Test
    void getEmployee() {
        Employee employee = new Employee();
        employee.setLogin("testLogin");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(employeeRepository.findEmployeeByLogin(securityContext.getAuthentication().getName())).thenReturn(Optional.of(employee));
        Employee employeeFromService = mainService.getEmployee();
        assertEquals(employee.getLogin(), employeeFromService.getLogin());
    }

    @Test
    void getEmployee_throwIllegalArgumentException() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(employeeRepository.findEmployeeByLogin(securityContext.getAuthentication().getName())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> mainService.getEmployee());
    }

}