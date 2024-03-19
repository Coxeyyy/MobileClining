package ru.coxey.diplom.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.service.ItemService;

@Component
public class ItemValidator implements Validator {

    private final ItemService itemService;

    public ItemValidator(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;
        Item itemByName = itemService.findItemByName(item.getName());
        if (itemByName != null) {
            errors.rejectValue("name", "", "Услуга с таким именем уже существует");
        }
    }
}
