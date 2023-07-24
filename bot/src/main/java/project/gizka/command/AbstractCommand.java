package project.gizka.command;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.bot.PrivateResponsePools;

import java.util.Queue;

@Getter
@Setter
public abstract class AbstractCommand {

    private int numOfResponses;
    private int readiness;
    private boolean done;

    public AbstractCommand() {
        this.readiness = 0;
        this.done = false;
    }

    public void handle(Update update) throws Exception {
        String chatId = update.getMessage().getChatId().toString();
        Queue<SendMessage> messages = getMessages(update);
        putResponseToPrivatePool(messages, chatId);
    }

    public boolean isDone() {
        if (this.getReadiness() == this.getNumOfResponses()) {
            this.setDone(true);
        }
        return this.done;
    }

    protected void improveReadiness() {
        this.setReadiness(this.getReadiness() + 1);
    }

    protected abstract Queue<SendMessage> getMessages(Update update) throws Exception;

    protected void putResponseToPrivatePool(Queue<SendMessage> messages, String chatId) {
        PrivateResponsePools.getInstance().addResponseToPrivatePool(messages, chatId);
    }
}
