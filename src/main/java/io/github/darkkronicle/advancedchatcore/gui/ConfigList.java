/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.gui;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;
import fi.dy.masa.malilib.gui.wrappers.TextFieldWrapper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screen.Screen;

public abstract class ConfigList<TYPE, WIDGET extends ConfigListEntry<TYPE>>
        extends WidgetListBase<TYPE, WIDGET> {

    protected List<TextFieldWrapper<GuiTextFieldGeneric>> textFields = new ArrayList<>();

    public ConfigList(
            int x,
            int y,
            int width,
            int height,
            ISelectionListener<TYPE> selectionListener,
            Screen parent) {
        super(x, y, width, height, selectionListener);
        this.browserEntryHeight = 22;
        this.setParent(parent);
    }

    @Override
    protected void reCreateListEntryWidgets() {
        textFields.clear();
        super.reCreateListEntryWidgets();
    }

    public void addTextField(TextFieldWrapper<GuiTextFieldGeneric> text) {
        textFields.add(text);
    }

    protected void clearTextFieldFocus() {
        for (TextFieldWrapper<GuiTextFieldGeneric> field : this.textFields) {
            GuiTextFieldGeneric textField = field.getTextField();

            if (textField.isFocused()) {
                textField.setFocused(false);
                break;
            }
        }
    }

    @Override
    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        clearTextFieldFocus();
        return super.onMouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean onKeyTyped(int keyCode, int scanCode, int modifiers) {
        for (ConfigListEntry<TYPE> widget : this.listWidgets) {
            if (widget.onKeyTyped(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return super.onKeyTyped(keyCode, scanCode, modifiers);
    }
}
