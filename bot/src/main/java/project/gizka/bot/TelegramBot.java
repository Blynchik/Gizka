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

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final CommandMap commands;
    private final TelegramBotConfig botConfig;
    private AbstractCommand currentCommand;
    private final MessageBuffer messageBuffer;

    @Autowired
    public TelegramBot(TelegramBotConfig botConfig,
                       CommandMap commands) {
        this.botConfig = botConfig;
        this.commands = commands;
        this.messageBuffer = new MessageBuffer();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message originalMessage = update.getMessage();

            if (originalMessage.hasText()) {
                String commandKey = originalMessage.getText();
                Map<String, AbstractCommand> commandMap = commands.getCommands();
                currentCommand = commandMap.getOrDefault(commandKey, currentCommand);

                if (currentCommand != null && !currentCommand.checkReadyForProcess()) {
                    try {
                        messageBuffer.addMessage(currentCommand.handle(update));
                    } catch (Exception e) {
                        e.printStackTrace();
                        String responseBody = e.getMessage();
                        messageBuffer.addMessage(new SendMessage(originalMessage.getChatId().toString(), responseBody));
                    }

                    sendResponseMessage();
                }
            }
        }
    }

    private ReplyKeyboard getInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        // Создание кнопки
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        var button1 = new InlineKeyboardButton();
        button1.setCallbackData("button1");
        button1.setText("Кнопка 1");

        var button2 = new InlineKeyboardButton();
        button2.setCallbackData("button2");
        button2.setText("Кнопка 2");

        rowInline.add(button1);
        rowInline.add(button2);
        rowsInline.add(rowInline);

        inlineKeyboardMarkup.setKeyboard(rowsInline);

        return inlineKeyboardMarkup;
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
