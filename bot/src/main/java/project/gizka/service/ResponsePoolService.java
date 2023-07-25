package project.gizka.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import project.gizka.bot.CommonResponsePool;
import project.gizka.bot.PrivateResponsePools;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ResponsePoolService {
    private static final int THREADS = 2;
    private final Queue<SendMessage> commonPool = CommonResponsePool.getInstance().getCommonPool();
    private final PrivateResponsePools privateResponsePools = PrivateResponsePools.getInstance();

    public void addPrivatePoolToCommonPool() {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        for(String key : privateResponsePools.getPrivateResponsePools().keySet()){
            Queue<SendMessage> messages = privateResponsePools.getPrivateResponsePools().get(key);
            privateResponsePools.getPrivateResponsePools().remove(key);

            executor.submit(() -> {
                synchronized (commonPool) {
                    commonPool.addAll(messages);
                }
            });
        }
        executor.shutdown();
    }
}

