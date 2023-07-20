package project.gizka.command;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

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
    public abstract Queue<SendMessage> handle(Update update) throws Exception;

    public boolean isDone() {
        if (this.getReadiness() == this.getNumOfResponses()) {
            this.setDone(true);
        }
        return this.done;
    }

    protected void improveReadiness(){
        this.setReadiness(this.getReadiness()+1);
    }
}
