package project.gizka.controller.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.client.RestClient;
import project.gizka.controller.AbstractCommand;
import project.gizka.util.FightLog;

import java.util.LinkedList;
import java.util.Queue;

public class FightCommand extends AbstractCommand {

    private final RestClient restClient;

    public FightCommand(RestClient restClient) {
        super(0, "/fight");
        this.restClient = restClient;
    }

    @Override
    protected Queue<?> getMessages(Update update) throws Exception {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        Queue<SendMessage> messages = new LinkedList<>();
        String text = "";

        if (this.getState() == this.getNumOfArgs()) {
            FightLog fightLog = restClient.getFightLog(chatId);

            for (int i = 0; i < fightLog.getTurns().size(); i++) {
                var fightTurn = fightLog.getTurns().get(i);
                text = text + "\n"+fightTurn.toString()+"\n";

                if(i%2==0){
                    text ="Раунд "+ (i/2+1) + text;
                }

                if(i == fightLog.getTurns().size()-1 || i%2>0){
                    messages.add(new SendMessage(chatId, text));
                    text = "";
                }
            }

            text = "\nПобедитель " + fightLog.getWinner();
            messages.add(new SendMessage(chatId, text));
            improveReadiness();

        }
        return messages;
    }

    private String askId() {
        return "Введите id героя";
    }
}
