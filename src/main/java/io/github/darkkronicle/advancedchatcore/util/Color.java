/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import lombok.With;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class Color {

    @Getter int red;

    @Getter int green;

    @Getter int blue;

    @Getter
    @With(AccessLevel.PUBLIC)
    int alpha;

    @Getter int color;

    public Color(int color) {
        this.color = color;
        Color completeColor = ColorUtil.intToColor4f(color);
        this.red = completeColor.red();
        this.green = completeColor.green();
        this.blue = completeColor.blue();
        this.alpha = completeColor.alpha();
    }

    public Color(int red, int green, int blue, int alpha) {
        if (red > 255) {
            red = 255;
        }
        if (green > 255) {
            green = 255;
        }
        if (blue > 255) {
            blue = 255;
        }
        if (alpha > 255) {
            alpha = 255;
        }
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.color = ColorUtil.colorToInt4f(this);
    }

    /** Generated for use of Lombok's @With */
    public Color(int red, int green, int blue, int alpha, int color) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.color = ColorUtil.colorToInt4f(this);
    }

    public String getString() {
        return String.format("#%08X", color);
    }
}
