/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.chat;

import io.github.darkkronicle.advancedchatcore.interfaces.AdvancedChatScreenSection;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.util.math.MatrixStack;

/** Handles the CommandSuggestor for the chat */
@Environment(EnvType.CLIENT)
public class DefaultChatSuggestor extends AdvancedChatScreenSection {

    private ChatInputSuggestor commandSuggestor;

    public DefaultChatSuggestor(AdvancedChatScreen screen) {
        super(screen);
    }

    @Override
    public void onChatFieldUpdate(String chatText, String text) {
        this.commandSuggestor.setWindowActive(!text.equals(getScreen().getOriginalChatText()));
        this.commandSuggestor.refresh();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.commandSuggestor.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        this.commandSuggestor.render(context, mouseX, mouseY);
    }

    @Override
    public void setChatFromHistory(String hist) {
        this.commandSuggestor.setWindowActive(false);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return this.commandSuggestor.mouseScrolled(amount);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.commandSuggestor.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void resize(int width, int height) {
        this.commandSuggestor.refresh();
    }

    @Override
    public void initGui() {
        MinecraftClient client = MinecraftClient.getInstance();
        AdvancedChatScreen screen = getScreen();
        this.commandSuggestor =
                new ChatInputSuggestor(
                        client,
                        screen,
                        screen.chatField,
                        client.textRenderer,
                        false,
                        false,
                        1,
                        10,
                        true,
                        -805306368);
        this.commandSuggestor.refresh();
    }
}
