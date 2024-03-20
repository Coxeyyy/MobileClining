package ru.coxey.diplom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.service.*;
import ru.coxey.diplom.util.ItemValidator;
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
    private final CustomerService customerService;
    private final OrderService orderService;
    private final ItemService itemService;
    private final ItemValidator itemValidator;

    public AdminController(RegistrationService registrationService, PersonValidator personValidator,
                           AdminService adminService, EmployeeService employeeService,
                           SpecialistService specialistService, CustomerService customerService,
                           OrderService orderService, ItemService itemService, ItemValidator itemValidator) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
        this.adminService = adminService;
        this.employeeService = employeeService;
        this.specialistService = specialistService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.itemService = itemService;
        this.itemValidator = itemValidator;
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

    @GetMapping("/specialists/{id}/orders")
    public String getOrdersBySpecialist(Model model, @PathVariable("id") int id) {
        model.addAttribute("specialist", employeeService.getEmployeeById(id));
        model.addAttribute("ordersBySpecialist", specialistService.getOrdersBySpecialist(id));
        return "adminpanel/specialist/getOrdersBySpecialist";
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

    // Все заказы
    @GetMapping("/orders")
    public String getAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "adminpanel/order/orders";
    }

    // Заказ по ID
    @GetMapping("/orders/{id}")
    public String getOrderById(Model model, @PathVariable("id") int id) {
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("specialists", specialistService.getAllSpecialists());
        return "adminpanel/order/showOrderByIndex";
    }

    // Назначить специалиста на заказ
    @PatchMapping("/orders/{id}")
    public String setSpecialistToOrder(@PathVariable("id") int id, @RequestParam("selectedOption") String specialistId) {
        specialistService.setSpecialistToOrder(id, specialistId);
        return "redirect:/adminpanel";
    }

    // Получить активные заказы
    @GetMapping("/activeOrders")
    public String checkActiveOrders(Model model) {
        model.addAttribute("activeOrders", orderService.getActiveOrders());
        model.addAttribute("specialists", specialistService.getAllSpecialists());
        model.addAttribute("order", new Order());
        return "adminpanel/order/checkActiveOrders";
    }

    // Все услуги
    @GetMapping("/items")
    public String getAllItems(Model model) {
        model.addAttribute("items", itemService.getAllItems());
        return "adminpanel/item/items";
    }

    // Услуги по ID
    @GetMapping("/items/{id}")
    public String getItemById(Model model, @PathVariable("id") int id) {
        model.addAttribute("item", itemService.getItemById(id));
        return "adminpanel/item/showItemByIndex";
    }

    // Изменение услуги
    @GetMapping("/items/{id}/edit")
    public String editItem(Model model, @PathVariable("id") int id) {
        model.addAttribute("item", itemService.getItemById(id));
        return "adminpanel/item/editItem";
    }

    // Обновление инфы по услуге
    @PatchMapping("/items/{id}")
    public String updateItem(@ModelAttribute("item") @Valid Item item, BindingResult bindingResult,
                             @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "adminpanel/item/editItem";
        }
        itemService.updateItem(item, id);
        return "redirect:/adminpanel/items";
    }

    // Удалить услугу
    @DeleteMapping("/items/{id}")
    public String deleteItem(@PathVariable("id") int id) {
        itemService.deleteItem(id);
        return "redirect:/adminpanel";
    }

    // Создать новую услугу
    @GetMapping("/items/new")
    public String createNewItem(Model model) {
        model.addAttribute("item", new Item());
        return "adminpanel/item/newItem";
    }

    // Создать новую услугу
    @PostMapping("/items/new")
    public String createNewItem(@ModelAttribute("item") @Valid Item item,
                                BindingResult bindingResult) {
        itemValidator.validate(item, bindingResult);
        if (bindingResult.hasErrors()) {
            return "adminpanel/item/newItem";
        }
        itemService.createNewItem(item);
        return "redirect:/adminpanel/items";
    }
}
