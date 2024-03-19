package ru.coxey.diplom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adminpanel")
public class AdminController {

    @GetMapping()
    public String getAdminPanel() {
        return "adminpanel/adminpanel";
    }
}
