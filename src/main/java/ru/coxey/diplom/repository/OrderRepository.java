package ru.coxey.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.model.enums.Status;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByStatus(Status status);

    List<Order> findOrdersByEmployee_Id(int id);

    List<Order> findOrdersByEmployee_id_AndStatus(int id, Status status);

    List<Order> findOrdersByCustomer_Id(int id);
}
