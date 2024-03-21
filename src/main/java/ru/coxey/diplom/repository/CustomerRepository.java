package ru.coxey.diplom.repository;

import ru.coxey.diplom.model.Customer;

import java.util.Optional;

public interface CustomerRepository extends PersonRepository<Customer> {

    Optional<Customer> findCustomerByTelegramUserId(Long telegramUserId);
}
