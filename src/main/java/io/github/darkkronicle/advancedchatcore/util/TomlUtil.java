/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.util;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.io.ParsingMode;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.electronwill.nightconfig.toml.TomlParser;
import io.github.darkkronicle.advancedchatcore.AdvancedChatCore;
import java.io.File;
import lombok.experimental.UtilityClass;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.Level;

@Environment(EnvType.CLIENT)
@UtilityClass
public class TomlUtil {

    public FileConfig loadFile(File file) {
        FileConfig config = FileConfig.of(file);
        config.load();
        return config;
    }

    public FileConfig loadFileWithDefaults(File file, String defaultName) {
        TomlFormat tomlFormat = TomlFormat.instance();
        TomlParser tomlParser = tomlFormat.createParser();
        FileConfig config = loadFile(file);
        try {
            // Layer on top
            tomlParser.parse(AdvancedChatCore.getResource(defaultName), config, ParsingMode.ADD);
        } catch (Exception e) {
            AdvancedChatCore.LOGGER.log(
                    Level.ERROR, "Could not load default settings into " + defaultName, e);
        }
        return config;
    }
}
