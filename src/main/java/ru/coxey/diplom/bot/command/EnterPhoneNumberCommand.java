package ru.coxey.diplom.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.coxey.diplom.bot.CustomerBot;
import ru.coxey.diplom.bot.KeyboardFactory;
import ru.coxey.diplom.bot.TgUtil;
import ru.coxey.diplom.service.CustomerBotService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EnterPhoneNumberCommand implements Command {

    private final CustomerBotService customerBotService;

    public EnterPhoneNumberCommand(CustomerBotService customerBotService) {
        this.customerBotService = customerBotService;
    }

    @Override
    public void execute(long chatId, Update update) {
        if (customerBotService.isRegisteredCustomer(chatId)) {
            TgUtil.executeSendMessage(chatId, "Вы уже зарегистрированы");
            TgUtil.promptWithKeyboard(chatId, "Выберите действие", KeyboardFactory.getCustomerActionsKeyboard());
            return;
        }
        String phoneNumber = update.getMessage().getText();
        Pattern patternPhoneNumber = Pattern.compile("^\\d{11}$");
        Matcher matcherPhoneNumber = patternPhoneNumber.matcher(phoneNumber);
        if (!matcherPhoneNumber.matches()) {
            CustomerBot.mapState.put(chatId, "WAIT_PHONE_NUMBER");
            String message = "Неверно введен номер, попробуйте заново";
            TgUtil.executeSendMessage(chatId, message);
            return;
        }
        CustomerBot.mapPhoneNumbers.put(chatId, phoneNumber);
        String message = "Введите ваш адрес";
        CustomerBot.mapState.put(chatId, "WAIT_HOME_ADDRESS");
        TgUtil.executeSendMessage(chatId, message);
    }
}
