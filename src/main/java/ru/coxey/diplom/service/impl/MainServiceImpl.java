package ru.coxey.diplom.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.repository.EmployeeRepository;
import ru.coxey.diplom.service.MainService;

import java.util.Optional;

@Service
public class MainServiceImpl implements MainService {
    private final EmployeeRepository employeeRepository;

    public MainServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee getEmployee() {
        String loginEmployee = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Employee> byLogin = employeeRepository.findEmployeeByLogin(loginEmployee);
        return byLogin.orElse(null);
    }
}
