package ru.coxey.diplom.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.coxey.diplom.bot.CustomerBot;
import ru.coxey.diplom.bot.TgUtil;

@Component
public class RegistrationCustomerCommand implements Command {
    @Override
    public void execute(long chatId, Update update) {
        String message = "Добро пожаловать в бота выездного клининга \r\n" +
                "Введите ваш номер телефона в формате 80001112233:";
        CustomerBot.mapState.put(chatId, "WAIT_PHONE_NUMBER");
        TgUtil.executeSendMessage(chatId, message);
    }
}
