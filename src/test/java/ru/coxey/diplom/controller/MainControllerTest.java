package ru.coxey.diplom.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.service.MainService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MainController.class)
@AutoConfigureMockMvc(addFilters = false)
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MainService mainService;

    @Test
    void getEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setRole(Role.ADMIN);
        Mockito.when(mainService.getEmployee()).thenReturn(employee);
        mockMvc.perform(get("/main"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("employee"))
                .andExpect(view().name("main"));
    }
}