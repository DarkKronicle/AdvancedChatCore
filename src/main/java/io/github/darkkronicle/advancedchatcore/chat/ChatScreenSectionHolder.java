/*
 * Mozilla Public License v2.0
 *
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.chat;

import io.github.darkkronicle.advancedchatcore.interfaces.AdvancedChatScreenSection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import lombok.Getter;

/**
 * A class to handle the construction and distribution of {@link AdvancedChatScreenSection} when
 * {@link AdvancedChatScreen} is created.
 */
public class ChatScreenSectionHolder {

    private static final ChatScreenSectionHolder INSTANCE = new ChatScreenSectionHolder();

    /** All suppliers for the sections */
    @Getter
    private final List<Function<AdvancedChatScreen, AdvancedChatScreenSection>> sectionSuppliers =
            new ArrayList<>();

    public static ChatScreenSectionHolder getInstance() {
        return INSTANCE;
    }

    private ChatScreenSectionHolder() {}

    public void addSectionSupplier(
            Function<AdvancedChatScreen, AdvancedChatScreenSection> sectionSupplier) {
        sectionSuppliers.add(sectionSupplier);
    }
}
