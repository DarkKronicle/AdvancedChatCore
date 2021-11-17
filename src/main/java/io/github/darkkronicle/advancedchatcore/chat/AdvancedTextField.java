/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.chat;

import fi.dy.masa.malilib.util.KeyCodes;
import io.github.darkkronicle.advancedchatcore.util.TextUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class AdvancedTextField extends TextFieldWidget {

    /**
     * Stores the last saved snapshot of the box. This ensures that not every character update is
     * put in, but instead groups.
     */
    private String lastSaved = "";

    /** Snapshots of chat box */
    private final List<String> history = new ArrayList<>();

    private int historyIndex = -1;

    public AdvancedTextField(
            TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        this(textRenderer, x, y, width, height, null, text);
    }

    public AdvancedTextField(
            TextRenderer textRenderer,
            int x,
            int y,
            int width,
            int height,
            @Nullable TextFieldWidget copyFrom,
            Text text) {
        super(textRenderer, x, y, width, height, copyFrom, text);
        history.add("");
    }

    public static boolean isUndo(int code) {
        // Undo (Ctrl + Z)
        return code == KeyCodes.KEY_Z && Screen.hasControlDown() && !Screen.hasAltDown();
    }

    /** Triggers undo for the text box */
    public void undo() {
        // Save the current snapshot if it's been edited
        if (!this.lastSaved.equals(this.getText()) && historyIndex < 0) {
            addToHistory(getText());
        }
        // History index < 0 means not in the middle of undoing
        if (historyIndex < 0) {
            historyIndex = history.size() - 1;
        }
        // Check that we're not at index 0
        if (historyIndex != 0) {
            historyIndex--;
        }
        // Set the text but don't update
        setText(history.get(historyIndex), false);
    }

    public void redo() {
        if (historyIndex < 0 || historyIndex >= history.size() - 1) {
            // No stuff to redo...
            return;
        }
        historyIndex++;
        setText(history.get(historyIndex), false);
    }

    @Override
    public void write(String text) {
        super.write(text);
        updateHistory();
    }

    @Override
    public void eraseCharacters(int characterOffset) {
        super.eraseCharacters(characterOffset);
        updateHistory();
    }

    /**
     * Sets the text for the text field
     *
     * @param text Text to set
     * @param update Updates the history
     */
    public void setText(String text, boolean update) {
        // Wrapper class for setText
        super.setText(text);
        if (update) {
            updateHistory();
        }
    }

    @Override
    public void setText(String text) {
        setText(text, true);
    }

    private void updateHistory() {
        if (historyIndex >= 0) {
            // Remove all history after what has gone back
            pruneHistory(historyIndex + 1);
            historyIndex = -1;
        }
        // Check to see if it should log
        int dif = getText().length() - lastSaved.length();
        double sim = TextUtil.similarity(getText(), lastSaved);
        if (sim >= .3 && (dif < 5 && dif * -1 < 5) || (sim >= .9)) {
            return;
        }
        addToHistory(getText());
    }

    private void addToHistory(String text) {
        this.lastSaved = text;
        this.history.add(text);
        while (this.history.size() > 50) {
            this.history.remove(0);
        }
    }

    /**
     * Remove's all history past a certain index
     *
     * @param index Index to prune from. Non-inclusive.
     */
    private void pruneHistory(int index) {
        if (index == 0) {
            history.clear();
            return;
        }
        while (history.size() > index) {
            history.remove(history.size() - 1);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isActive()) {
            return false;
        }
        if (!isUndo(keyCode)) {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
        if (Screen.hasShiftDown()) {
            redo();
        } else {
            undo();
        }
        return true;
    }
}
