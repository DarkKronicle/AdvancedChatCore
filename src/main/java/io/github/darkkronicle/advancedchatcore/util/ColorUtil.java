/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.util;

import lombok.experimental.UtilityClass;
import net.minecraft.util.Formatting;

/**
 * A static utility class that helps when dealing with colors that use bit shifting, like Minecraft.
 * In here there is a color storage class that makes getting int and creating new colors simple.
 */
@UtilityClass
public class ColorUtil {

    // Probably my best hope for color...
    // https://github.com/parzivail/StarWarsMod/blob/master/src/main/java/com/parzivail/util/ui/GLPalette.java
    // I don't like color ints :(
    // intToColor and colorToInt from parzivail https://github.com/parzivail (slightly modified to
    // account for Alpha)

    /**
     * Turns a packed RGB color into a Color
     *
     * @param rgb The color to unpack
     * @return The new Color
     */
    public Color intToColor4f(int rgb) {
        int alpha = rgb >> 24 & 0xFF;
        int red = rgb >> 16 & 0xFF;
        int green = rgb >> 8 & 0xFF;
        int blue = rgb & 0xFF;
        return new Color(red, green, blue, alpha);
    }

    /**
     * Turns a Color into a packed RGB int
     *
     * @param c The color to pack
     * @return The packed int
     */
    public int colorToInt4f(Color c) {
        int rgb = c.alpha();
        rgb = (rgb << 8) + c.red();
        rgb = (rgb << 8) + c.green();
        rgb = (rgb << 8) + c.blue();
        return rgb;
    }

    public Color fade(Color color, float percent) {
        float alpha = (float) color.alpha();
        return color.withAlpha((int) Math.floor((alpha * percent)));
    }

    public Color colorFromFormatting(Formatting formatting) {
        return new Color(formatting.getColorValue());
    }
}
