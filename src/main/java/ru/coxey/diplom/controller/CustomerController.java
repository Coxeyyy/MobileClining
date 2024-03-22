package ru.coxey.diplom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.coxey.diplom.service.CustomerService;

@Controller
@RequestMapping("/adminpanel")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public String getAllCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "adminpanel/customer/customers";
    }

    //  Заказчик по ID
    @GetMapping("/customers/{id}")
    public String getCustomerById(Model model, @PathVariable("id") int id) {
        model.addAttribute("customer", customerService.getCustomerById(id));
        return "adminpanel/customer/showCustomerByIndex";
    }

    // Удалить заказчика
    @DeleteMapping("/customers/{id}")
    public String deleteCustomer(@PathVariable("id") int id) {
        customerService.deleteCustomer(id);
        return "redirect:/adminpanel";
    }

    // Получить список заказов конкретного заказчика
    @GetMapping("/customers/{id}/orders")
    public String getOrdersByCustomer(Model model, @PathVariable("id") int id) {
        model.addAttribute("customer", customerService.getCustomerById(id));
        model.addAttribute("ordersByCustomer", customerService.getOrdersByCustomer(id));
        return "adminpanel/customer/getOrdersByCustomer";
    }

}
