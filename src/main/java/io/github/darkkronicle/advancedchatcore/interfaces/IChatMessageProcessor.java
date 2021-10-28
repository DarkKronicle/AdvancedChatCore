/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.interfaces;

import io.github.darkkronicle.advancedchatcore.chat.ChatMessage;

/**
 * A processor that will get updated whenever an event in {@link
 * io.github.darkkronicle.advancedchatcore.chat.ChatHistory} happens. This gets triggered for
 * specific {@link ChatMessage}.
 */
public interface IChatMessageProcessor {
    /** Types of chat message events that can be referenced */
    enum UpdateType {
        /** A new message is sent. This still gets triggered even if the message is stacked. */
        NEW,

        /** A message is added to the history. This does not get called on stack. */
        ADDED,

        /** A previous message has been stacked. The stack number has changed. */
        STACK,

        /**
         * A message is removed. This does not get called on clear, but only if a specific message
         * was called to remove.
         */
        REMOVE,
    }

    /**
     * A method to handle a {@link ChatMessage} update.
     *
     * @param message Message that was updated
     * @param type Type of the update
     */
    void onMessageUpdate(ChatMessage message, UpdateType type);
}
