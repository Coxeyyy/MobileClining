package ru.coxey.diplom.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.repository.EmployeeRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Test
    void registerEmployee() {
        Employee employee = new Employee();
        employee.setLogin("test");
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        registrationService.registerEmployee(employee);
        verify(employeeRepository, times(1)).save(employee);
    }

}