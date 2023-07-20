package project.gizka.command.impl.adventurer;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.gizka.client.RestClient;
import project.gizka.command.AbstractCommand;
import project.gizka.util.FightLog;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@Setter
public class FightAdventurerCommand extends AbstractCommand {

    private final String command;
    private final RestClient restClient;
    private final int numOfArgs = 1;
    private String adventurerId;

    public FightAdventurerCommand(String command, RestClient restClient) {
        super.setNumOfResponses(numOfArgs + 1);
        this.command = command;
        this.restClient = restClient;
    }


    @Override
    public Queue<SendMessage> handle(Update update) throws Exception {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        Queue<SendMessage> messages = new LinkedList<>();
        String text = "";

        if (this.getReadiness() == numOfArgs) {
            adventurerId = message.getText();
            FightLog fightLog = restClient.getFightLog(adventurerId);

            for (int i = 0; i < fightLog.getTurns().size(); i++) {
                var fightTurn = fightLog.getTurns().get(i);
                text = text + "\n"+fightTurn.toString()+"\n";
                if(i%2>0){
                    text ="Раунд "+ (i/2+1) + text;
                    messages.add(new SendMessage(chatId, text));
                    text = "";
                }
            }

            text = "\nПобедитель " + fightLog.getWinner();
            messages.add(new SendMessage(chatId, text));
            improveReadiness();
        } else if (this.getReadiness() == 0) {
            text = askId();
            messages.add(new SendMessage(chatId,text));
            improveReadiness();
        }

        return messages;
    }

    private String askId() {
        return "Введите id героя";
    }
}
