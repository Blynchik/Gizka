package project.gizka.bot;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class PrivateResponsePools {
    private final Map<String, Queue<SendMessage>> privateResponsePools;
    private static PrivateResponsePools instance;

    private PrivateResponsePools() {
        privateResponsePools = new ConcurrentHashMap<>();
    }

    public static synchronized PrivateResponsePools getInstance() {
        if(instance == null) {
            instance = new PrivateResponsePools();
        }
        return instance;
    }

    public void addResponseToPrivatePool(Queue<SendMessage> messages, String chatId) {
        Queue<SendMessage> currentResponsePool = getCurrentResponsePool(chatId);
        currentResponsePool.addAll(messages);
    }

    private Queue<SendMessage> getCurrentResponsePool(String chatId) {
        if (privateResponsePools.containsKey(chatId)) {
            return privateResponsePools.get(chatId);
        } else {
            Queue<SendMessage> currentResponsePool = new ConcurrentLinkedQueue<>();
            privateResponsePools.put(chatId, currentResponsePool);
            return currentResponsePool;
        }
    }
}