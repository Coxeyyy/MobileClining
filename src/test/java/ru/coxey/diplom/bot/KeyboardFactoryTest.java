package ru.coxey.diplom.bot;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.coxey.diplom.model.Item;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KeyboardFactoryTest {

    @Mock
    private KeyboardFactory keyboardFactory;

    @Test
    public void testGetCustomerActionsKeyboard() {
        InlineKeyboardMarkup expectedKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardCustomerActions = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons = new ArrayList<>();

        InlineKeyboardButton buttonCreateOrder = new InlineKeyboardButton();
        buttonCreateOrder.setText("Создать заказ");
        buttonCreateOrder.setCallbackData("/create_order");

        InlineKeyboardButton buttonShowAllOrders = new InlineKeyboardButton();
        buttonShowAllOrders.setText("Показать все заказы");
        buttonShowAllOrders.setCallbackData("/show_orders");

        rowButtons.add(buttonCreateOrder);
        rowButtons.add(buttonShowAllOrders);

        keyboardCustomerActions.add(rowButtons);
        expectedKeyboard.setKeyboard(keyboardCustomerActions);

        InlineKeyboardMarkup actualKeyboard = keyboardFactory.getCustomerActionsKeyboard();

        assertEquals(expectedKeyboard, actualKeyboard);
    }

    @Test
    public void testGetItemsForOrderKeyboard() {
        List<Item> testItems = new ArrayList<>();
        testItems.add(new Item("Item1", 500.0));
        testItems.add(new Item("Item2", 1000.0));

        InlineKeyboardMarkup actualKeyboard = keyboardFactory.getItemsForOrderKeyboard(testItems);

        assertEquals(2, actualKeyboard.getKeyboard().size());
        assertEquals("Item1", actualKeyboard.getKeyboard().get(0).get(0).getText());
        assertEquals("/add_to_order_Item1", actualKeyboard.getKeyboard().get(0).get(0).getCallbackData());
        assertEquals("Завершить формирование заказа", actualKeyboard.getKeyboard().get(1).get(0).getText());
        assertEquals("/close_order", actualKeyboard.getKeyboard().get(1).get(0).getCallbackData());
    }

}