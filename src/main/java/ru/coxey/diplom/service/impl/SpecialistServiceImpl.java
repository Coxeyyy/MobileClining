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
    private final OrderRepository orderRepository;

    public SpecialistServiceImpl(EmployeeRepository employeeRepository, OrderRepository orderRepository) {
        this.employeeRepository = employeeRepository;
        this.orderRepository = orderRepository;
    }

    /** Метод возвращает список сотрудников с ролью специалист */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Employee> getAllSpecialists() {
        return employeeRepository.findEmployeesByRole(Role.SPECIALIST);
    }

    /** Метод устанавливает конкретного специалиста на конкретный заказ */
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void setSpecialistToOrder(int id, String specialistId) {
        Optional<Order> orderById = orderRepository.findById(id);
        Optional<Employee> specialistById = employeeRepository.findById(Integer.parseInt(specialistId));
        if (orderById.isPresent() && specialistById.isPresent()) {
            Order order = orderById.get();
            Employee specialist = specialistById.get();
            order.setEmployee(specialist);
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Произошла ошибка");
        }
    }

    /** Метод возвращает список заказ по ID специалиста */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Order> getOrdersBySpecialist(int id) {
        Optional<Employee> employeeById = employeeRepository.findById(id);
        List<Order> byEmployee = null;
        if (employeeById.isPresent()) {
            byEmployee = orderRepository.findOrdersByEmployee_Id(employeeById.get().getId());
        }
        return byEmployee;
    }

    /** Метод возвращает список заказов конкретного специалиста */
    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    public List<Order> getOrders() {
        if (getPerson().isPresent()) {
            return orderRepository.findOrdersByEmployee_Id(getPerson().get().getId());
        }
        return List.of();
    }

    /** Метод возвращает список активных заказов конкретного специалиста */
    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    public List<Order> getActiveOrders() {
        if (getPerson().isPresent()) {
            return orderRepository.findOrdersByEmployee_id_AndStatus(getPerson().get().getId(), Status.IN_PROCESS);
        }
        return List.of();
    }

    /** Метод возвращает заказ по его ID */
    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    public Order getOrderById(int id) {
        Optional<Order> orderById = orderRepository.findById(id);
        if (orderById.isPresent()) {
            return orderById.get();
        } else {
            throw new IllegalArgumentException("Такого заказа не существует");
        }
    }

    /** Метод устанавливает статус IN_PROCESS или READY на конкретный заказ */
    @Transactional
    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    public void setStatusToOrder(int id, String statusOrder) {
        Optional<Order> orderById = orderRepository.findById(id);
        if (orderById.isPresent()) {
            Order order = orderById.get();
            order.setStatus(Status.valueOf(statusOrder));
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Такого заказа не существует");
        }
    }

    /** Метод ищет в SecurityContextHolder логин специалиста */
    protected Optional<Person> getPerson() {
        String loginSpecialist = SecurityContextHolder.getContext().getAuthentication().getName();
        return employeeRepository.findByLogin(loginSpecialist);
    }

}
