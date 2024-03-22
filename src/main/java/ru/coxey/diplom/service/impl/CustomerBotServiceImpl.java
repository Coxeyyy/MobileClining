package ru.coxey.diplom.service.impl;

import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.repository.CustomerRepository;
import ru.coxey.diplom.repository.ItemRepository;
import ru.coxey.diplom.repository.OrderRepository;
import ru.coxey.diplom.service.CustomerBotService;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerBotServiceImpl implements CustomerBotService {

    private final CustomerRepository customerRepository;

    private final ItemRepository itemRepository;

    private final OrderRepository orderRepository;

    public CustomerBotServiceImpl(CustomerRepository customerRepository, ItemRepository itemRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    /** Метод регистрирует нового заказчика в таблицу users */
    @Override
    public Customer registerCustomer(String login, String address, String phoneNumber, Long telegramUserId) {
        Customer customer = new Customer(login, "defaultPass", Role.CUSTOMER,  phoneNumber, address, telegramUserId);
        return customerRepository.save(customer);
    }

    /** Метод возвращает список всех услуг */
    @Override
    public List<Item> getAllItemsForCustomer() {
        return itemRepository.findAll();
    }

    /** Метод ищет услугу по имени */
    @Override
    public Optional<Item> getItemByName(String name) {
        return itemRepository.findItemByName(name);
    }

    /** Метод ищет заказчика по его telegramUserId */
    @Override
    public Optional<Customer> getCustomerByTelegramUserId(Long telegramUserId) {
        return customerRepository.findCustomerByTelegramUserId(telegramUserId);
    }

    /** Метод сохраняет заказ в БД */
    @Override
    public void saveOrder(Order order) {
        double orderPrice = 0;
        for (int i = 0; i < order.getItems().size(); i++) {
            orderPrice += order.getItems().get(i).getPrice();
        }
        order.setOrderPrice(orderPrice);
        orderRepository.save(order);
    }

    /** Метод возвращает все заказы пользователя по его telegramUserId */
    @Override
    public List<Order> getAllOrdersByCustomer(Long telegramUserId) {
        Optional<Customer> customerByTelegramUserId = customerRepository.findCustomerByTelegramUserId(telegramUserId);
        if (customerByTelegramUserId.isPresent()) {
            return orderRepository.findOrdersByCustomer_Id(customerByTelegramUserId.get().getId());
        }
        return List.of();
    }

    /**
     * Метод проверяет зарегистрирован ли заказчик
     * Возвращает true, если такой заказчик есть, иначе false
     */
    @Override
    public boolean isRegisteredCustomer(Long telegramUserId) {
        Optional<Customer> customerByTelegramUserId = customerRepository.findCustomerByTelegramUserId(telegramUserId);
        return customerByTelegramUserId.isPresent();
    }


}
