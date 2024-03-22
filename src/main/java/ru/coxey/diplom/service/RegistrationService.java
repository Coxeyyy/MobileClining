package ru.coxey.diplom.service;

import org.springframework.transaction.annotation.Transactional;
import ru.coxey.diplom.model.Employee;

public interface RegistrationService {

    @Transactional
    void registerEmployee(Employee employee);
}
