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

    /**
     * Метод достает из SecurityContextHolder логин сотрудника
     * и ищет его в БД
     * Метод нужен, чтобы при открытии главной страницы сайта
     * сотруднику в зависимости от его роли показывалась ссылка
     * либо на админ панель либо на панель специалиста
     */
    @Override
    public Employee getEmployee() {
        String loginEmployee = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Employee> byLogin = employeeRepository.findEmployeeByLogin(loginEmployee);
        if (byLogin.isPresent()) {
            return byLogin.get();
        } else {
            throw new IllegalArgumentException("Такого сотрудника не существует");
        }
    }
}
