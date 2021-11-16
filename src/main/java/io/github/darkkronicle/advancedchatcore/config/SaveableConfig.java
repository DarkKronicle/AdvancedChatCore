/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.config;

import fi.dy.masa.malilib.config.IConfigBase;

public class SaveableConfig<T extends IConfigBase> {

    public final T config;
    public final String key;

    private SaveableConfig(String key, T config) {
        this.key = key;
        this.config = config;
    }

    public static <C extends IConfigBase> SaveableConfig<C> fromConfig(String key, C config) {
        return new SaveableConfig<>(key, config);
    }
}
