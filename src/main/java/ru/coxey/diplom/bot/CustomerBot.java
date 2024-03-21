package ru.coxey.diplom.bot;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.model.enums.Status;
import ru.coxey.diplom.service.CustomerBotService;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CustomerBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    private final Map<Long, String> mapState = new HashMap<>();

    private final Map<Long, String> mapPhoneNumbers = new HashMap<>();

    private final Map<Long, Item> mapItemName = new HashMap<>();

    private final Map<Long, List<Item>> mapListItem = new HashMap<>();

    private final CustomerBotService customerBotService;

    protected CustomerBot(@Value("${bot.token}") String botToken, CustomerBotService customerBotService) {
        super(botToken);
        this.customerBotService = customerBotService;
        TgUtil.setSender(this);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().getText().startsWith("/")) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (message) {
                case "/start":
                    registrationCustomer(chatId, update);
                    break;
                default:
                    TgUtil.executeSendMessage(chatId, "Неизвестная команда");
                    break;
            }
        } else if (update.hasCallbackQuery()) {
            String message = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            if (message.startsWith("/add_to_order")) {
                addItemToOrder(chatId, update, message);
                return;
            }
            if (message.startsWith("/close_order")) {
                closeOrder(chatId, update);
                return;
            }
            switch (message) {
                case "/create_order":
                    createOrder(chatId, update);
                    break;
                case "/show_orders":
                    TgUtil.executeSendMessage(chatId, "Вот твои заказы");
                    showOrders(chatId, update);
                    break;
                default:
                    TgUtil.executeSendMessage(chatId, "Неизвестная команда");
                    break;
            }
        } else {
            long chatId = update.getMessage().getChatId(); // Tyt null pri zapuske mojet kinyt`
            String state = mapState.get(chatId);
            switch (state) {
                case "WAIT_PHONE_NUMBER":
                    enterPhoneNumber(chatId, update);
                    break;
                case "WAIT_HOME_ADDRESS":
                    enterHomeAddress(chatId, update);
                    break;
                case "SET_TO_ORDER":
                    setAmountItemToOrder(chatId, update);
                    break;
                default:
                    TgUtil.executeSendMessage(chatId, "Неизвестная команда");
                    break;
            }
        }
    }

    private void registrationCustomer(long chatId,Update update) {
        String message = "Добро пожаловать в бота выездного клининга \r\n" +
                "Введите ваш номер телефона в формате 80001112233:";
        mapState.put(chatId, "WAIT_PHONE_NUMBER");
        TgUtil.executeSendMessage(chatId, message);
    }

    private void enterPhoneNumber(long chatId, Update update) {
        if (customerBotService.isRegisteredCustomer(chatId)) {
            TgUtil.executeSendMessage(chatId, "Вы уже зарегистрированы");
            TgUtil.promptWithKeyboard(chatId, "Выберите действие", KeyboardFactory.getCustomerActionsKeyboard());
            return;
        }
        String phoneNumber = update.getMessage().getText();
        Pattern patternPhoneNumber = Pattern.compile("^\\d{11}$");
        Matcher matcherPhoneNumber = patternPhoneNumber.matcher(phoneNumber);
        if (!matcherPhoneNumber.matches()) {
            mapState.put(chatId, "WAIT_PHONE_NUMBER");
            String message = "Неверно введен номер, попробуйте заново";
            TgUtil.executeSendMessage(chatId, message);
            return;
        }
        mapPhoneNumbers.put(chatId, phoneNumber);
        String message = "Введите ваш адрес";
        mapState.put(chatId, "WAIT_HOME_ADDRESS");
        TgUtil.executeSendMessage(chatId, message);
    }

    private void enterHomeAddress(long chatId, Update update) {
        String address = update.getMessage().getText();
        String phoneNumber = mapPhoneNumbers.get(chatId);
        String login = update.getMessage().getChat().getFirstName();
        Long telegramUserId = update.getMessage().getChat().getId();
        customerBotService.registerCustomer(login, address, phoneNumber, telegramUserId);
        String message = "Вы успешно зарегистировались в системе!";
        mapState.put(chatId, "WAIT_COMMAND");
        TgUtil.executeSendMessage(chatId, message);
        TgUtil.promptWithKeyboard(chatId, "Выберите действие", KeyboardFactory.getCustomerActionsKeyboard());
    }

    private void createOrder(long chatId, Update update) {
        List<Item> allItems = customerBotService.getAllItemsForCustomer();
        InlineKeyboardMarkup itemsForOrderKeyboard = KeyboardFactory.getItemsForOrderKeyboard(allItems);
        TgUtil.promptWithKeyboard(chatId, "Выберите услугу", itemsForOrderKeyboard);
    }

    private void addItemToOrder(long chatId, Update update, String message) {
        String itemName = message.substring(14);
        Optional<Item> itemByName = customerBotService.getItemByName(itemName);
        if (itemByName.isPresent()) {
            Item presentItem = itemByName.get();
            mapItemName.put(chatId, presentItem);
        }
        TgUtil.executeSendMessage(chatId, "Введите количество, которые вы хотели бы почистить: ");
        mapState.put(chatId, "SET_TO_ORDER");
    }

    private void setAmountItemToOrder(long chatId, Update update) {
        String data = update.getMessage().getText();
        if (!StringUtils.isNumeric(data)) {
            TgUtil.executeSendMessage(chatId, "Введите число!");
            return;
        }
        int amountItems = Integer.parseInt(data);
        for(int i = 0; i < amountItems; i++) {
            List<Item> items = mapListItem.get(chatId);
            Item item = mapItemName.get(chatId);
            if (items == null) {
                List<Item> items1 = new ArrayList<>();
                items1.add(item);
                mapListItem.put(chatId, items1);
            } else {
                items.add(item);
            }
        }
        List<Item> allItems = customerBotService.getAllItemsForCustomer();
        InlineKeyboardMarkup itemsForOrderKeyboard = KeyboardFactory.getItemsForOrderKeyboard(allItems);
        TgUtil.promptWithKeyboard(chatId, "Выберите услугу", itemsForOrderKeyboard);
        mapState.put(chatId, "SET_TO_ORDER");
    }

    private void closeOrder(long chatId, Update update) {
        TgUtil.promptWithKeyboard(chatId, "Выберите действие", KeyboardFactory.getCustomerActionsKeyboard());
        Long telegramUserId = update.getCallbackQuery().getMessage().getChatId();
        Order order = new Order();
        order.setItems(mapListItem.get(chatId));
        order.setStatus(Status.IN_PROCESS);
        Optional<Customer> byId = customerBotService.getCustomerByTelegramUseId(telegramUserId);
        order.setCustomer(byId.get());
        customerBotService.saveOrder(order);
        mapState.put(chatId, "NONE");
    }

    private void showOrders(long chatId, Update update) {
        Long telegramUserId = update.getCallbackQuery().getMessage().getChatId();
        List<Order> allOrders = customerBotService.getAllOrdersByCustomer(telegramUserId);
        for (int i = 0; i < allOrders.size(); i++) {
            Order order = allOrders.get(i);
            String messageOrder = "Цена заказа: " + order.getOrderPrice().toString() + "\r\n" +
                    "Состав заказа: " + order.getItems().toString() + "\r\n" +
                    "Статус заказа: " + order.getStatus().getCode();
            TgUtil.executeSendMessage(chatId, messageOrder);
        }
        TgUtil.promptWithKeyboard(chatId, "Выберите действие", KeyboardFactory.getCustomerActionsKeyboard());
    }

}
