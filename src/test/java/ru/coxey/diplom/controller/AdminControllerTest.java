package ru.coxey.diplom.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Errors;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.service.*;
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
    private PersonValidator personValidator;

    @MockBean
    private AdminService adminService;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private RegistrationService registrationService;

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

    private Employee getAdmin() {
        return new Employee("Artem", "777", Role.ADMIN);
    }

    private List<Employee> getListAdmins() {
        Employee emp1 = new Employee("Artem", "777", Role.ADMIN);
        Employee emp2 = new Employee("Vitalik", "111", Role.ADMIN);
        return List.of(emp1, emp2);
    }

    private List<Item> getListItems() {
        Item item1 = new Item("Sofa", 2537.1);
        Item item2 = new Item("Carpet", 3671.8);
        return List.of(item1, item2);
    }

}