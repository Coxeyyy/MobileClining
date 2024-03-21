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

    @Override
    public Customer registerCustomer(String login, String address, String phoneNumber, Long telegramUserId) {
        Customer customer = new Customer(login, "defaultPass", Role.CUSTOMER,  phoneNumber, address, telegramUserId);
        return customerRepository.save(customer);
    }

    @Override
    public List<Item> getAllItemsForCustomer() {
        return itemRepository.findAll();
    }

    @Override
    public Optional<Item> getItemByName(String name) {
        return itemRepository.findItemByName(name);
    }

    @Override
    public Optional<Customer> getCustomerByTelegramUseId(Long telegramUserId) {
        return customerRepository.findCustomerByTelegramUserId(telegramUserId);
    }

    @Override
    public void saveOrder(Order order) {
        double sumOrder = 0;
        for (int i = 0; i < order.getItems().size(); i++) {
            sumOrder += order.getItems().get(i).getPrice();
        }
        order.setOrderPrice(sumOrder);
        orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrdersByCustomer(Long telegramUserId) {
        Optional<Customer> customerByTelegramUserId = customerRepository.findCustomerByTelegramUserId(telegramUserId);
        if (customerByTelegramUserId.isPresent()) {
            return orderRepository.findOrdersByCustomer_Id(customerByTelegramUserId.get().getId());
        }
        return List.of();
    }

    @Override
    public boolean isRegisteredCustomer(Long telegramUserId) {
        Optional<Customer> customerByTelegramUserId = customerRepository.findCustomerByTelegramUserId(telegramUserId);
        return customerByTelegramUserId.isPresent();
    }


}
