package project.gizka.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import project.gizka.command.AbstractCommand;
import project.gizka.command.CommandMap;
import project.gizka.config.TelegramBotConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final CommandMap commands;
    private final TelegramBotConfig botConfig;
    private final Map<String, AbstractCommand> userCommands;
    private final MessageBuffer messageBuffer;

    @Autowired
    public TelegramBot(TelegramBotConfig botConfig,
                       CommandMap commands) {
        this.botConfig = botConfig;
        this.commands = commands;
        this.messageBuffer = new MessageBuffer();
        this.userCommands = new ConcurrentHashMap<>();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message originalMessage = update.getMessage();
            String chatId = originalMessage.getChatId().toString();

            if (originalMessage.hasText()) {
                String commandKey = originalMessage.getText();
                AbstractCommand currentCommand;
                if (userCommands.containsKey(chatId)) { //есть ли текущие запросы от данного пользователя
                    currentCommand = userCommands.get(chatId); //достаем из общего хранилища команду этого пользователя
                    if (currentCommand != null && !currentCommand.isDone()) {//если команда не исполнена полностью
                        try {
                            messageBuffer.addMessage(currentCommand.handle(update));//продожаем с текущего места
                        } catch (Exception e) {
                            e.printStackTrace();
                            String responseBody = e.getMessage();
                            messageBuffer.addMessage(new SendMessage(chatId, responseBody));
                        }
                        sendResponseMessage();//отправляем сообщение
                        if (currentCommand.isDone()) {//если команда завершена, то удаляем команду
                            userCommands.remove(chatId);
                        }
                    }
                } else { //если запросов от данного пользователя нет
                    if (commands.getCommands().containsKey(commandKey)) { //проверяем является ли текущее сообщение командой
                        currentCommand = commands.getCommands().get(commandKey);//создаем новую команду
                        userCommands.put(chatId, currentCommand);
                        if (currentCommand != null && !currentCommand.isDone()) {
                            try {
                                messageBuffer.addMessage(currentCommand.handle(update));
                            } catch (Exception e) {
                                e.printStackTrace();
                                String responseBody = e.getMessage();
                                messageBuffer.addMessage(new SendMessage(originalMessage.getChatId().toString(), responseBody));
                            }
                            sendResponseMessage();
                            if (currentCommand.isDone()) { //если команда завершена, то удаляем команду
                                userCommands.remove(chatId);
                            }
                        }
                    }
                }
            }
        }
    }

    private void sendResponseMessage() {
        while (messageBuffer.hasMessages()) {
            SendMessage message = messageBuffer.getMessage();
            try {
                execute(message);
            } catch (TelegramApiException e) {
                //TODO
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }
}
