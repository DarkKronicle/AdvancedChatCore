package io.github.darkkronicle.advancedchatcore.chat;

import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import io.github.darkkronicle.advancedchatcore.interfaces.IChatMessageProcessor;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A utility class to maintain the storage of the chat.
 */
@Environment(EnvType.CLIENT)
public class ChatHistory {

    private final static ChatHistory INSTANCE = new ChatHistory();

    @Getter
    private final List<ChatMessage> messages = new ArrayList<>();

    @Getter
    @Setter
    private int maxLines = 500;

    @Getter
    private final List<Runnable> onClear = new ArrayList<>();

    @Getter
    private final List<IChatMessageProcessor> onUpdate = new ArrayList<>();

    public static ChatHistory getInstance() {
        return INSTANCE;
    }

    private ChatHistory() {

    }

    /**
     * Add's a runnable that will trigger when all chat messages should be cleared.
     * @param runnable Runnable to run
     */
    public void addOnClear(Runnable runnable) {
        onClear.add(runnable);
    }

    /**
     * Add's a {@link IChatMessageProcessor} that get's called on new messages, added messages, stacked messages,
     * or removed messages.
     * @param processor Processor ot add
     */
    public void addOnUpdate(IChatMessageProcessor processor) {
        onUpdate.add(processor);
    }


    /**
     * Goes through and clears all message data from everywhere.
     */
    public void clearAll() {
        this.messages.clear();
        for (Runnable r : onClear) {
            r.run();
        }
    }

    /**
     * Clear's all the chat messages from the history
     */
    public void clear() {
        messages.clear();
    }

    private void sendUpdate(ChatMessage message, IChatMessageProcessor.UpdateType type) {
        for (IChatMessageProcessor consumer : onUpdate) {
            consumer.onMessageUpdate(message, type);
        }
    }

    /**
     * Add's a chat message to the history.
     * @param message
     */
    public boolean add(ChatMessage message) {
        sendUpdate(message, IChatMessageProcessor.UpdateType.NEW);
        for (int i = 0; i < ConfigStorage.General.CHAT_STACK.config.getIntegerValue() && i < messages.size(); i++) {
            ChatMessage chatLine = messages.get(i);
            if (message.isSimilar(chatLine)) {
                chatLine.setStacks(chatLine.getStacks() + 1);
                sendUpdate(chatLine, IChatMessageProcessor.UpdateType.STACK);
                return false;
            }
        }
        sendUpdate(message, IChatMessageProcessor.UpdateType.ADDED);
        messages.add(0, message);
        while (this.messages.size() > maxLines) {
            sendUpdate(this.messages.remove(this.messages.size() - 1), IChatMessageProcessor.UpdateType.REMOVE);
        }
        return true;
    }

    /**
     * Remove's a message based off of it's messageId.
     * @param messageId Message ID to find and remove
     */
    public void removeMessage(int messageId) {
        List<ChatMessage> toRemove = this.messages.stream().filter(line -> line.getId() == messageId).collect(Collectors.toList());
        this.messages.removeAll(toRemove);
        for (ChatMessage m : toRemove) {
            sendUpdate(m, IChatMessageProcessor.UpdateType.REMOVE);
        }
    }

}
