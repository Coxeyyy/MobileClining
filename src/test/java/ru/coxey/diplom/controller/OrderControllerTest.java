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
import ru.coxey.diplom.service.OrderService;
import ru.coxey.diplom.service.SpecialistService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private SpecialistService specialistService;

    private final int ID = 1;

    @Test
    void getAllOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(getListOrders());
        mockMvc.perform(get("/adminpanel/orders"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orders"))
                .andExpect(view().name("adminpanel/order/orders"));
    }

    @Test
    void getOrderById() throws Exception {
        when(orderService.getOrderById(anyInt())).thenReturn(getOrder());
        when(specialistService.getAllSpecialists()).thenReturn(getListSpecialists());
        mockMvc.perform(get("/adminpanel/orders/" + ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("specialists"))
                .andExpect(view().name("adminpanel/order/showOrderByIndex"));
    }

    @Test
    void setSpecialistToOrder() throws Exception {
        doNothing().when(specialistService).setStatusToOrder(anyInt(), anyString());
        mockMvc.perform(patch("/adminpanel/orders/" + ID)
                        .param("selectedOption", "anyString()"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/adminpanel"));
    }

    @Test
    void checkActiveOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(getListOrders());
        when(specialistService.getAllSpecialists()).thenReturn(getListSpecialists());
        mockMvc.perform(get("/adminpanel/activeOrders"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminpanel/order/checkActiveOrders"))
                .andExpect(model().attributeExists("activeOrders"))
                .andExpect(model().attributeExists("specialists"));
    }

    private Order getOrder() {
        Customer customer = new Customer("Artem", "777", Role.CUSTOMER, "89995556611",
                "Ryazan", true);
        Employee specialist = new Employee("Artem", "777", Role.SPECIALIST);
        return new Order(customer, specialist, getListItems(), Status.IN_PROCESS, 511.1);
    }

    private List<Order> getListOrders() {
        Customer customer = new Customer("Artem", "777", Role.CUSTOMER, "89995556611",
                "Ryazan", true);
        Employee specialist = new Employee("Artem", "777", Role.SPECIALIST);
        Order order1 = new Order(customer, specialist, getListItems(), Status.IN_PROCESS, 511.1);
        Order order2 = new Order(customer, specialist, getListItems(), Status.IN_PROCESS, 200.9);
        return List.of(order1, order2);
    }

    private List<Employee> getListSpecialists() {
        Employee emp1 = new Employee("Artem", "777", Role.SPECIALIST);
        Employee emp2 = new Employee("Vitalik", "111", Role.SPECIALIST);
        return List.of(emp1, emp2);
    }

    private List<Item> getListItems() {
        Item item1 = new Item("Sofa", 2537.1);
        Item item2 = new Item("Carpet", 3671.8);
        return List.of(item1, item2);
    }
}