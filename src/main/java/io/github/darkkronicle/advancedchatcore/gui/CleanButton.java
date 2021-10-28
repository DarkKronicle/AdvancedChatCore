/*
 * Mozilla Public License v2.0
 *
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.gui;

import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.render.RenderUtils;
import io.github.darkkronicle.advancedchatcore.util.ColorUtil;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

/** A simple button */
@EqualsAndHashCode(callSuper = false)
@ToString
@Environment(EnvType.CLIENT)
public class CleanButton extends ButtonBase {

    protected ColorUtil.SimpleColor baseColor;

    private MinecraftClient client = MinecraftClient.getInstance();

    /**
     * Constructs a new simple clean button
     *
     * @param x X
     * @param y Y
     * @param width Width
     * @param height Height
     * @param baseColor Color that it should render when not hovered
     * @param text Text to render
     */
    public CleanButton(
            int x, int y, int width, int height, ColorUtil.SimpleColor baseColor, String text) {
        super(x, y, width, height, text);
        this.x = x;
        this.y = y;
        this.baseColor = baseColor;
    }

    @Override
    public void render(int mouseX, int mouseY, boolean selected, MatrixStack matrixStack) {
        int relMX = mouseX - x;
        int relMY = mouseY - y;
        hovered = relMX >= 0 && relMX <= width && relMY >= 0 && relMY <= height;
        ColorUtil.SimpleColor color = baseColor;
        if (hovered) {
            color = ColorUtil.WHITE.withAlpha(color.alpha());
        }
        RenderUtils.drawRect(x, y, width, height, color.color());
        drawCenteredString(
                (x + (width / 2)),
                (y + (height / 2) - 3),
                ColorUtil.WHITE.color(),
                displayString,
                matrixStack);
    }
}
