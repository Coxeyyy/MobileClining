package ru.coxey.diplom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.service.OrderService;
import ru.coxey.diplom.service.SpecialistService;

@Controller
@RequestMapping("/adminpanel")
public class OrderController {

    private final OrderService orderService;

    private final SpecialistService specialistService;

    public OrderController(OrderService orderService, SpecialistService specialistService) {
        this.orderService = orderService;
        this.specialistService = specialistService;
    }

    @GetMapping("/orders")
    public String getAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "adminpanel/order/orders";
    }

    @GetMapping("/orders/{id}")
    public String getOrderById(Model model, @PathVariable("id") int id) {
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("specialists", specialistService.getAllSpecialists());
        return "adminpanel/order/showOrderByIndex";
    }

    @PatchMapping("/orders/{id}")
    public String setSpecialistToOrder(@PathVariable("id") int id, @RequestParam("selectedOption") String specialistId) {
        specialistService.setSpecialistToOrder(id, specialistId);
        return "redirect:/adminpanel";
    }

    @GetMapping("/activeOrders")
    public String checkActiveOrders(Model model) {
        model.addAttribute("activeOrders", orderService.getActiveOrders());
        model.addAttribute("specialists", specialistService.getAllSpecialists());
        model.addAttribute("order", new Order());
        return "adminpanel/order/checkActiveOrders";
    }

}
