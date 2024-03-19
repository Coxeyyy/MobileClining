package ru.coxey.diplom.service;

import org.springframework.transaction.annotation.Transactional;
import ru.coxey.diplom.model.Employee;


public interface EmployeeService {

    Employee getEmployeeById(int id);

    Employee getEmployeeByLogin(String login);

    @Transactional
    void updateEmployee(Employee admin, int id);

    @Transactional
    void deleteEmployee(int id);
}
