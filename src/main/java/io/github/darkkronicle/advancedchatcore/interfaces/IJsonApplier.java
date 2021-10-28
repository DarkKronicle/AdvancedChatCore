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
