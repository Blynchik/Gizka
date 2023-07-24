package project.gizka.bot;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Getter
public class CommonResponsePool {
    private final Queue<SendMessage> commonPool;
    private final PrivateResponsePools privateResponsePools;
    private static CommonResponsePool instance;

    private CommonResponsePool(){
        commonPool = new ConcurrentLinkedQueue<>();
        this.privateResponsePools = PrivateResponsePools.getInstance();
    }

    public static synchronized  CommonResponsePool getInstance(){
        if(instance == null){
            instance = new CommonResponsePool();
        }
        return instance;
    }

    public void addPrivatePoolToCommonPool() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
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
