package io.github.darkkronicle.advancedchatcore.chat;

import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
    private final List<Consumer<ChatMessage>> onMessage = new ArrayList<>();

    @Getter
    private final List<Consumer<ChatMessage>> onStack = new ArrayList<>();

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
     * Add's a consumer that will accept a ChatMessage when it's added. This will not get triggered for stacks.
     * @param consumer Consumer to take ChatMessage
     */
    public void addOnMessage(Consumer<ChatMessage> consumer) {
        onMessage.add(consumer);
    }

    /**
     * Add's a consumer that will accept a ChatMessage when it's been stacked.
     * @param consumer Consumer to take ChatMessage
     */
    public void addOnStack(Consumer<ChatMessage> consumer) {
        onStack.add(consumer);
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

    /**
     * Add's a chat message to the history.
     * @param message
     */
    public boolean add(ChatMessage message) {
        for (int i = 0; i < ConfigStorage.General.CHAT_STACK.config.getIntegerValue() && i < messages.size(); i++) {
            ChatMessage chatLine = messages.get(i);
            if (message.isSimilar(chatLine)) {
                chatLine.setStacks(chatLine.getStacks() + 1);
                for (Consumer<ChatMessage> consumer : onStack) {
                    consumer.accept(chatLine);
                }
                return false;
            }
        }
        messages.add(0, message);
        while (this.messages.size() > maxLines) {
            this.messages.remove(this.messages.size() - 1);
        }
        for (Consumer<ChatMessage> consumer : onMessage) {
            consumer.accept(message);
        }
        return true;
    }

    /**
     * Remove's a message based off of it's messageId.
     * @param messageId Message ID to find and remove
     */
    public void removeMessage(int messageId) {
        this.messages.removeIf(line -> line.getId() == messageId);
    }

}
