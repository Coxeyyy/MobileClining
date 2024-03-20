package ru.coxey.diplom.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.repository.EmployeeRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setLogin("test");
    }

    @Test
    void getEmployeeById() {
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(employee));
        Employee employeeFromService = employeeService.getEmployeeById(5);
        assertEquals(employee.getLogin(), employeeFromService.getLogin());
        verify(employeeRepository, times(1)).findById(anyInt());
    }

    @Test
    void getEmployeeById_throwIllegalArgumentException() {
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> employeeService.getEmployeeById(anyInt()));
    }

    @Test
    void getEmployeeByLogin() {
        when(employeeRepository.findEmployeeByLogin(anyString())).thenReturn(Optional.of(employee));
        Employee employeeFromService = employeeService.getEmployeeByLogin("test");
        assertEquals(employee.getLogin(), employeeFromService.getLogin());
        verify(employeeRepository, times(1)).findEmployeeByLogin(anyString());
    }

    @Test
    void getEmployeeByLogin_throwIllegalArgumentException() {
        when(employeeRepository.findEmployeeByLogin(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> employeeService.getEmployeeByLogin(anyString()));
    }

    @Test
    void updateEmployee() {
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(employee));
        employeeService.updateEmployee(employee, 1);
        verify(employeeRepository, times(1)).findById(anyInt());
    }

    @Test
    void updateEmployee_throw_throwIllegalArgumentException() {
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> employeeService.updateEmployee(new Employee(), 5));
    }

    @Test
    void deleteEmployee() {
        doNothing().when(employeeRepository).deleteById(anyInt());
        employeeService.deleteEmployee(5);
        verify(employeeRepository, times(1)).deleteById(anyInt());
    }

}