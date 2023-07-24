package project.gizka.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import project.gizka.bot.TelegramBot;
import project.gizka.service.MessageReceiver;
import project.gizka.service.MessageSender;

@Component
public class TelegramBotInitializer {
    private final TelegramBot telegramBot;
    private final MessageSender messageSender;
    private final MessageReceiver messageReceiver;
    private static final int SENDER_PRIORITY = 1;
    private static final int RECEIVER_PRIORITY = 3;

    @Autowired
    public TelegramBotInitializer(TelegramBot telegramBot,
                                  MessageReceiver messageReceiver,
                                  MessageSender messageSender) {
        this.telegramBot = telegramBot;
        this.messageReceiver = messageReceiver;
        this.messageSender = messageSender;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            //TODO
        }

        initReceiver();
        initSender();
    }

    private void initReceiver(){
        Thread receiver = new Thread(messageReceiver);
        receiver.setDaemon(true);
        receiver.setName("MessageReceiver");
        receiver.setPriority(RECEIVER_PRIORITY);
        receiver.start();
    }

    private void initSender(){
        Thread sender = new Thread(messageSender);
        sender.setDaemon(true);
        sender.setName("MessageSender");
        sender.setPriority(SENDER_PRIORITY);
        sender.start();
    }
}