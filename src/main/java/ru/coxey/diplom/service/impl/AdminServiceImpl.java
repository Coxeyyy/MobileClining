package ru.coxey.diplom.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.repository.EmployeeRepository;
import ru.coxey.diplom.service.AdminService;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final EmployeeRepository employeeRepository;

    public AdminServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /** Метод возвращает список администраторов из таблицы users */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Employee> getAllAdmins() {
        return employeeRepository.findEmployeesByRole(Role.ADMIN);
    }
}
