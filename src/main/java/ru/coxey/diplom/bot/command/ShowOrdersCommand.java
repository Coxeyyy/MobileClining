package ru.coxey.diplom.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.coxey.diplom.bot.KeyboardFactory;
import ru.coxey.diplom.bot.TgUtil;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.service.CustomerBotService;

import java.util.List;

@Component
public class ShowOrdersCommand implements Command {

    private final CustomerBotService customerBotService;

    public ShowOrdersCommand(CustomerBotService customerBotService) {
        this.customerBotService = customerBotService;
    }

    @Override
    public void execute(long chatId, Update update) {
        Long telegramUserId = update.getCallbackQuery().getMessage().getChatId();
        List<Order> allOrders = customerBotService.getAllOrdersByCustomer(telegramUserId);
        for (Order order : allOrders) {
            String messageOrder = "Цена заказа: " + order.getOrderPrice().toString() + "\r\n" +
                    "Состав заказа: " + order.getItems().toString() + "\r\n" +
                    "Статус заказа: " + order.getStatus().getCode();
            TgUtil.executeSendMessage(chatId, messageOrder);
        }
        TgUtil.promptWithKeyboard(chatId, "Выберите действие", KeyboardFactory.getCustomerActionsKeyboard());
    }
}
