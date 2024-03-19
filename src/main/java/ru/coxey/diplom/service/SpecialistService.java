package ru.coxey.diplom.service;

import org.springframework.transaction.annotation.Transactional;
import ru.coxey.diplom.model.Employee;

import java.util.List;


public interface SpecialistService {

    List<Employee> getAllSpecialists();

    @Transactional
    void setSpecialistToOrder(int id, String specialistId);
}
