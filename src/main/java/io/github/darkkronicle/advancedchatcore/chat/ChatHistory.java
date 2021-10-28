/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.chat;

import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import io.github.darkkronicle.advancedchatcore.interfaces.IChatMessageProcessor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/** A utility class to maintain the storage of the chat. */
@Environment(EnvType.CLIENT)
public class ChatHistory {

    private static final ChatHistory INSTANCE = new ChatHistory();

    /** Stored lines */
    @Getter private final List<ChatMessage> messages = new ArrayList<>();

    /** Maximum lines for storage */
    @Getter @Setter private int maxLines = 500;

    /** Runnable's to run when chat history is cleared */
    @Getter private final List<Runnable> onClear = new ArrayList<>();

    /** {@link IChatMessageProcessor} for when history is updated. */
    @Getter private final List<IChatMessageProcessor> onUpdate = new ArrayList<>();

    public static ChatHistory getInstance() {
        return INSTANCE;
    }

    private ChatHistory() {}

    /**
     * Add's a runnable that will trigger when all chat messages should be cleared.
     *
     * @param runnable Runnable to run
     */
    public void addOnClear(Runnable runnable) {
        onClear.add(runnable);
    }

    /**
     * Add's a {@link IChatMessageProcessor} that get's called on new messages, added messages,
     * stacked messages, or removed messages.
     *
     * @param processor Processor ot add
     */
    public void addOnUpdate(IChatMessageProcessor processor) {
        onUpdate.add(processor);
    }

    /** Goes through and clears all message data from everywhere. */
    public void clearAll() {
        this.messages.clear();
        for (Runnable r : onClear) {
            r.run();
        }
    }

    /** Clear's all the chat messages from the history */
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
     *
     * @param message
     */
    public boolean add(ChatMessage message) {
        sendUpdate(message, IChatMessageProcessor.UpdateType.NEW);
        for (int i = 0;
                i < ConfigStorage.General.CHAT_STACK.config.getIntegerValue()
                        && i < messages.size();
                i++) {
            // Check for stacks
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
            sendUpdate(
                    this.messages.remove(this.messages.size() - 1),
                    IChatMessageProcessor.UpdateType.REMOVE);
        }
        return true;
    }

    /**
     * Remove's a message based off of it's messageId.
     *
     * @param messageId Message ID to find and remove
     */
    public void removeMessage(int messageId) {
        List<ChatMessage> toRemove =
                this.messages.stream()
                        .filter(line -> line.getId() == messageId)
                        .collect(Collectors.toList());
        this.messages.removeAll(toRemove);
        for (ChatMessage m : toRemove) {
            sendUpdate(m, IChatMessageProcessor.UpdateType.REMOVE);
        }
    }
}
