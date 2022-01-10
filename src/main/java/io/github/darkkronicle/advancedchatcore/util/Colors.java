/*
 * Copyright (C) 2021-2022 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.util;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import fi.dy.masa.malilib.util.FileUtils;
import io.github.darkkronicle.advancedchatcore.AdvancedChatCore;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.Level;

/** A class storing data of colors as defined in colors.toml */
@Environment(EnvType.CLIENT)
public class Colors {

    private static final Colors INSTANCE = new Colors();

    public static Colors getInstance() {
        return INSTANCE;
    }

    @Getter private final Map<String, Color> colors = new HashMap<>();
    @Getter private final Map<String, Palette> palettes = new HashMap<>();
    private String defaultPalette = "";

    private Colors() {}

    /**
     * Loads configuration from colors.toml
     *
     * <p>If the file doesn't exist in the configuration directory, it's copied from resources
     */
    public void load() {
        colors.clear();
        palettes.clear();

        // Get file or create if it doesn't exist
        File file = FileUtils.getConfigDirectory()
                        .toPath()
                        .resolve("advancedchat")
                        .resolve("colors.toml")
                        .toFile();
        if (!file.exists()) {
            try {
                org.apache.commons.io.FileUtils.copyInputStreamToFile(AdvancedChatCore.getResource("colors.toml"), file);
            } catch (Exception e) {
                // Rip
                AdvancedChatCore.LOGGER.log(Level.ERROR, "Colors could not be loaded correctly!", e);
                return;
            }
        }

        // Use night-config toml parsing
        FileConfig config = TomlUtil.loadFileWithDefaults(file, "colors.toml");

        // Assign colors
        Optional<Config> customColors = config.getOptional("color");
        if (customColors.isPresent()) {
            for (Config.Entry entry : customColors.get().entrySet()) {
                this.colors.put(entry.getKey(), hexToSimple(entry.getValue()));
            }
        }

        Optional<Config> palettes = config.getOptional("palettes");
        if (palettes.isPresent()) {
            // Nested configuration
            for (Config.Entry entry : palettes.get().entrySet()) {
                ArrayList<Color> colors = new ArrayList<>();
                for (String c : (List<String>) entry.getValue()) {
                    if (this.colors.containsKey(c)) {
                        // Allow color reference
                        colors.add(this.colors.get(c));
                    } else {
                        colors.add(hexToSimple(c));
                    }
                }
                this.palettes.put(entry.getKey(), new Palette(colors));
            }
        }

        // Set default
        Optional<String> defaultPalette = config.getOptional("default_palette");
        defaultPalette.ifPresent(s -> this.defaultPalette = s);
        config.close();
    }

    /**
     * Get's the default palette specified by the user.
     *
     * @return Palette user specified
     */
    public Palette getDefault() {
        if (this.palettes.containsKey(defaultPalette)) {
            return this.palettes.get(defaultPalette);
        }
        AdvancedChatCore.LOGGER.log(
                Level.WARN, "Default Palette " + defaultPalette + " does not exist!");
        return this.palettes.values().toArray(new Palette[0])[0];
    }

    /**
     * Get's a palette by name. If it doesn't exist, an empty optional is returned
     *
     * @param name Name of the palette from colors.toml
     * @return Palette
     */
    public Optional<Palette> get(String name) {
        Palette palette = palettes.get(name);
        if (palette != null) {
            return Optional.of(palette);
        }
        return Optional.empty();
    }

    private static Color hexToSimple(String string) {
        if (string.length() != 7 && string.length() != 9) {
            // Not #ffffff (so invalid!)
            AdvancedChatCore.LOGGER.log(
                    Level.WARN,
                    "Color " + string + " isn't formatted correctly! (#ffffff) (#ffffffff)");
            return new Color(255, 255, 255, 255);
        }
        string = string.substring(1);
        try {
            int red = Integer.parseInt(string.substring(0, 2), 16);
            int green = Integer.parseInt(string.substring(2, 4), 16);
            int blue = Integer.parseInt(string.substring(4, 6), 16);
            int alpha = 255;
            if (string.length() == 8) {
                alpha = Integer.parseInt(string.substring(6));
            }
            return new Color(red, green, blue, alpha);
        } catch (Exception e) {
            AdvancedChatCore.LOGGER.log(
                    Level.WARN, "Couldn't convert " + string + " into a color!", e);
        }
        return new Color(255, 255, 255, 255);
    }

    public Optional<Color> getColor(String key) {
        if (colors.containsKey(key)) {
            return Optional.of(colors.get(key));
        }
        return Optional.empty();
    }

    public Color getColorOrWhite(String key) {
        return getColor(key).orElse(new Color(255, 255, 255, 255));
    }

    public static class Palette {

        @Getter private final List<Color> colors;

        public Palette(List<Color> colors) {
            this.colors = colors;
        }
    }
}
