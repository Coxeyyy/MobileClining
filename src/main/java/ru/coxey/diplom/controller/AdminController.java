package ru.coxey.diplom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.service.AdminService;
import ru.coxey.diplom.service.EmployeeService;
import ru.coxey.diplom.service.RegistrationService;
import ru.coxey.diplom.service.SpecialistService;
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
    private final SpecialistService specialistService;

    public AdminController(RegistrationService registrationService, PersonValidator personValidator, AdminService adminService, EmployeeService employeeService, SpecialistService specialistService) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
        this.adminService = adminService;
        this.employeeService = employeeService;
        this.specialistService = specialistService;
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

    // Получение всех админов
    @GetMapping("/admins")
    public String getAllAdmins(Model model) {
        model.addAttribute("admins", adminService.getAllAdmins());
        return "adminpanel/admin/admins";
    }

    // Получение админов по ID
    @GetMapping("/admins/{id}")
    public String getAdminById(Model model, @PathVariable("id") int id) {
        model.addAttribute("admin", employeeService.getEmployeeById(id));
        return "adminpanel/admin/showAdminByIndex";
    }

    // Изменение админов
    @GetMapping("/admins/{id}/edit")
    public String editAdmin(Model model, @PathVariable("id") int id) {
        model.addAttribute("admin", employeeService.getEmployeeById(id));
        List<Role> role = new ArrayList<>();
        role.add(Role.getByCode("Администратор"));
        role.add(Role.getByCode("Специалист"));
        model.addAttribute("roles", role);
        return "adminpanel/admin/editAdmin";
    }

    // Обновление инфы по админам
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

    // Удаление админа
    @DeleteMapping("/admins/{id}")
    public String deleteAdmin(@PathVariable("id") int id) {
        employeeService.deleteEmployee(id);
        return "redirect:/adminpanel";
    }

    // Получение всех специалистов
    @GetMapping("/specialists")
    public String getAllSpecialist(Model model) {
        model.addAttribute("specialists", specialistService.getAllSpecialists());
        return "adminpanel/specialist/specialists";
    }

    // Специалист по ID
    @GetMapping("/specialists/{id}")
    public String getSpecialistById(Model model, @PathVariable("id") int id) {
        model.addAttribute("specialist", employeeService.getEmployeeById(id));
        return "adminpanel/specialist/showSpecialistByIndex";
    }

    // Изменение специалистов
    @GetMapping("/specialists/{id}/edit")
    public String editSpecialist(Model model, @PathVariable("id") int id) {
        model.addAttribute("specialist", employeeService.getEmployeeById(id));
        List<Role> role = new ArrayList<>();
        role.add(Role.getByCode("Администратор"));
        role.add(Role.getByCode("Специалист"));
        model.addAttribute("roles", role);
        return "adminpanel/specialist/editSpecialist";
    }

    // Обновление инфы по спецам
    @PatchMapping("/specialists/{id}")
    public String updateSpecialist(@ModelAttribute("specialist") @Valid Employee specialist,
                                   BindingResult bindingResult, Model model, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            List<Role> role = new ArrayList<>();
            role.add(Role.getByCode("Администратор"));
            role.add(Role.getByCode("Специалист"));
            model.addAttribute("roles", role);
            return "adminpanel/specialist/editSpecialist";
        }
        employeeService.updateEmployee(specialist, id);
        return "redirect:/adminpanel/specialists";
    }

    // Удалить специалиста
    @DeleteMapping("/specialists/{id}")
    public String deleteSpecialist(@PathVariable("id") int id) {
        employeeService.deleteEmployee(id);
        return "redirect:/adminpanel";
    }
}
