/*
 * Copyright (C) 2021-2022 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.config.options;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.util.StringUtils;
import io.github.darkkronicle.advancedchatcore.util.Color;
import io.github.darkkronicle.advancedchatcore.util.Colors;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ConfigColor extends fi.dy.masa.malilib.config.options.ConfigColor {
    private Color color;
    private final String defaultReference;
    private String reference = null;

    public ConfigColor(String name, Color defaultValue, String comment) {
        super(name, defaultValue.getString(), comment);
        this.color = defaultValue;
        this.defaultReference = null;
    }

    public ConfigColor(String name, String referenceDefault, String comment) {
        super(name, Colors.getInstance().getColorOrWhite(referenceDefault).toString(), comment);
        this.color = Colors.getInstance().getColorOrWhite(referenceDefault);
        this.defaultReference = referenceDefault;
    }

    @Override
    public void resetToDefault() {
        if (defaultReference != null) {
            this.setValueFromString(defaultReference);
        } else {
            this.setValueFromString(new Color(defaultValue).getString());
        }
        onValueChanged();
    }

    @Override
    public String getDefaultStringValue() {
        if (defaultReference == null) {
            return super.getDefaultStringValue();
        }
        return defaultReference;
    }

    @Override
    public void setValueFromString(String value) {
        Optional<Color> color = Colors.getInstance().getColor(value);
        if (color.isPresent()) {
            this.setIntegerValue(color.get().color());
            this.reference = value;
            this.setColor();
            return;
        }
        this.reference = null;
        super.setValueFromString(value);
        this.setColor();
    }

    @Override
    public String getStringValue() {
        if (reference != null) {
            return this.reference;
        }
        return super.getStringValue();
    }

    private void setColor() {
        this.color = new Color(getIntegerValue());
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        try {
            if (element.isJsonPrimitive()) {
                String value = element.getAsString();
                Optional<Color> color = Colors.getInstance().getColor(value);
                if (color.isPresent()) {
                    this.setIntegerValue(color.get().color());
                    this.reference = value;
                    this.setColor();
                    return;
                }
                this.value = this.getClampedValue(StringUtils.getColor(value, 0));
                this.setIntegerValue(this.value);
                this.setColor();
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

    @Override
    public JsonElement getAsJsonElement() {
        return new JsonPrimitive(getStringValue());
    }

    public Color get() {
        return color;
    }
}
