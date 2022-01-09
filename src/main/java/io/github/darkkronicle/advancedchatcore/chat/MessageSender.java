/*
 * Copyright (C) 2022 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.chat;

import io.github.darkkronicle.advancedchatcore.interfaces.IStringFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.MinecraftClient;

public class MessageSender {

    private static final MessageSender INSTANCE = new MessageSender();
    private final MinecraftClient client = MinecraftClient.getInstance();

    public static MessageSender getInstance() {
        return INSTANCE;
    }

    private MessageSender() {}

    private final List<IStringFilter> filters = new ArrayList<>();

    public void addFilter(IStringFilter filter) {
        filters.add(filter);
    }

    public void addFilter(IStringFilter filter, int index) {
        filters.add(index, filter);
    }

    public void sendMessage(String string) {
        String unfiltered = string;
        for (IStringFilter filter : filters) {
            Optional<String> filtered = filter.filter(string);
            if (filtered.isPresent()) {
                string = filtered.get();
            }
        }
        if (string.length() > 256) {
            string = string.substring(0, 256);
        }
        this.client.inGameHud.getChatHud().addToMessageHistory(unfiltered);

        if (client.player != null) {
            this.client.player.sendChatMessage(string);
        }
    }
}
