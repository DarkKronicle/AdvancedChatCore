/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.gui.buttons;

import fi.dy.masa.malilib.util.StringUtils;

public enum Buttons {
    BACK("advancedchat.gui.button.back");

    private final String translationString;

    Buttons(String translationString) {
        this.translationString = translationString;
    }

    public NamedSimpleButton createButton(int x, int y) {
        return new NamedSimpleButton(x, y, StringUtils.translate(translationString));
    }
}
