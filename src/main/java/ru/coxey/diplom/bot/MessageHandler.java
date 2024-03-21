package ru.coxey.diplom.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.coxey.diplom.bot.command.*;
import ru.coxey.diplom.service.CustomerBotService;

@Component
public class MessageHandler {

    private Command command;

    private final CustomerBotService customerBotService;

    public MessageHandler(CustomerBotService customerBotService) {
        this.customerBotService = customerBotService;
    }

    public void getMessage(long chatId, Update update) {
        if (update.hasMessage() && update.getMessage().getText().startsWith("/")) {
            executeCommand(chatId, update);
        } else if (update.hasCallbackQuery()) {
            executeCallbackCommand(chatId, update);
        } else {
            executeStateCommand(chatId, update);
        }
    }

    private void executeCommand(long chatId, Update update) {
        switch (update.getMessage().getText()) {
            case "/start":
                command = new RegistrationCustomerCommand();
                command.execute(chatId, update);
                break;
            default:
                TgUtil.executeSendMessage(chatId, "Неизвестная команда");
                break;
        }
    }

    private void executeCallbackCommand(long chatId, Update update) {
        String message = update.getCallbackQuery().getData();
        if (message.startsWith("/add_to_order")) {
            command = new AddItemToOrderCommand(customerBotService);
            command.execute(chatId, update);
            return;
        }
        if (message.startsWith("/close_order")) {
            command = new CloseOrderCommand(customerBotService);
            command.execute(chatId, update);
            return;
        }
        switch (message) {
            case "/create_order":
                command = new CreateOrderCommand(customerBotService);
                command.execute(chatId, update);
                break;
            case "/show_orders":
                TgUtil.executeSendMessage(chatId, "Вот твои заказы");
                command = new ShowOrdersCommand(customerBotService);
                command.execute(chatId, update);
                break;
            default:
                TgUtil.executeSendMessage(chatId, "Неизвестная команда");
                break;
        }
    }

    private void executeStateCommand(long chatId, Update update) {
        String state = CustomerBot.mapState.get(chatId);
        switch (state) {
            case "WAIT_PHONE_NUMBER":
                command = new EnterPhoneNumberCommand(customerBotService);
                command.execute(chatId, update);
                break;
            case "WAIT_HOME_ADDRESS":
                command = new EnterHomeAddressCommand(customerBotService);
                command.execute(chatId, update);
                break;
            case "SET_TO_ORDER":
                command = new SetAmountItemToOrderCommand(customerBotService);
                command.execute(chatId, update);
                break;
            default:
                TgUtil.executeSendMessage(chatId, "Неизвестная команда");
                break;
        }
    }
}
