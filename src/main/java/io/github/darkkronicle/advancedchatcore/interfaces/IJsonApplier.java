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

/** An interface to serialize and deserialize classes into themselves. */
public interface IJsonApplier {
    /**
     * Serializes the object into a JsonObject.
     *
     * @return Constructed JsonObject
     */
    JsonObject save();

    /**
     * Takes a JsonElement and loads it into the class.
     *
     * @param element Element to load
     */
    void load(JsonElement element);
}
