package ru.coxey.diplom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.service.*;
import ru.coxey.diplom.util.PersonValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/adminpanel")
public class AdminController {

    private final RegistrationService registrationService;

    private final PersonValidator personValidator;

    private final AdminService adminService;

    private final EmployeeService employeeService;

    public AdminController(RegistrationService registrationService, PersonValidator personValidator,
                           AdminService adminService, EmployeeService employeeService) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
        this.adminService = adminService;
        this.employeeService = employeeService;
    }

    @GetMapping()
    public String getAdminPanel() {
        return "adminpanel/adminpanel";
    }

    @GetMapping("/register")
    public String registerNewEmployee(Model model) {
        List<Role> role = new ArrayList<>();
        role.add(Role.getByCode("Администратор"));
        role.add(Role.getByCode("Специалист"));
        model.addAttribute("employee", new Employee());
        model.addAttribute("roles", role);
        return "adminpanel/register";
    }

    @PostMapping("/register")
    public String registerNewEmployee(@ModelAttribute("employee") @Valid Employee employee,
                                      BindingResult bindingResult, Model model) {
        personValidator.validate(employee, bindingResult);
        if (bindingResult.hasErrors()) {
            List<Role> role = new ArrayList<>();
            role.add(Role.getByCode("Администратор"));
            role.add(Role.getByCode("Специалист"));
            model.addAttribute("roles", role);
            return "adminpanel/register";
        }
        registrationService.registerEmployee(employee);
        return "redirect:/adminpanel";
    }

    @GetMapping("/admins")
    public String getAllAdmins(Model model) {
        model.addAttribute("admins", adminService.getAllAdmins());
        return "adminpanel/admin/admins";
    }

    @GetMapping("/admins/{id}")
    public String getAdminById(Model model, @PathVariable("id") int id) {
        model.addAttribute("admin", employeeService.getEmployeeById(id));
        return "adminpanel/admin/showAdminByIndex";
    }

    @GetMapping("/admins/{id}/edit")
    public String editAdmin(Model model, @PathVariable("id") int id) {
        model.addAttribute("admin", employeeService.getEmployeeById(id));
        List<Role> role = new ArrayList<>();
        role.add(Role.getByCode("Администратор"));
        role.add(Role.getByCode("Специалист"));
        model.addAttribute("roles", role);
        return "adminpanel/admin/editAdmin";
    }

    @PatchMapping("/admins/{id}")
    public String updateAdmin(@ModelAttribute("admin") @Valid Employee admin, BindingResult bindingResult,
                              @PathVariable("id") int id, Model model) {
        if (bindingResult.hasErrors()) {
            List<Role> role = new ArrayList<>();
            role.add(Role.getByCode("Администратор"));
            role.add(Role.getByCode("Специалист"));
            model.addAttribute("roles", role);
            return "adminpanel/admin/editAdmin";
        }
        employeeService.updateEmployee(admin, id);
        return "redirect:/adminpanel/admins";
    }

    @DeleteMapping("/admins/{id}")
    public String deleteAdmin(@PathVariable("id") int id) {
        employeeService.deleteEmployee(id);
        return "redirect:/adminpanel";
    }

}
