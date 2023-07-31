package project.gizka.controller;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.client.RestClient;

import java.util.Queue;

@Getter
@Setter
public abstract class AbstractCommand {

    protected final RestClient restClient;
    private String command;
    private int numOfArgs;
    private int state;
    private boolean done;

    public AbstractCommand(RestClient restClient, int numOfArgs, String command) {
        this.restClient = restClient;
        this.numOfArgs = numOfArgs;
        this.command = command;
        this.state = 0;
        this.done = false;
    }

    public boolean isDone() {
        if (this.getState() == this.getNumOfArgs() + 1) {
            this.setDone(true);
        }
        return this.done;
    }

    protected void improveReadiness() {
        this.setState(this.getState() + 1);
    }

    protected abstract Queue<?> getMessages(Update update) throws Exception;
}
