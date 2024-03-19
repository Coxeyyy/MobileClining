package ru.coxey.diplom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.coxey.diplom.model.enums.Status;
import ru.coxey.diplom.service.SpecialistService;

@Controller
@RequestMapping("/specialistpanel")
public class SpecialistController {

    private final SpecialistService specialistService;

    public SpecialistController(SpecialistService specialistService) {
        this.specialistService = specialistService;
    }

    // Показать главную страницу
    @GetMapping()
    public String showSpecialistPanel() {
        return "specialistpanel/specialistpanel";
    }

    // Получить все заказы
    @GetMapping("/orders")
    public String showOrders(Model model) {
        model.addAttribute("orders", specialistService.getOrders());
        return "specialistpanel/orders";
    }

    // Получить детальную информацию по ID заказа
    @GetMapping("/orders/{id}")
    public String showOrdersById(Model model, @PathVariable("id") int id) {
        model.addAttribute("order", specialistService.getOrderById(id));
        model.addAttribute("statuses", Status.values());
        return "specialistpanel/showOrderByIndex";
    }

    // Поменять статус заказа
    @PatchMapping("/orders/{id}")
    public String setStatusToOrder(@PathVariable("id") int id, @RequestParam("selectedOption") String statusOrder) {
        specialistService.setStatusToOrder(id, statusOrder);
        return "redirect:/specialistpanel";
    }

    // Показать все активные заказы
    @GetMapping("/activeOrders")
    public String showActiveOrders(Model model) {
        model.addAttribute("orders", specialistService.getActiveOrders());
        return "specialistpanel/activeOrders";
    }
}
