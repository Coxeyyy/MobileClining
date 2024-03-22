package ru.coxey.diplom.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.coxey.diplom.bot.CustomerBot;
import ru.coxey.diplom.bot.KeyboardFactory;
import ru.coxey.diplom.bot.TgUtil;
import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.model.enums.Status;
import ru.coxey.diplom.service.CustomerBotService;

import java.util.Optional;

@Component
public class CloseOrderCommand implements Command {

    private final CustomerBotService customerBotService;

    public CloseOrderCommand(CustomerBotService customerBotService) {
        this.customerBotService = customerBotService;
    }

    @Override
    public void execute(long chatId, Update update) {
        TgUtil.promptWithKeyboard(chatId, "Выберите действие", KeyboardFactory.getCustomerActionsKeyboard());
        Long telegramUserId = update.getCallbackQuery().getMessage().getChatId();
        Order order = new Order();
        order.setItems(CustomerBot.mapListItem.get(chatId));
        order.setStatus(Status.IN_PROCESS);
        Optional<Customer> byId = customerBotService.getCustomerByTelegramUserId(telegramUserId);
        order.setCustomer(byId.get());
        customerBotService.saveOrder(order);
        CustomerBot.mapListItem.remove(chatId);
        CustomerBot.mapState.put(chatId, "NONE");
    }
}
