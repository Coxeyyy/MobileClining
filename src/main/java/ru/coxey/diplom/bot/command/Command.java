package ru.coxey.diplom.bot.command;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {

    void execute(long chatId, Update update);
}
