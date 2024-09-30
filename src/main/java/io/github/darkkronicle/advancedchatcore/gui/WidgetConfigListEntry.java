/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.gui.wrappers.TextFieldWrapper;
import fi.dy.masa.malilib.render.RenderUtils;
import io.github.darkkronicle.advancedchatcore.util.Colors;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public abstract class WidgetConfigListEntry<TYPE> extends WidgetListEntryBase<TYPE> {

    private final boolean odd;
    private final List<String> hoverLines;

    @Setter @Getter private int buttonStartX;

    public WidgetConfigListEntry(
            int x, int y, int width, int height, boolean isOdd, TYPE entry, int listIndex) {
        this(x, y, width, height, isOdd, entry, listIndex, null);
    }

    public WidgetConfigListEntry(
            int x,
            int y,
            int width,
            int height,
            boolean isOdd,
            TYPE entry,
            int listIndex,
            List<String> hoverLines) {
        super(x, y, width, height, entry, listIndex);
        this.odd = isOdd;
        this.hoverLines = hoverLines;
        this.buttonStartX = x + width;
    }

    /** Get's the name to render for the entry. */
    public String getName() {
        return "";
    }

    public List<TextFieldWrapper<GuiTextFieldGeneric>> getTextFields() {
        return null;
    }

    @Override
    public void render(int mouseX, int mouseY, boolean selected, DrawContext context) {
        RenderUtils.color(1f, 1f, 1f, 1f);

        // Draw a lighter background for the hovered and the selected entry
        if (selected || this.isMouseOver(mouseX, mouseY)) {
            RenderUtils.drawRect(
                    this.x,
                    this.y,
                    this.width,
                    this.height,
                    Colors.getInstance().getColorOrWhite("white").withAlpha(150).color());
        } else if (this.odd) {
            RenderUtils.drawRect(
                    this.x,
                    this.y,
                    this.width,
                    this.height,
                    Colors.getInstance().getColorOrWhite("white").withAlpha(70).color());
        } else {
            RenderUtils.drawRect(
                    this.x,
                    this.y,
                    this.width,
                    this.height,
                    Colors.getInstance().getColorOrWhite("white").withAlpha(50).color());
        }

        renderEntry(mouseX, mouseY, selected, context);

        RenderUtils.color(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();

        this.drawTextFields(mouseX, mouseY, context);

        super.render(mouseX, mouseY, selected, context);

        RenderUtils.disableDiffuseLighting();
    }

    /**
     * Render's in the middle of the rendering cycle. After the background, but before it goes to
     * super.
     */
    public void renderEntry(int mouseX, int mouseY, boolean selected, DrawContext context) {
        String name = getName();
        this.drawString(
                this.x + 4,
                this.y + 7,
                Colors.getInstance().getColorOrWhite("white").color(),
                name,
                context);
    }

    @Override
    public void postRenderHovered(
            int mouseX, int mouseY, boolean selected, DrawContext context) {
        super.postRenderHovered(mouseX, mouseY, selected, context);
        if (hoverLines == null) {
            return;
        }

        if (mouseX >= this.x
                && mouseX < this.buttonStartX
                && mouseY >= this.y
                && mouseY <= this.y + this.height) {
            RenderUtils.drawHoverText(mouseX, mouseY, this.hoverLines, context);
        }
    }

    @Override
    protected boolean onKeyTypedImpl(int keyCode, int scanCode, int modifiers) {
        if (getTextFields() == null) {
            return false;
        }
        for (TextFieldWrapper<GuiTextFieldGeneric> field : getTextFields()) {
            if (field != null && field.isFocused()) {
                return field.onKeyTyped(keyCode, scanCode, modifiers);
            }
        }
        return false;
    }

    @Override
    protected boolean onCharTypedImpl(char charIn, int modifiers) {
        if (getTextFields() != null) {
            for (TextFieldWrapper<GuiTextFieldGeneric> field : getTextFields()) {
                if (field != null && field.onCharTyped(charIn, modifiers)) {
                    return true;
                }
            }
        }

        return super.onCharTypedImpl(charIn, modifiers);
    }

    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        if (super.onMouseClickedImpl(mouseX, mouseY, mouseButton)) {
            return true;
        }

        boolean ret = false;

        if (getTextFields() != null) {
            for (TextFieldWrapper<GuiTextFieldGeneric> field : getTextFields()) {
                if (field != null) {
                    ret = field.getTextField().mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }

        if (!this.subWidgets.isEmpty()) {
            for (WidgetBase widget : this.subWidgets) {
                ret |=
                        widget.isMouseOver(mouseX, mouseY)
                                && widget.onMouseClicked(mouseX, mouseY, mouseButton);
            }
        }

        return ret;
    }

    protected void drawTextFields(int mouseX, int mouseY, DrawContext context) {
        if (getTextFields() == null) {
            return;
        }
        for (TextFieldWrapper<GuiTextFieldGeneric> field : getTextFields()) {
            field.getTextField().render(context, mouseX, mouseY, 0f);
        }
    }
}
