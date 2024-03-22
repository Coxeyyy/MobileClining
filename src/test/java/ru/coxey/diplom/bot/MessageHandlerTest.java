package ru.coxey.diplom.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.service.CustomerBotService;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageHandlerTest {

    @Test
    void testExecuteCommandStart() {
        CustomerBotService customerBotService = mock(CustomerBotService.class);
        MessageHandler messageHandler = new MessageHandler(customerBotService);
        Update update = new Update();
        Message message = new Message();
        message.setText("/start");
        update.setMessage(message);
        long chatId = 12345;
        try (MockedStatic<TgUtil> telegramUtilMock = Mockito.mockStatic(TgUtil.class)) {
            messageHandler.getMessage(chatId, update);
        }
    }

    @Test
    void testExecuteCallbackCommand_add_to_order() {
        CustomerBotService customerBotService = mock(CustomerBotService.class);
        MessageHandler messageHandler = new MessageHandler(customerBotService);
        long chatId = 12345L;
        Update update = mock(Update.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        when(update.hasCallbackQuery()).thenReturn(true);
        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getData()).thenReturn("/add_to_order_Chair");

        try (MockedStatic<TgUtil> telegramUtilMock = Mockito.mockStatic(TgUtil.class)) {
            messageHandler.getMessage(chatId, update);
        }
    }
    @Test
    void testExecuteStateCommand_WAIT_PHONE_NUMBER() {
        CustomerBotService customerBotService = mock(CustomerBotService.class);
        MessageHandler messageHandler = new MessageHandler(customerBotService);
        long chatId = 12345L;
        Update update = mock(Update.class);
        when(update.hasCallbackQuery()).thenReturn(false);
        Message message = new Message();
        message.setText("88001112233");
        update.setMessage(message);
        when(update.getMessage()).thenReturn(message);
        CustomerBot.mapState.put(12345L, "WAIT_PHONE_NUMBER");

        try (MockedStatic<TgUtil> telegramUtilMock = Mockito.mockStatic(TgUtil.class)) {
            messageHandler.getMessage(chatId, update);
        }
    }

    @Test
    void testExecuteStateCommand_WAIT_HOME_ADDRESS() {
        CustomerBotService customerBotService = mock(CustomerBotService.class);
        MessageHandler messageHandler = new MessageHandler(customerBotService);
        long chatId = 12345L;
        Update update = mock(Update.class);
        when(update.hasCallbackQuery()).thenReturn(false);
        Message message = new Message();
        message.setText("88001112233");
        Chat chat = new Chat();
        chat.setFirstName("Artem");
        chat.setId(12345L);
        message.setChat(chat);
        update.setMessage(message);
        when(update.getMessage()).thenReturn(message);
        CustomerBot.mapState.put(12345L, "WAIT_HOME_ADDRESS");
        CustomerBot.mapPhoneNumbers.put(12345L, "88001112233");

        try (MockedStatic<TgUtil> telegramUtilMock = Mockito.mockStatic(TgUtil.class)) {
            messageHandler.getMessage(chatId, update);
        }
    }

    @Test
    void testExecuteStateCommand_SET_TO_ORDER() {
        CustomerBotService customerBotService = mock(CustomerBotService.class);
        MessageHandler messageHandler = new MessageHandler(customerBotService);
        long chatId = 12345L;
        Update update = mock(Update.class);
        when(update.hasCallbackQuery()).thenReturn(false);
        Message message = new Message();
        message.setText("5");
        Chat chat = new Chat();
        chat.setFirstName("Artem");
        chat.setId(12345L);
        message.setChat(chat);
        update.setMessage(message);
        when(update.getMessage()).thenReturn(message);
        CustomerBot.mapState.put(chatId, "SET_TO_ORDER");
        CustomerBot.mapItemName.put(chatId, new Item("Chair", 500.0));
        try (MockedStatic<TgUtil> telegramUtilMock = Mockito.mockStatic(TgUtil.class)) {
            messageHandler.getMessage(chatId, update);
        }
    }
}