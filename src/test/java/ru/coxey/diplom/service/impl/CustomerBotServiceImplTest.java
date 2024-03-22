package ru.coxey.diplom.service.impl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.model.enums.Status;
import ru.coxey.diplom.repository.CustomerRepository;
import ru.coxey.diplom.repository.ItemRepository;
import ru.coxey.diplom.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerBotServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CustomerBotServiceImpl customerBotService;

    @Test
    @Disabled
    void registerEmployee() {
        Customer customer = new Customer("Artem", "defaultPass", Role.CUSTOMER,
                "88001112233", "Ryazan", 653789L);
        when(customerRepository.save(customer)).thenReturn(customer);
        Customer customerFromService = customerBotService.registerCustomer("Artem", "Ryazan",
                "88001112233",  653789L);
        assertAll(
                () -> assertEquals(customer.getLogin(), customerFromService.getLogin()),
                () -> assertEquals(customer.getPhoneNumber(), customerFromService.getPhoneNumber()),
                () -> assertEquals(customer.getPassword(), customerFromService.getPassword()),
                () -> assertEquals(customer.getAddress(), customerFromService.getAddress()),
                () -> assertEquals(customer.getRole(), customerFromService.getRole()),
                () -> assertEquals(customer.getTelegramUserId(), customerFromService.getTelegramUserId())
        );
    }

    @Test
    void getAllItems() {
        List<Item> itemList = List.of(new Item("Chair", 500.0), new Item("Sofa", 198.0));
        when(itemRepository.findAll()).thenReturn(itemList);
        List<Item> itemListFromService = customerBotService.getAllItemsForCustomer();
        assertEquals(itemList.size(), itemListFromService.size());
    }

    @Test
    void getItemByName() {
        Item item = new Item("Chair", 500.0);
        when(itemRepository.findItemByName(anyString())).thenReturn(Optional.of(item));
        Optional<Item> itemFromService = customerBotService.getItemByName("test");
        if (itemFromService.isPresent()) {
            assertEquals(item.getName(), itemFromService.get().getName());
            assertEquals(item.getPrice(), itemFromService.get().getPrice());
        }
    }

    @Test
    void getCustomerByTelegramUseId() {
        Customer customer = new Customer("Artem", "defaultPass", Role.CUSTOMER,
                "88001112233", "Ryazan", 653789L);
        when(customerRepository.findCustomerByTelegramUserId(anyLong())).thenReturn(Optional.of(customer));
        Optional<Customer> customerFromService = customerBotService.getCustomerByTelegramUseId(424242L);
        if (customerFromService.isPresent()) {
            assertEquals(customer.getLogin(), customerFromService.get().getLogin());
            assertEquals(customer.getRole(), customerFromService.get().getRole());
        }
    }

    @Test
    void saveOrder() {
        List<Item> listItem = List.of(new Item("Chair", 500.0), new Item("Sofa", 1000.0));
        Customer customer = new Customer("Artem", "defaultPass", Role.CUSTOMER,
                "88001112233", "Ryazan", 653789L);
        Employee specialist = new Employee("Vitalik", "111", Role.SPECIALIST);
        Order order = new Order(customer, specialist, listItem, Status.IN_PROCESS, 1500.0);
        when(orderRepository.save(order)).thenReturn(order);
        customerBotService.saveOrder(order);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void getAllOrdersByCustomer_CustomerFound() {
        Customer customer = new Customer("Artem", "defaultPass", Role.CUSTOMER,
                "88001112233", "Ryazan", 653789L);
        when(customerRepository.findCustomerByTelegramUserId(anyLong())).thenReturn(Optional.of(customer));
        when(orderRepository.findOrdersByCustomer_Id(anyInt())).thenReturn(List.of(new Order(), new Order()));
        List<Order> listOrdersFromService = customerBotService.getAllOrdersByCustomer(414141L);
        assertEquals(2, listOrdersFromService.size());
    }

    @Test
    void getAllOrdersByCustomer_CustomerNotFound() {
        when(customerRepository.findCustomerByTelegramUserId(anyLong())).thenReturn(Optional.empty());
        List<Order> listOrdersFromService = customerBotService.getAllOrdersByCustomer(414141L);
        assertEquals(0, listOrdersFromService.size());
    }

    @Test
    void isRegisteredCustomer_NotFound() {
        when(customerRepository.findCustomerByTelegramUserId(anyLong())).thenReturn(Optional.empty());
        assertFalse(customerBotService.isRegisteredCustomer(52525L));
    }

}