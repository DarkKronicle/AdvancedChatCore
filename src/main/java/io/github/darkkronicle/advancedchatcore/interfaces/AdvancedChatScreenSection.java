/*
 * Mozilla Public License v2.0
 *
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.interfaces;

import io.github.darkkronicle.advancedchatcore.chat.AdvancedChatScreen;
import lombok.Getter;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;

/**
 * A class meant to extend onto the {@link AdvancedChatScreen}
 *
 * <p>This is used so that many modules can add onto the screen without problems occuring.
 */
public abstract class AdvancedChatScreenSection implements Drawable {

    /** The {@link AdvancedChatScreen} that is linked to this section */
    @Getter private final AdvancedChatScreen screen;

    public AdvancedChatScreenSection(AdvancedChatScreen screen) {
        this.screen = screen;
    }

    /** Triggers when the gui is initiated */
    public void initGui() {}

    /**
     * Triggered when the window is resized
     *
     * @param width Window width
     * @param height Window height
     */
    public void resize(int width, int height) {}

    /** Triggered when the GUI is closed */
    public void removed() {}

    /**
     * Triggered when the chatfield text is pudated
     *
     * @param chatText Updated value (?)
     * @param text The text of the chatfield
     */
    public void onChatFieldUpdate(String chatText, String text) {}

    /**
     * Triggered when a key is pressed
     *
     * @param keyCode Keycode
     * @param scanCode Scancode
     * @param modifiers Modifiers
     * @return If it was handled and should stop.
     */
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    /**
     * Triggered when the mouse is scrolled
     *
     * @param mouseX MouseX
     * @param mouseY MouseY
     * @param amount Scroll amount
     * @return If it was handled and should stop.
     */
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return false;
    }

    /**
     * Triggered when the mouse is clicked
     *
     * @param mouseX MouseX
     * @param mouseY MouseY
     * @param button Mouse button
     * @return If it was handled and should stop.
     */
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    /**
     * Triggered when the mouse click is released
     *
     * @param mouseX MouseX
     * @param mouseY MouseY
     * @param mouseButton Mouse button
     * @return If it was handled and should stop.
     */
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    /**
     * @param mouseX
     * @param mouseY
     * @param button
     * @param deltaX
     * @param deltaY
     * @return If it was handled and should stop.
     */
    public boolean mouseDragged(
            double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    /**
     * Called when the chat field gets set due to history (up arrows)
     *
     * @param hist History set from
     */
    public void setChatFromHistory(String hist) {}

    /**
     * Called when the screen renders.
     *
     * @param matrixStack MatrixStack
     * @param mouseX MouseX
     * @param mouseY MouseY
     * @param partialTicks Partial tick from the last tick
     */
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {}
}
