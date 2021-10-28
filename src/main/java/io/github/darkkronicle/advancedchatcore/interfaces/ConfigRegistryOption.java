/*
 * Mozilla Public License v2.0
 *
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.interfaces;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * A {@link RegistryOption} that can be saved and loaded from a JSON file.
 *
 * @param <TYPE>
 */
@Environment(EnvType.CLIENT)
public interface ConfigRegistryOption<TYPE>
        extends RegistryOption<TYPE>, IConfigOptionListEntry, IJsonApplier {
    /**
     * Get's a configurable boolean for whether or not the option is active.
     *
     * @return Configurable boolean
     */
    ConfigStorage.SaveableConfig<ConfigBoolean> getActive();

    /**
     * Get's if the option is currently active.
     *
     * @return If the option is active
     */
    @Override
    default boolean isActive() {
        return getActive().config.getBooleanValue();
    }

    /**
     * Save's the config option and the object that it is wrapping.
     *
     * <p>By default it will only save if the option is active or not, but if the {@link TYPE}
     * implements {@link IJsonApplier} it will also save/load that object.
     *
     * @return Serialized object
     */
    @Override
    default JsonObject save() {
        JsonObject obj = new JsonObject();
        obj.add(getActive().key, getActive().config.getAsJsonElement());
        JsonObject extra = null;
        if (getOption() instanceof IJsonApplier) {
            extra = ((IJsonApplier) getOption()).save();
        }
        if (extra != null) {
            for (Map.Entry<String, JsonElement> e : extra.entrySet()) {
                obj.add(e.getKey(), e.getValue());
            }
        }
        return obj;
    }

    /**
     * Load's the config option and the object that it is wrapping.
     *
     * <p>By default it will only load if the option is active or not, but if the {@link TYPE}
     * implements {@link IJsonApplier} it will also save/load that object.
     */
    @Override
    default void load(JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return;
        }
        JsonObject obj = element.getAsJsonObject();
        getActive().config.setValueFromJsonElement(obj.get(getActive().key));
        if (getOption() instanceof IJsonApplier) {
            ((IJsonApplier) getOption()).load(obj);
        }
    }
}
