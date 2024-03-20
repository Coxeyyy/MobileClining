package ru.coxey.diplom.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.service.ItemService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class ItemValidatorTest {

    @Mock
    private ItemService itemService;

    private ItemValidator itemValidator;

    @BeforeEach
    void setUp() {
        itemValidator = new ItemValidator(itemService);
    }

    @Test
    void supports() {
        assertTrue(itemValidator.supports(Item.class));
        assertFalse(itemValidator.supports(Object.class));
    }

    @Test
    void itemIsFound() {
        Item item = new Item("Chair", 500.0);
        Mockito.when(itemService.findItemByName(anyString())).thenReturn(item);
        BindException errors = new BindException(item, "item");
        ValidationUtils.invokeValidator(itemValidator, item, errors);
        assertTrue(errors.hasErrors());
    }

    @Test
    void itemNotFound() {
        Item item = new Item("Chair", 500.0);
        Mockito.when(itemService.findItemByName(anyString())).thenReturn(null);
        BindException errors = new BindException(item, "item");
        ValidationUtils.invokeValidator(itemValidator, item, errors);
        assertFalse(errors.hasErrors());
    }
}