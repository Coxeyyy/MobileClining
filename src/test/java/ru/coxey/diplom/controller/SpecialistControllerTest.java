package ru.coxey.diplom.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.model.enums.Status;
import ru.coxey.diplom.service.SpecialistService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SpecialistControllerTest {

    @Mock
    private SpecialistService specialistService;

    @InjectMocks
    private SpecialistController specialistController;

    private MockMvc mockMvc;

    private List<Order> listOrders;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(specialistController).build();
        listOrders = new ArrayList<>();
        Customer customer = new Customer("Vitalik", "111", Role.CUSTOMER, "89995556621", "Ryazan", true);
        Employee employee = new Employee();
        employee.setLogin("Artem");
        employee.setPassword("777");
        employee.setRole(Role.SPECIALIST);
        Item itemChair = new Item("Chair", 500.0);
        Item itemSofa = new Item("Sofa", 1000.0);
        List<Item> itemList = List.of(itemChair, itemSofa);
        listOrders.add(new Order(customer, employee, itemList, Status.IN_PROCESS, 1500.0));
    }

    @Test
    void showSpecialistPanel() throws Exception {
        mockMvc.perform(get("/specialistpanel"))
                .andExpect(status().isOk())
                .andExpect(view().name("specialistpanel/specialistpanel"));
    }

    @Test
    void showNullOrders() throws Exception {
        Mockito.when(specialistService.getOrders()).thenReturn(null);
        mockMvc.perform(get("/specialistpanel/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("specialistpanel/orders"));
        verify(specialistService, times(1)).getOrders();
    }

    @Test
    void showNonNullOrders() throws Exception {
        Mockito.when(specialistService.getOrders()).thenReturn(listOrders);
        mockMvc.perform(get("/specialistpanel/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("specialistpanel/orders"))
                .andExpect(model().attributeExists("orders"));
        verify(specialistService, times(1)).getOrders();
    }

    @Test
    void showNullOrderById() throws Exception {
        Mockito.when(specialistService.getOrderById(anyInt())).thenReturn(null);
        mockMvc.perform(get("/specialistpanel/orders/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(view().name("specialistpanel/showOrderByIndex"))
                .andExpect(model().attributeExists("statuses"));
        verify(specialistService, times(1)).getOrderById(anyInt());
    }

    @Test
    void showNonNullOrderById() throws Exception {
        Mockito.when(specialistService.getOrderById(0)).thenReturn(listOrders.get(0));
        mockMvc.perform(get("/specialistpanel/orders/{id}", 0))
                .andExpect(status().isOk())
                .andExpect(view().name("specialistpanel/showOrderByIndex"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("statuses"));
        verify(specialistService, times(1)).getOrderById(0);
    }

//    @Test    // ne rabotaet
//    @Transactional
//    void setStatusReadyToOrder() throws Exception {
//        doNothing().when(specialistService).setStatusToOrder(anyInt(), anyString());
//        mockMvc.perform(patch("/specialistpanel/orders/{id}", anyInt(), anyString())
//                        .param("id", "selectedOption"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/specialistpanel"));
//    }

    @Test
    void showNullActiveOrders() throws Exception {
        Mockito.when(specialistService.getActiveOrders()).thenReturn(null);
        mockMvc.perform(get("/specialistpanel/activeOrders"))
                .andExpect(status().isOk())
                .andExpect(view().name("specialistpanel/activeOrders"));
        verify(specialistService, times(1)).getActiveOrders();
    }

    @Test
    void showNonNullActiveOrders() throws Exception {
        Mockito.when(specialistService.getActiveOrders()).thenReturn(listOrders);
        mockMvc.perform(get("/specialistpanel/activeOrders"))
                .andExpect(status().isOk())
                .andExpect(view().name("specialistpanel/activeOrders"))
                .andExpect(model().attributeExists("orders"));
        verify(specialistService, times(1)).getActiveOrders();
    }

}