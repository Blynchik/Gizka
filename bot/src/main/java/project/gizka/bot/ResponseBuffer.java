package project.gizka.bot;

import lombok.ToString;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.LinkedList;
import java.util.Queue;

@ToString
public class ResponseBuffer {
    private Queue<SendMessage> messageQueue;

    public ResponseBuffer() {
        this.messageQueue = new LinkedList<>();
    }

    public synchronized void addMessage(SendMessage message) {
        this.messageQueue.add(message);
    }

    public synchronized void addMessage(Queue<SendMessage> messageQueue){
        this.messageQueue.addAll(messageQueue);
    }

    public synchronized SendMessage getMessage() {
        return messageQueue.poll();
    }

    public synchronized boolean hasMessages() {
        return !messageQueue.isEmpty();
    }
}