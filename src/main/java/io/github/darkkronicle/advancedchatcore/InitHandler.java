/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import io.github.darkkronicle.advancedchatcore.chat.ChatHistoryProcessor;
import io.github.darkkronicle.advancedchatcore.chat.ChatScreenSectionHolder;
import io.github.darkkronicle.advancedchatcore.chat.DefaultChatSuggestor;
import io.github.darkkronicle.advancedchatcore.chat.MessageDispatcher;
import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import io.github.darkkronicle.advancedchatcore.config.SaveableConfig;
import io.github.darkkronicle.advancedchatcore.config.gui.GuiConfigHandler;
import io.github.darkkronicle.advancedchatcore.finder.CustomFinder;
import io.github.darkkronicle.advancedchatcore.finder.custom.ProfanityFinder;
import io.github.darkkronicle.advancedchatcore.util.FluidText;
import io.github.darkkronicle.advancedchatcore.util.ProfanityUtil;
import io.github.darkkronicle.advancedchatcore.util.StringMatch;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class InitHandler implements IInitializationHandler {

    @Override
    public void registerModHandlers() {
        // Setup modules
        ModuleHandler.getInstance().registerModules();
        ConfigManager.getInstance()
                .registerConfigHandler(AdvancedChatCore.MOD_ID, new ConfigStorage());
        // Setup chat history
        MessageDispatcher.getInstance().register(new ChatHistoryProcessor(), -1);
        List<IConfigBase> configBases = new ArrayList<>();
        for (SaveableConfig<? extends IConfigBase> saveable : ConfigStorage.General.OPTIONS) {
            configBases.add(saveable.config);
        }
        GuiConfigHandler.getInstance()
                .addGuiSection(
                        GuiConfigHandler.createGuiConfigSection(
                                "advancedchat.config.tab.general", ConfigStorage.General.OPTIONS));

        GuiConfigHandler.getInstance()
                .addGuiSection(
                        GuiConfigHandler.createGuiConfigSection(
                                "advancedchat.config.tab.chatscreen",
                                ConfigStorage.ChatScreen.OPTIONS));

        ProfanityUtil.getInstance().loadConfigs();
        MessageDispatcher.getInstance()
                .registerPreFilter(
                        text -> {
                            if (ConfigStorage.General.FILTER_PROFANITY.config.getBooleanValue()) {
                                List<StringMatch> profanity =
                                        ProfanityUtil.getInstance().getBadWords(text.getString());
                                if (profanity.size() == 0) {
                                    return Optional.empty();
                                }
                                Map<StringMatch, FluidText.StringInsert> insertions =
                                        new HashMap<>();
                                for (StringMatch bad : profanity) {
                                    insertions.put(
                                            bad,
                                            (current, match) ->
                                                    new FluidText(
                                                            current.withMessage(
                                                                    "*"
                                                                            .repeat(
                                                                                    bad.end
                                                                                            - bad.start))));
                                }
                                text.replaceStrings(insertions);
                                return Optional.of(text);
                            }
                            return Optional.empty();
                        },
                        -1);

        // This constructs the default chat suggestor
        ChatScreenSectionHolder.getInstance()
                .addSectionSupplier(
                        (advancedChatScreen -> {
                            if (AdvancedChatCore.CREATE_SUGGESTOR) {
                                return new DefaultChatSuggestor(advancedChatScreen);
                            }
                            return null;
                        }));

        CustomFinder.getInstance()
                .register(
                        ProfanityFinder::new,
                        "profanity",
                        "advancedchatcore.findtype.custom.profanity",
                        "advancedchatcore.findtype.custom.info.profanity");
    }
}
