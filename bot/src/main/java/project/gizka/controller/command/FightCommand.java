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
        String text = "\n\n❗\uFE0FЕсли исход сражения решен по очкам здоровья, но вы не видите сообщение о победе, то можно " +
                "смело жмакать /fight. Это баг, я хз как его исправить❗\uFE0F\n\n";

        if (this.getState() == this.getNumOfArgs()) {
            FightLog fightLog = restClient.getFightLog(chatId);

            for (int i = 0; i < fightLog.getTurns().size(); i++) {
                var fightTurn = fightLog.getTurns().get(i);
                text = text + "\n" +
                        fightTurn.getAttacker() + " ⚔\uFE0F наносит противнику " + fightTurn.getDamage() + " урона.\n" +
                        fightTurn.getDefender() + " \uD83D\uDEE1\uFE0F сохраняет " + fightTurn.getHealthPoint() + " очков здоровья.\n";

                if (i % 2 == 0) {
                    text = " \uD83D\uDD25 Раунд " + (i / 2 + 1) + " \uD83D\uDD25\n" + text;
                }

                if (i == fightLog.getTurns().size() - 1 || i % 2 > 0) {
                    messages.add(new SendMessage(chatId, text));
                    text = "";
                }
            }

            text = "\n\uD83C\uDFC6\uD83C\uDF89 Победитель " + fightLog.getWinner() + "❗\uFE0F" +
                    "\n/fight - для нового сражения \uD83E\uDD3A";
            messages.add(new SendMessage(chatId, text));
            improveReadiness();

        }
        return messages;
    }

    private String askId() {
        return "Введите id героя";
    }
}
