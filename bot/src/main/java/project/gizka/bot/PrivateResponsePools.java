package project.gizka.bot;

import lombok.Getter;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class PrivateResponsePools {
    private final Map<String, Queue<Object>> privateResponsePools;

    public PrivateResponsePools() {
        privateResponsePools = new ConcurrentHashMap<>();
    }

    public void addResponseToPrivatePool(Queue<?> messages, String chatId) {
        Queue<Object> currentResponsePool = getCurrentResponsePool(chatId);
        currentResponsePool.addAll(messages);
    }

    private Queue<Object> getCurrentResponsePool(String chatId) {
        if (privateResponsePools.containsKey(chatId)) {
            return privateResponsePools.get(chatId);
        } else {
            Queue<Object> currentResponsePool = new ConcurrentLinkedQueue<>();
            privateResponsePools.put(chatId, currentResponsePool);
            return currentResponsePool;
        }
    }
}