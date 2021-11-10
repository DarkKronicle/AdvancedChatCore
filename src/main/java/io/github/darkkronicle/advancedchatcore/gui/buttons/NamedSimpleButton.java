/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.gui.buttons;

import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.util.StringUtils;

public class NamedSimpleButton extends ButtonGeneric {

    public NamedSimpleButton(int x, int y, String text) {
        this(x, y, text, true);
    }

    public NamedSimpleButton(int x, int y, String text, boolean alineLeft) {
        super(x, y, 5, 20, text);
        setWidth(StringUtils.getStringWidth(text) + 10);
        if (!alineLeft) {
            setX(this.x - this.width);
        }
    }
}
