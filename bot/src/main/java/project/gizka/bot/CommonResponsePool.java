package project.gizka.bot;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Getter
public class CommonResponsePool {
    private final Queue<SendMessage> commonPool;
    private final PrivateResponsePools privateResponsePools;
    private static CommonResponsePool instance;
    private final int THREADS = 2;
    private final String WAIT_MESSAGE = "Ожидание...";
    private ScheduledExecutorService executorService;

    private CommonResponsePool() {
        this.commonPool = new ConcurrentLinkedQueue<>();
        this.privateResponsePools = PrivateResponsePools.getInstance();
        executorService = Executors.newScheduledThreadPool(THREADS);
    }

    public static synchronized CommonResponsePool getInstance() {
        if (instance == null) {
            instance = new CommonResponsePool();
        }
        return instance;
    }

    public void addPrivatePoolToCommonPool() {
        for (String key : privateResponsePools.getPrivateResponsePools().keySet()) {
            Queue<SendMessage> messages = privateResponsePools.getPrivateResponsePools().get(key);
            int delay = 0;

            for (SendMessage ignored : messages) {
                if (messages.size() > 1) {
                    executorService.schedule(() -> {
                        synchronized (commonPool) {
                            commonPool.add(messages.poll());
                            if (!messages.isEmpty()) {
                                SendMessage waitMessage = new SendMessage(key, WAIT_MESSAGE);
                                commonPool.add(waitMessage);
                            }
                        }
                    }, delay, TimeUnit.SECONDS);
                    delay += 10;
                } else {
                    synchronized (commonPool) {
                        commonPool.addAll(messages);
                    }
                }
            }

            privateResponsePools.getPrivateResponsePools().remove(key);
        }
    }
}

