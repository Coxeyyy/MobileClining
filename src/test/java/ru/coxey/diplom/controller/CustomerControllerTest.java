package ru.coxey.diplom.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.model.enums.Status;
import ru.coxey.diplom.service.CustomerService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private final int ID = 1;

    @Test
    void getAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(getListCustomers());
        mockMvc.perform(get("/adminpanel/customers"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("customers"))
                .andExpect(view().name("adminpanel/customer/customers"));
    }

    @Test
    void getCustomerById() throws Exception {
        when(customerService.getCustomerById(anyInt())).thenReturn(getCustomer());
        mockMvc.perform(get("/adminpanel/customers/" + ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("customer"))
                .andExpect(view().name("adminpanel/customer/showCustomerByIndex"));
    }

    @Test
    void deleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(anyInt());
        mockMvc.perform(delete("/adminpanel/customers/" + ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/adminpanel"));
    }

    @Test
    void getOrdersByCustomer() throws Exception {
        when(customerService.getCustomerById(anyInt())).thenReturn(getCustomer());
        when(customerService.getOrdersByCustomer(anyInt())).thenReturn(getListOrders());
        mockMvc.perform(get("/adminpanel/customers/" + ID + "/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminpanel/customer/getOrdersByCustomer"))
                .andExpect(model().attributeExists("customer"))
                .andExpect(model().attributeExists("ordersByCustomer"));
    }

    private Customer getCustomer() {
        return new Customer("Artem", "777", Role.CUSTOMER, "89995556611",
                "Ryazan", true);
    }

    private List<Customer> getListCustomers() {
        Customer customer1 = new Customer("Artem", "777", Role.CUSTOMER, "89995556611",
                "Ryazan", true);
        Customer customer2 = new Customer("Vitalik", "111", Role.CUSTOMER, "89995556611",
                "Ryazan", true);
        return List.of(customer1, customer2);
    }

    private List<Order> getListOrders() {
        Customer customer = new Customer("Artem", "777", Role.CUSTOMER, "89995556611",
                "Ryazan", true);
        Employee specialist = new Employee("Artem", "777", Role.SPECIALIST);
        Order order1 = new Order(customer, specialist, getListItems(), Status.IN_PROCESS, 511.1);
        Order order2 = new Order(customer, specialist, getListItems(), Status.IN_PROCESS, 200.9);
        return List.of(order1, order2);
    }

    private List<Order> getListActiveOrders() {
        Customer customer = new Customer("Artem", "777", Role.CUSTOMER, "89995556611",
                "Ryazan", true);
        Employee specialist = new Employee("Artem", "777", Role.SPECIALIST);
        Order order1 = new Order(customer, specialist, getListItems(), Status.READY, 511.1);
        Order order2 = new Order(customer, specialist, getListItems(), Status.READY, 200.9);
        return List.of(order1, order2);
    }

    private List<Item> getListItems() {
        Item item1 = new Item("Sofa", 2537.1);
        Item item2 = new Item("Carpet", 3671.8);
        return List.of(item1, item2);
    }

}