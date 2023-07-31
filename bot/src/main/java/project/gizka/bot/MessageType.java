package project.gizka.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

public enum MessageType {
    PHOTO, TEXT, DELETE, NOT_DETECTED;

    public static MessageType getMessageType(Object object) {
        if (object instanceof SendMessage) return MessageType.TEXT;
        if (object instanceof SendPhoto) return MessageType.PHOTO;
        if (object instanceof DeleteMessage) return MessageType.DELETE;
        return MessageType.NOT_DETECTED;
    }
}
