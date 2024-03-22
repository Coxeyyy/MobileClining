package ru.coxey.diplom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.service.ItemService;
import ru.coxey.diplom.util.ItemValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/adminpanel")
public class ItemController {

    private final ItemService itemService;

    private final ItemValidator itemValidator;

    public ItemController(ItemService itemService, ItemValidator itemValidator) {
        this.itemService = itemService;
        this.itemValidator = itemValidator;
    }

    @GetMapping("/items")
    public String getAllItems(Model model) {
        model.addAttribute("items", itemService.getAllItems());
        return "adminpanel/item/items";
    }

    @GetMapping("/items/{id}")
    public String getItemById(Model model, @PathVariable("id") int id) {
        model.addAttribute("item", itemService.getItemById(id));
        return "adminpanel/item/showItemByIndex";
    }

    @GetMapping("/items/{id}/edit")
    public String editItem(Model model, @PathVariable("id") int id) {
        model.addAttribute("item", itemService.getItemById(id));
        return "adminpanel/item/editItem";
    }

    @PatchMapping("/items/{id}")
    public String updateItem(@ModelAttribute("item") @Valid Item item, BindingResult bindingResult,
                             @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "adminpanel/item/editItem";
        }
        itemService.updateItem(item, id);
        return "redirect:/adminpanel/items";
    }

    @DeleteMapping("/items/{id}")
    public String deleteItem(@PathVariable("id") int id) {
        itemService.deleteItem(id);
        return "redirect:/adminpanel";
    }

    @GetMapping("/items/new")
    public String createNewItem(Model model) {
        model.addAttribute("item", new Item());
        return "adminpanel/item/newItem";
    }

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
