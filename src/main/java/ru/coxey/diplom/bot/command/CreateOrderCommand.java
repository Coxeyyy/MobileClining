package ru.coxey.diplom.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.coxey.diplom.bot.KeyboardFactory;
import ru.coxey.diplom.bot.TgUtil;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.service.CustomerBotService;

import java.util.List;

@Component
public class CreateOrderCommand implements Command {

    private final CustomerBotService customerBotService;

    public CreateOrderCommand(CustomerBotService customerBotService) {
        this.customerBotService = customerBotService;
    }

    @Override
    public void execute(long chatId, Update update) {
        List<Item> allItems = customerBotService.getAllItemsForCustomer();
        InlineKeyboardMarkup itemsForOrderKeyboard = KeyboardFactory.getItemsForOrderKeyboard(allItems);
        TgUtil.promptWithKeyboard(chatId, "Выберите услугу", itemsForOrderKeyboard);
    }
}
