/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.config.options;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.util.StringUtils;
import io.github.darkkronicle.advancedchatcore.util.Color;
import io.github.darkkronicle.advancedchatcore.util.ColorUtil;

public class ConfigColor extends fi.dy.masa.malilib.config.options.ConfigColor {
    private Color color;

    public ConfigColor(String name, Color defaultValue, String comment) {
        super(name, defaultValue.getString(), comment);
        this.color = defaultValue;
    }

    @Override
    public void setIntegerValue(int value) {
        super.setIntegerValue(value);
        setColor();
    }

    private void setColor() {
        this.color = new Color(getIntegerValue());
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        {
            try {
                if (element.isJsonPrimitive()) {
                    this.value =
                            this.getClampedValue(StringUtils.getColor(element.getAsString(), 0));
                    this.setIntegerValue(value);
                } else {
                    MaLiLib.logger.warn(
                            "Failed to set config value for '{}' from the JSON element '{}'",
                            this.getName(),
                            element);
                }
            } catch (Exception e) {
                MaLiLib.logger.warn(
                        "Failed to set config value for '{}' from the JSON element '{}'",
                        this.getName(),
                        element,
                        e);
            }
        }
    }

    public Color get() {
        return color;
    }

    @Deprecated
    public ColorUtil.SimpleColor getSimpleColor() {
        return ColorUtil.SimpleColor.fromColor(color);
    }
}
