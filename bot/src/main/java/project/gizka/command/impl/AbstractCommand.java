package project.gizka.command.impl;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.command.Command;

@Getter
@Setter
public abstract class AbstractCommand implements Command {

    private int state;

    public AbstractCommand(int state){
        this.state = state;
    }

    @Override
    public abstract SendMessage handle(Update update);

    @Override
    public abstract SendMessage callback(Update update);
}
