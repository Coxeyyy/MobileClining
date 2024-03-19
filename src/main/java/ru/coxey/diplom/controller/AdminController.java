package ru.coxey.diplom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.service.RegistrationService;
import ru.coxey.diplom.util.PersonValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/adminpanel")
public class AdminController {

    private final RegistrationService registrationService;
    private final PersonValidator personValidator;

    public AdminController(RegistrationService registrationService, PersonValidator personValidator) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
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
}
