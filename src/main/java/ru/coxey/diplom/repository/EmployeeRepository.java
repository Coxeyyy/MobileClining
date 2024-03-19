package ru.coxey.diplom.repository;

import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.enums.Role;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends PersonRepository<Employee> {

    List<Employee> findEmployeesByRole(Role role);

    Optional<Employee> findEmployeeByLogin(String login);
}
