/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.config.options;

import fi.dy.masa.malilib.config.options.ConfigString;

public class ConfigRegex extends ConfigString {

    public ConfigRegex(String name, String defaultValue, String comment) {
        super(name, defaultValue, comment);
    }
}
