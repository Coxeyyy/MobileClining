package ru.coxey.diplom.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import ru.coxey.diplom.service.EmployeeService;
import ru.coxey.diplom.service.SpecialistService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpecialistController.class)
@AutoConfigureMockMvc(addFilters = false)
class SpecialistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpecialistService specialistService;

    @MockBean
    private EmployeeService employeeService;

    private List<Order> listOrders;

    private final int ID = 1;

    @BeforeEach
    void setUp() {
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
        Mockito.when(specialistService.getOrders()).thenReturn(List.of());
        mockMvc.perform(get("/specialistpanel/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("specialistpanel/orders"));
        verify(specialistService, times(1)).getOrders();
    }

    @Test
    void showEmptyListOrders() throws Exception {
        Mockito.when(specialistService.getOrders()).thenReturn(listOrders);
        mockMvc.perform(get("/specialistpanel/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("specialistpanel/orders"))
                .andExpect(model().attributeExists("orders"));
        verify(specialistService, times(1)).getOrders();
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

    @Test
    void setStatusReadyToOrder() throws Exception {
        int id = 1;
        doNothing().when(specialistService).setStatusToOrder(anyInt(), anyString());
        mockMvc.perform(patch("/specialistpanel/orders/" + id)
                .param("selectedOption", "READY"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/specialistpanel"));
    }

    @Test
    void showActiveOrders() throws Exception {
        Mockito.when(specialistService.getActiveOrders()).thenReturn(listOrders);
        mockMvc.perform(get("/specialistpanel/activeOrders"))
                .andExpect(status().isOk())
                .andExpect(view().name("specialistpanel/activeOrders"))
                .andExpect(model().attributeExists("orders"));
        verify(specialistService, times(1)).getActiveOrders();
    }

    @Test
    void getAllSpecialist() throws Exception {
        when(specialistService.getAllSpecialists()).thenReturn(getListSpecialists());
        mockMvc.perform(get("/adminpanel/specialists"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("specialists"))
                .andExpect(view().name("adminpanel/specialist/specialists"));
    }


    @Test
    void getSpecialistById() throws Exception {
        when(employeeService.getEmployeeById(anyInt())).thenReturn(getSpecialist());
        mockMvc.perform(get("/adminpanel/specialists/" + ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("specialist"))
                .andExpect(view().name("adminpanel/specialist/showSpecialistByIndex"));
    }

    @Test
    void editSpecialist() throws Exception {
        when(employeeService.getEmployeeById(anyInt())).thenReturn(getSpecialist());
        mockMvc.perform(get("/adminpanel/specialists/" + ID + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminpanel/specialist/editSpecialist"));
    }

    @Test
    void updateSpecialist_notUpdateAndRedirect() throws Exception {
        mockMvc.perform(patch("/adminpanel/specialists/" + ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("roles"))
                .andExpect(view().name("adminpanel/specialist/editSpecialist"));
    }

    @Test
    void deleteSpecialist() throws Exception {
        doNothing().when(employeeService).deleteEmployee(anyInt());
        mockMvc.perform(delete("/adminpanel/specialists/" + ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/adminpanel"));
    }

    @Test
    void getOrdersBySpecialist() throws Exception {
        when(employeeService.getEmployeeById(anyInt())).thenReturn(getSpecialist());
        when(specialistService.getOrdersBySpecialist(anyInt())).thenReturn(getListOrders());
        mockMvc.perform(get("/adminpanel/specialists/" + ID + "/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminpanel/specialist/getOrdersBySpecialist"))
                .andExpect(model().attributeExists("specialist"))
                .andExpect(model().attributeExists("ordersBySpecialist"));
    }

    private Employee getSpecialist() {
        return new Employee("Artem", "777", Role.SPECIALIST);
    }

    private List<Employee> getListSpecialists() {
        Employee emp1 = new Employee("Artem", "777", Role.SPECIALIST);
        Employee emp2 = new Employee("Vitalik", "111", Role.SPECIALIST);
        return List.of(emp1, emp2);
    }

    private List<Order> getListOrders() {
        Customer customer = new Customer("Artem", "777", Role.CUSTOMER, "89995556611",
                "Ryazan", true);
        Employee specialist = new Employee("Artem", "777", Role.SPECIALIST);
        Order order1 = new Order(customer, specialist, getListItems(), Status.IN_PROCESS, 511.1);
        Order order2 = new Order(customer, specialist, getListItems(), Status.IN_PROCESS, 200.9);
        return List.of(order1, order2);
    }

    private List<Item> getListItems() {
        Item item1 = new Item("Sofa", 2537.1);
        Item item2 = new Item("Carpet", 3671.8);
        return List.of(item1, item2);
    }

}