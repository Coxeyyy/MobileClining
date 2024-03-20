package ru.coxey.diplom.controller;

import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.model.enums.Status;
import ru.coxey.diplom.service.*;
import ru.coxey.diplom.util.ItemValidator;
import ru.coxey.diplom.util.PersonValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RegistrationService registrationService;
    @MockBean
    private PersonValidator personValidator;
    @MockBean
    private AdminService adminService;
    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private SpecialistService specialistService;
    @MockBean
    private CustomerService customerService;
    @MockBean
    private OrderService orderService;
    @MockBean
    private ItemService itemService;
    @MockBean
    private ItemValidator itemValidator;
    @MockBean
    private BindingResult bindingResult;
    private final int ID = 1;

    @Test
    void getAdminPanel() throws Exception {
        mockMvc.perform(get("/adminpanel"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminpanel/adminpanel"));
    }

    @Test
    void testGetMappingRegisterNewEmployee() throws Exception {
        mockMvc.perform(get("/adminpanel/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminpanel/register"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attributeExists("roles"));
    }

    @Test
    void testPostMappingRegisterNewEmployee_EmployeeNotRegister() throws Exception {
        doNothing().when(personValidator).validate(any(Employee.class), any(Errors.class));
        mockMvc.perform(post("/adminpanel/register"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllAdmins() throws Exception {
        when(adminService.getAllAdmins()).thenReturn(getListAdmins());
        mockMvc.perform(get("/adminpanel/admins"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("admins"))
                .andExpect(view().name("adminpanel/admin/admins"));
    }

    @Test
    void getAdminById() throws Exception {
        when(employeeService.getEmployeeById(anyInt())).thenReturn(getAdmin());
        mockMvc.perform(get("/adminpanel/admins/" + ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("admin"))
                .andExpect(view().name("adminpanel/admin/showAdminByIndex"));
    }

    @Test
    void editAdmin() throws Exception {
        when(employeeService.getEmployeeById(anyInt())).thenReturn(getAdmin());
        mockMvc.perform(get("/adminpanel/admins/" + ID + "/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("admin"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(view().name("adminpanel/admin/editAdmin"));
    }

    @Test
    void updateAdmin_NotUpdateAndRedirect() throws Exception {
        mockMvc.perform(patch("/adminpanel/admins/" + ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("roles"))
                .andExpect(view().name("adminpanel/admin/editAdmin"));
    }

    @Test
    void deleteAdmin() throws Exception {
        doNothing().when(employeeService).deleteEmployee(anyInt());
        mockMvc.perform(delete("/adminpanel/admins/" + ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/adminpanel"));
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

    @Test
    void getAllItems() throws Exception {
        when(itemService.getAllItems()).thenReturn(getListItems());
        mockMvc.perform(get("/adminpanel/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminpanel/item/items"))
                .andExpect(model().attributeExists("items"));
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItemById(anyInt())).thenReturn(getItem());
        mockMvc.perform(get("/adminpanel/items/" + ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("item"))
                .andExpect(view().name("adminpanel/item/showItemByIndex"));

    }

    @Test
    void editItem() throws Exception {
        when(itemService.getItemById(anyInt())).thenReturn(getItem());
        mockMvc.perform(get("/adminpanel/items/" + ID + "/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("item"))
                .andExpect(view().name("adminpanel/item/editItem"));
    }

    @Test
    void updateItem_notUpdateAndRedirect() throws Exception {
        mockMvc.perform(patch("/adminpanel/items/" + ID))
                .andExpect(status().isOk())
                .andExpect(view().name("adminpanel/item/editItem"));
    }

    @Test
    void deleteItem() throws Exception {
        doNothing().when(itemService).deleteItem(anyInt());
        mockMvc.perform(delete("/adminpanel/items/" + ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/adminpanel"));
    }

    @Test
    void testGetMappingCreateNewItem() throws Exception {
        mockMvc.perform(get("/adminpanel/items/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("item"))
                .andExpect(view().name("adminpanel/item/newItem"));
    }

    @Test
    void testPostMappingCreateNewItem_ItemNotCreated() throws Exception {
        doNothing().when(itemValidator).validate(any(Item.class), any(Errors.class));
        mockMvc.perform(post("/adminpanel/items/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminpanel/item/newItem"));
    }

    private Employee getAdmin() {
        return new Employee("Artem", "777", Role.ADMIN);
    }

    private Employee getSpecialist() {
        return new Employee("Artem", "777", Role.SPECIALIST);
    }

    private Customer getCustomer() {
        return new Customer("Artem", "777", Role.CUSTOMER, "89995556611",
                "Ryazan", true);
    }

    private List<Employee> getListAdmins() {
        Employee emp1 = new Employee("Artem", "777", Role.ADMIN);
        Employee emp2 = new Employee("Vitalik", "111", Role.ADMIN);
        return List.of(emp1, emp2);
    }

    private List<Employee> getListSpecialists() {
        Employee emp1 = new Employee("Artem", "777", Role.SPECIALIST);
        Employee emp2 = new Employee("Vitalik", "111", Role.SPECIALIST);
        return List.of(emp1, emp2);
    }

    private List<Customer> getListCustomers() {
        Customer customer1 = new Customer("Artem", "777", Role.CUSTOMER, "89995556611",
                "Ryazan", true);
        Customer customer2 = new Customer("Vitalik", "111", Role.CUSTOMER, "89995556611",
                "Ryazan", true);
        return List.of(customer1, customer2);
    }

    private Item getItem() {
        return new Item("Sofa", 2537.1);
    }

    private List<Item> getListItems() {
        Item item1 = new Item("Sofa", 2537.1);
        Item item2 = new Item("Carpet", 3671.8);
        return List.of(item1, item2);
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

    private List<Order> getListActiveOrders() {
        Customer customer = new Customer("Artem", "777", Role.CUSTOMER, "89995556611",
                "Ryazan", true);
        Employee specialist = new Employee("Artem", "777", Role.SPECIALIST);
        Order order1 = new Order(customer, specialist, getListItems(), Status.READY, 511.1);
        Order order2 = new Order(customer, specialist, getListItems(), Status.READY, 200.9);
        return List.of(order1, order2);
    }

}