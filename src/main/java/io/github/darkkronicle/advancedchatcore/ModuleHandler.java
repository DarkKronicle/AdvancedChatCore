/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;

public class ModuleHandler {

    private static final ModuleHandler INSTANCE = new ModuleHandler();

    @Getter private final List<Module> modules = new ArrayList<>();

    private ModuleHandler() {}

    private List<LoadOrder> toLoad = new ArrayList<>();

    public static ModuleHandler getInstance() {
        return INSTANCE;
    }

    public void registerModules() {
        modules.clear();
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            // Check if in "custom" it has "acmodule": true
            CustomValue acData = mod.getMetadata().getCustomValue("acmodule");
            if (acData == null) {
                continue;
            }
            if (acData.getType() == CustomValue.CvType.BOOLEAN && acData.getAsBoolean()) {
                // Add the module
                modules.add(new Module(mod.getMetadata().getId(), mod.getMetadata().getAuthors()));
            }
        }
    }

    public void registerInitHandler(String name, int priority, IInitializationHandler handler) {
        toLoad.add(new LoadOrder(name, priority, handler));
    }

    /** Do not call */
    public void load() {
        Collections.sort(toLoad);
        for (LoadOrder load : toLoad) {
            load.getHandler().registerModHandlers();
        }
        toLoad = null;
    }

    /**
     * Retrieves a {@link Module} based off of a mod ID.
     *
     * <p>This is useful for incompatible features or enabling others.
     *
     * @param modID Mod id of the mod
     * @return An optional containing the module if found.
     */
    public Optional<Module> fromId(String modID) {
        for (Module m : modules) {
            if (m.getModId().equals(modID)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }

    @AllArgsConstructor
    @Value
    public static class LoadOrder implements Comparable<LoadOrder> {
        String name;
        Integer order;
        IInitializationHandler handler;

        @Override
        public int compareTo(ModuleHandler.LoadOrder o) {
            int compared = order.compareTo(o.order);
            if (compared == 0) {
                return name.compareTo(o.getName());
            }
            return compared;
        }
    }
}
