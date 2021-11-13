/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.finder;

import io.github.darkkronicle.advancedchatcore.AdvancedChatCore;
import io.github.darkkronicle.advancedchatcore.interfaces.IFinder;
import io.github.darkkronicle.advancedchatcore.interfaces.RegistryOption;
import io.github.darkkronicle.advancedchatcore.util.AbstractRegistry;
import io.github.darkkronicle.advancedchatcore.util.StringMatch;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.Level;

@Environment(EnvType.CLIENT)
public class CustomFinder extends AbstractRegistry<IFinder, CustomFinder.CustomFinderOption>
        implements IFinder {

    private static final CustomFinder INSTANCE = new CustomFinder();

    public static CustomFinder getInstance() {
        return INSTANCE;
    }

    private CustomFinder() {}

    public static final String NAME = "customfind";

    @Override
    public boolean isMatch(String input, String toMatch) {
        Optional<IFinder> option = getFinder(toMatch);
        if (option.isEmpty()) {
            // Invalid :(
            AdvancedChatCore.LOGGER.log(Level.WARN, getHelp(toMatch));
            return false;
        }
        return option.get().isMatch(input, toMatch);
    }

    private String getHelp(String toMatch) {
        StringBuilder builder =
                new StringBuilder()
                        .append("Custom find type was used but the match ")
                        .append(toMatch)
                        .append(" does not exist in the registry! Possible correct options: ");
        for (CustomFinderOption o : getAll()) {
            builder.append(o.saveString).append(", ");
        }
        return builder.substring(0, builder.length() - 2);
    }

    @Override
    public List<StringMatch> getMatches(String input, String toMatch) {
        Optional<IFinder> option = getFinder(toMatch);
        if (option.isEmpty()) {
            // Invalid :(
            AdvancedChatCore.LOGGER.log(Level.WARN, getHelp(toMatch));
            return new ArrayList<>();
        }
        return option.get().getMatches(input, toMatch);
    }

    public Optional<IFinder> getFinder(String toMatch) {
        for (CustomFinderOption o : getAll()) {
            if (toMatch.startsWith(o.saveString)) {
                return Optional.of(o.getOption());
            }
        }
        return Optional.empty();
    }

    @Override
    public CustomFinder clone() {
        CustomFinder finder = new CustomFinder();
        for (CustomFinderOption o : getAll()) {
            finder.add(o.copy(this));
        }
        return finder;
    }

    @Override
    public CustomFinderOption constructOption(
            Supplier<IFinder> type,
            String saveString,
            String translation,
            String infoTranslation,
            boolean active,
            boolean setDefault,
            boolean hidden) {
        return new CustomFinderOption(
                type, saveString, translation, infoTranslation, active, hidden, this);
    }

    public static class CustomFinderOption implements RegistryOption<IFinder> {

        private final IFinder finder;
        private final String saveString;
        private final String translation;
        private final CustomFinder registry;
        private final String infoTranslation;
        private final boolean hidden;
        private final boolean active;

        private CustomFinderOption(
                Supplier<IFinder> finder,
                String saveString,
                String translation,
                String infoTranslation,
                boolean active,
                boolean hidden,
                CustomFinder registry) {
            this(finder.get(), saveString, translation, infoTranslation, active, hidden, registry);
        }

        // Only register
        private CustomFinderOption(
                IFinder finder,
                String saveString,
                String translation,
                String infoTranslation,
                boolean active,
                boolean hidden,
                CustomFinder registry) {
            this.finder = finder;
            this.saveString = saveString;
            this.translation = translation;
            this.registry = registry;
            this.infoTranslation = infoTranslation;
            this.hidden = hidden;
            this.active = active;
        }

        @Override
        public IFinder getOption() {
            return finder;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        @Override
        public String getSaveString() {
            return saveString;
        }

        @Override
        public CustomFinderOption copy(AbstractRegistry<IFinder, ?> registry) {
            return new CustomFinderOption(
                    finder,
                    saveString,
                    translation,
                    infoTranslation,
                    isActive(),
                    isHidden(),
                    registry == null ? this.registry : (CustomFinder) registry);
        }
    }
}
