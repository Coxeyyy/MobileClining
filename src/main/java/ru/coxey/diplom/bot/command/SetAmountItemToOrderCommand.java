package ru.coxey.diplom.bot.command;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.coxey.diplom.bot.CustomerBot;
import ru.coxey.diplom.bot.KeyboardFactory;
import ru.coxey.diplom.bot.TgUtil;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.service.CustomerBotService;

import java.util.ArrayList;
import java.util.List;

@Component
public class SetAmountItemToOrderCommand implements Command {

    private final CustomerBotService customerBotService;

    public SetAmountItemToOrderCommand(CustomerBotService customerBotService) {
        this.customerBotService = customerBotService;
    }

    @Override
    public void execute(long chatId, Update update) {
        String data = update.getMessage().getText();
        if (!StringUtils.isNumeric(data)) {
            TgUtil.executeSendMessage(chatId, "Введите число!");
            return;
        }
        int amountItems = Integer.parseInt(data);
        for(int i = 0; i < amountItems; i++) {
            List<Item> items = CustomerBot.mapListItem.get(chatId);
            Item item = CustomerBot.mapItemName.get(chatId);
            if (items == null) {
                List<Item> items1 = new ArrayList<>();
                items1.add(item);
                CustomerBot.mapListItem.put(chatId, items1);
            } else {
                items.add(item);
            }
        }
        List<Item> allItems = customerBotService.getAllItemsForCustomer();
        InlineKeyboardMarkup itemsForOrderKeyboard = KeyboardFactory.getItemsForOrderKeyboard(allItems);
        TgUtil.promptWithKeyboard(chatId, "Выберите услугу", itemsForOrderKeyboard);
        CustomerBot.mapState.put(chatId, "NONE");
    }
}
