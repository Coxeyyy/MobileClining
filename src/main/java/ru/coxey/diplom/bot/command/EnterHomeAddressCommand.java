package ru.coxey.diplom.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.coxey.diplom.bot.CustomerBot;
import ru.coxey.diplom.bot.KeyboardFactory;
import ru.coxey.diplom.bot.TgUtil;
import ru.coxey.diplom.service.CustomerBotService;

@Component
public class EnterHomeAddressCommand implements Command {

    private final CustomerBotService customerBotService;

    public EnterHomeAddressCommand(CustomerBotService customerBotService) {
        this.customerBotService = customerBotService;
    }

    @Override
    public void execute(long chatId, Update update) {
        String address = update.getMessage().getText();
        String phoneNumber = CustomerBot.mapPhoneNumbers.get(chatId);
        String login = update.getMessage().getChat().getFirstName();
        Long telegramUserId = update.getMessage().getChat().getId();
        customerBotService.registerCustomer(login, address, phoneNumber, telegramUserId);
        String message = "Вы успешно зарегистировались в системе!";
        CustomerBot.mapState.put(chatId, "WAIT_COMMAND");
        TgUtil.executeSendMessage(chatId, message);
        TgUtil.promptWithKeyboard(chatId, "Выберите действие", KeyboardFactory.getCustomerActionsKeyboard());
    }
}
