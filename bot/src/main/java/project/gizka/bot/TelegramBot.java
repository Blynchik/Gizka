package project.gizka.bot;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.command.AbstractCommand;
import project.gizka.command.CommandMap;
import project.gizka.config.TelegramBotConfig;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@Getter
public class TelegramBot extends TelegramLongPollingBot {
    private final CommandMap validCommands;
    private final TelegramBotConfig botConfig;
    private final Map<String, AbstractCommand> notCompletedCommands;
    private final Map<String, Message> deletePool;
    private final CommonResponsePool commonResponsePool;
    private final Queue<Update> updatePool;

    @Autowired
    public TelegramBot(TelegramBotConfig botConfig,
                       CommandMap validCommands) {
        this.botConfig = botConfig;
        this.validCommands = validCommands;
        this.commonResponsePool = CommonResponsePool.getInstance();
        this.notCompletedCommands = new ConcurrentHashMap<>();
        this.updatePool = new ConcurrentLinkedQueue<>();
        this.deletePool = new ConcurrentHashMap<>();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            updatePool.add(update);
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