package project.gizka.bot;

import lombok.Getter;
import org.hibernate.sql.Delete;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Getter
public class CommonResponsePool {
    private final Queue<Object> commonPool;
    private final PrivateResponsePools privateResponsePools;
    private final int THREADS = 2;
    private final String WAIT_MESSAGE = "⏳Ожидание...";
    private final int SECONDS_DELAY = 5;
    private final ScheduledExecutorService executorService;
    private boolean waitingMessageAdded = false;

    public CommonResponsePool() {
        this.commonPool = new ConcurrentLinkedQueue<>();
        this.privateResponsePools = new PrivateResponsePools();
        executorService = Executors.newScheduledThreadPool(THREADS);
    }

    public void addPrivatePoolToCommonPool() {
        for (String key : privateResponsePools.getPrivateResponsePools().keySet()) {
            Queue<Object> messages = privateResponsePools.getPrivateResponsePools().get(key);
            int delay = 0;
            for (int i = 0; i < messages.size(); i++) {
                if (messages.size() > 1) {
                    executorService.schedule(() -> {
                        synchronized (commonPool) {

                            if (waitingMessageAdded) {
                                DeleteMessage deleteMessage = new DeleteMessage();
                                deleteMessage.setChatId(key);
                                commonPool.add(deleteMessage);
                                waitingMessageAdded = false;
                            }

                            commonPool.add(messages.poll());

                            if (!messages.isEmpty()) {
                                SendMessage waitMessage = new SendMessage();
                                waitMessage.setChatId(key);
                                waitMessage.setText(WAIT_MESSAGE);
                                commonPool.add(waitMessage);
                                waitingMessageAdded = true;
                            }
                        }
                    }, delay, TimeUnit.SECONDS);
                    delay += SECONDS_DELAY;
                } else {
                    synchronized (commonPool) {
                        commonPool.add(messages.poll());
                    }
                }
            }
            privateResponsePools.getPrivateResponsePools().remove(key);
        }
    }
}

