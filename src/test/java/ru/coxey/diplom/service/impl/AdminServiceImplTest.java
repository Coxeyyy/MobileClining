package ru.coxey.diplom.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.repository.EmployeeRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void getAllAdmins() {
        when(employeeRepository.findEmployeesByRole(Role.ADMIN)).thenReturn(List.of(new Employee(), new Employee()));
        List<Employee> allAdminsFromService = adminService.getAllAdmins();
        assertEquals(2, allAdminsFromService.size());
        verify(employeeRepository, times(1)).findEmployeesByRole(Role.ADMIN);
    }

}