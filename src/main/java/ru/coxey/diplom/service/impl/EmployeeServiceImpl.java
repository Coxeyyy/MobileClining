package ru.coxey.diplom.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.repository.EmployeeRepository;
import ru.coxey.diplom.service.EmployeeService;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final PasswordEncoder passwordEncoder;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** Метод возвращает сотрудника по его ID */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Employee getEmployeeById(int id) {
        Optional<Employee> employeeById = employeeRepository.findById(id);
        if (employeeById.isPresent()) {
            return employeeById.get();
        } else {
            throw new IllegalArgumentException("Такого пользователя не существует");
        }
    }

    /** Метод возвращает сотрудника по его логину */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Employee getEmployeeByLogin(String login) {
        Optional<Employee> employeeByLogin = employeeRepository.findEmployeeByLogin(login);
        if (employeeByLogin.isPresent()) {
            return employeeByLogin.get();
        } else {
            throw new IllegalArgumentException("Такого пользователя не существует");
        }
    }

    /** Метод обновляет информацию сотрудника */
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateEmployee(Employee admin, int id) {
        Optional<Employee> employeeById = employeeRepository.findById(id);
        if (employeeById.isPresent()) {
            Employee employeeFromDB = employeeById.get();
            employeeFromDB.setId(admin.getId());
            employeeFromDB.setLogin(admin.getLogin());
            employeeFromDB.setPassword(passwordEncoder.encode(admin.getPassword()));
            employeeFromDB.setRole(admin.getRole());
            employeeRepository.save(employeeFromDB);
        } else {
            throw new IllegalArgumentException("Такого пользователя не существует");
        }
    }

    /** Метод удаляет сотрудника из БД */
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteEmployee(int id) {
        employeeRepository.deleteById(id);
    }
}
