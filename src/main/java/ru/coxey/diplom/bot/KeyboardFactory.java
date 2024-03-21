package ru.coxey.diplom.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.coxey.diplom.model.Item;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

    public static InlineKeyboardMarkup getCustomerActionsKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
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
        inlineKeyboardMarkup.setKeyboard(keyboardCustomerActions);

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getItemsForOrderKeyboard(List<Item> allItems) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardCustomerActions = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons = new ArrayList<>();
        List<InlineKeyboardButton> rowExitButton = new ArrayList<>();

        for (int i = 0; i < allItems.size(); i ++) {
            InlineKeyboardButton buttonItem = new InlineKeyboardButton();
            buttonItem.setText(allItems.get(i).getName());
            buttonItem.setCallbackData("/add_to_order_" + allItems.get(i).getName());
            rowButtons.add(buttonItem);
        }

        keyboardCustomerActions.add(rowButtons);

        InlineKeyboardButton buttonExit = new InlineKeyboardButton();
        buttonExit.setText("Завершить формирование заказа");
        buttonExit.setCallbackData("/close_order");
        rowExitButton.add(buttonExit);
        keyboardCustomerActions.add(rowExitButton);

        inlineKeyboardMarkup.setKeyboard(keyboardCustomerActions);
        return inlineKeyboardMarkup;
    }
}
