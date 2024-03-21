package ru.coxey.diplom.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.coxey.diplom.bot.CustomerBot;
import ru.coxey.diplom.bot.TgUtil;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.service.CustomerBotService;

import java.util.Optional;

@Component
public class AddItemToOrderCommand implements Command {

    private final CustomerBotService customerBotService;

    public AddItemToOrderCommand(CustomerBotService customerBotService) {
        this.customerBotService = customerBotService;
    }

    @Override
    public void execute(long chatId, Update update) {
        String itemName1 = update.getCallbackQuery().getData().substring(14);
        Optional<Item> itemByName = customerBotService.getItemByName(itemName1);
        if (itemByName.isPresent()) {
            Item presentItem = itemByName.get();
            CustomerBot.mapItemName.put(chatId, presentItem);
        }
        TgUtil.executeSendMessage(chatId, "Введите количество, которые вы хотели бы почистить: ");
        CustomerBot.mapState.put(chatId, "SET_TO_ORDER");
    }
}
