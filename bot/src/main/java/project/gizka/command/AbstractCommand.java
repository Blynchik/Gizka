package project.gizka.command;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Queue;

@Getter
@Setter
public abstract class AbstractCommand {

    private int numOfArgs;
    private int state;
    private boolean readyForProcess;

    public AbstractCommand() {
        this.state = 0;
        this.readyForProcess = false;
    }
    public abstract Queue<SendMessage> handle(Update update) throws Exception;

    public boolean checkReadyForProcess() {
        if (this.getState() > this.getNumOfArgs()) {
            this.setReadyForProcess(true);
        }
        this.setState(this.getState()+1);
        return this.isReadyForProcess();
    }
}
