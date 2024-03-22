package ru.coxey.diplom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.model.enums.Status;
import ru.coxey.diplom.service.EmployeeService;
import ru.coxey.diplom.service.SpecialistService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SpecialistController {

    private final SpecialistService specialistService;

    private final EmployeeService employeeService;

    public SpecialistController(SpecialistService specialistService, EmployeeService employeeService) {
        this.specialistService = specialistService;
        this.employeeService = employeeService;
    }

    @GetMapping("/specialistpanel")
    public String showSpecialistPanel() {
        return "specialistpanel/specialistpanel";
    }

    @GetMapping("/specialistpanel/orders")
    public String showOrders(Model model) {
        model.addAttribute("orders", specialistService.getOrders());
        return "specialistpanel/orders";
    }

    @GetMapping("/specialistpanel/orders/{id}")
    public String showOrdersById(Model model, @PathVariable("id") int id) {
        model.addAttribute("order", specialistService.getOrderById(id));
        model.addAttribute("statuses", Status.values());
        return "specialistpanel/showOrderByIndex";
    }

    @PatchMapping("/specialistpanel/orders/{id}")
    public String setStatusToOrder(@PathVariable("id") int id, @RequestParam("selectedOption") String statusOrder) {
        specialistService.setStatusToOrder(id, statusOrder);
        return "redirect:/specialistpanel";
    }

    @GetMapping("/specialistpanel/activeOrders")
    public String showActiveOrders(Model model) {
        model.addAttribute("orders", specialistService.getActiveOrders());
        return "specialistpanel/activeOrders";
    }

    @GetMapping("/adminpanel/specialists")
    public String getAllSpecialist(Model model) {
        model.addAttribute("specialists", specialistService.getAllSpecialists());
        return "adminpanel/specialist/specialists";
    }

    @GetMapping("/adminpanel/specialists/{id}")
    public String getSpecialistById(Model model, @PathVariable("id") int id) {
        model.addAttribute("specialist", employeeService.getEmployeeById(id));
        return "adminpanel/specialist/showSpecialistByIndex";
    }

    @GetMapping("/adminpanel/specialists/{id}/edit")
    public String editSpecialist(Model model, @PathVariable("id") int id) {
        model.addAttribute("specialist", employeeService.getEmployeeById(id));
        List<Role> role = new ArrayList<>();
        role.add(Role.getByCode("Администратор"));
        role.add(Role.getByCode("Специалист"));
        model.addAttribute("roles", role);
        return "adminpanel/specialist/editSpecialist";
    }

    @PatchMapping("/adminpanel/specialists/{id}")
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

    @DeleteMapping("/adminpanel/specialists/{id}")
    public String deleteSpecialist(@PathVariable("id") int id) {
        employeeService.deleteEmployee(id);
        return "redirect:/adminpanel";
    }

    @GetMapping("/adminpanel/specialists/{id}/orders")
    public String getOrdersBySpecialist(Model model, @PathVariable("id") int id) {
        model.addAttribute("specialist", employeeService.getEmployeeById(id));
        model.addAttribute("ordersBySpecialist", specialistService.getOrdersBySpecialist(id));
        return "adminpanel/specialist/getOrdersBySpecialist";
    }
}
