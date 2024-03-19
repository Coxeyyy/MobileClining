package ru.coxey.diplom.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.model.Person;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.model.enums.Status;
import ru.coxey.diplom.repository.EmployeeRepository;
import ru.coxey.diplom.repository.OrderRepository;
import ru.coxey.diplom.service.SpecialistService;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialistServiceImpl implements SpecialistService {

    private final EmployeeRepository employeeRepository;

    public SpecialistServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Employee> getAllSpecialists() {
        return employeeRepository.findEmployeesByRole(Role.SPECIALIST);
    }

}
