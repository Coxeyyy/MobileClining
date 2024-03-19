package ru.coxey.diplom.service;

import org.springframework.transaction.annotation.Transactional;
import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.model.Employee;

public interface RegistrationService {

    @Transactional
    public void registerEmployee(Employee employee);
}
