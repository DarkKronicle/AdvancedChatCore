package io.github.darkkronicle.advancedchatcore.chat;

import io.github.darkkronicle.advancedchatcore.interfaces.AdvancedChatScreenSection;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ChatScreenSectionHolder {

    private final static ChatScreenSectionHolder INSTANCE = new ChatScreenSectionHolder();

    @Getter
    private final List<Function<AdvancedChatScreen, AdvancedChatScreenSection>> sectionSuppliers = new ArrayList<>();

    public static ChatScreenSectionHolder getInstance() {
        return INSTANCE;
    }

    private ChatScreenSectionHolder() {

    }

    public void addSectionSupplier(Function<AdvancedChatScreen, AdvancedChatScreenSection> sectionSupplier) {
        sectionSuppliers.add(sectionSupplier);
    }

}
