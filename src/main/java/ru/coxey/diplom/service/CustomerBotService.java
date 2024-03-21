package ru.coxey.diplom.service;

import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.model.Order;

import java.util.List;
import java.util.Optional;

public interface CustomerBotService {

    Customer registerCustomer(String login, String address, String phoneNumber, Long telegramUserId);

    List<Item> getAllItemsForCustomer();

    Optional<Item> getItemByName(String name);

    Optional<Customer> getCustomerByTelegramUseId(Long telegramUserId);

    void saveOrder(Order order);

    List<Order> getAllOrdersByCustomer(Long telegramUserId);

    boolean isRegisteredCustomer(Long telegramUserId);
}
