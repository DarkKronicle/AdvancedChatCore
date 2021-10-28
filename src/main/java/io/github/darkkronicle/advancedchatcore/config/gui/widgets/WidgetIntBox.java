/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.config.gui.widgets;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import io.github.darkkronicle.advancedchatcore.util.FindType;
import io.github.darkkronicle.advancedchatcore.util.SearchUtils;
import io.github.darkkronicle.advancedchatcore.util.StringMatch;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.font.TextRenderer;

public class WidgetIntBox extends GuiTextFieldGeneric {

    @Setter @Getter private Runnable apply = null;

    public WidgetIntBox(int x, int y, int width, int height, TextRenderer textRenderer) {
        super(x, y, width, height, textRenderer);
        this.setTextPredicate(
                text -> {
                    if (text.equals("")) {
                        return true;
                    }
                    try {
                        // Only allow numbers!
                        Integer.valueOf(text);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                    return true;
                });
        this.setDrawsBackground(true);
    }

    public Integer getInt() {
        String text = this.getText();
        if (text == null || text.length() == 0) {
            return null;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            // Extra catch
            Optional<List<StringMatch>> omatches =
                    SearchUtils.findMatches(text, "[0-9]+", FindType.REGEX);
            if (!omatches.isPresent()) {
                return null;
            }
            for (StringMatch m : omatches.get()) {
                try {
                    return Integer.parseInt(m.match);
                } catch (NumberFormatException err) {
                    return null;
                }
            }
        }
        return null;
    }
}
