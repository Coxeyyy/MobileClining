package ru.coxey.diplom.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.coxey.diplom.model.Item;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomerBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    public static final Map<Long, String> mapState = new ConcurrentHashMap<>();

    public static final Map<Long, String> mapPhoneNumbers = new ConcurrentHashMap<>();

    public static final Map<Long, Item> mapItemName = new ConcurrentHashMap<>();

    public static final Map<Long, List<Item>> mapListItem = new ConcurrentHashMap<>();

    private final MessageHandler messageHandler;


    protected CustomerBot(@Value("${bot.token}") String botToken, MessageHandler messageHandler) {
        super(botToken);
        this.messageHandler = messageHandler;
        TgUtil.setSender(this);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            messageHandler.getMessage(update.getMessage().getChatId(), update);
        } else if (update.hasCallbackQuery()) {
            messageHandler.getMessage(update.getCallbackQuery().getFrom().getId(), update);
        } else {
            messageHandler.getMessage(update.getMessage().getChatId(), update);
        }
    }

}
