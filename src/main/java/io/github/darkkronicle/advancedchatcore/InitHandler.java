/*
 * Copyright (C) 2021-2022 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import fi.dy.masa.malilib.util.InfoUtils;
import io.github.darkkronicle.advancedchatcore.chat.*;
import io.github.darkkronicle.advancedchatcore.config.CommandsHandler;
import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import io.github.darkkronicle.advancedchatcore.config.gui.GuiConfig;
import io.github.darkkronicle.advancedchatcore.config.gui.GuiConfigHandler;
import io.github.darkkronicle.advancedchatcore.config.gui.TabSupplier;
import io.github.darkkronicle.advancedchatcore.finder.CustomFinder;
import io.github.darkkronicle.advancedchatcore.finder.custom.ProfanityFinder;
import io.github.darkkronicle.advancedchatcore.hotkeys.InputHandler;
import io.github.darkkronicle.advancedchatcore.util.ProfanityUtil;
import io.github.darkkronicle.advancedchatcore.util.StringInsert;
import io.github.darkkronicle.advancedchatcore.util.StringMatch;
import io.github.darkkronicle.advancedchatcore.util.TextUtil;
import java.util.*;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.text.Text;

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

        GuiConfigHandler.getInstance().addTab(
                GuiConfigHandler.children(
                        "advancedchatcore",
                        "advancedchat.tab.advancedchatcore",
                GuiConfigHandler.wrapOptions(
                        "core_general",
                        "advancedchatcore.tab.general",
                        ConfigStorage.General.OPTIONS.stream().map((saveableConfig) -> (IConfigBase) saveableConfig.config).toList()
                ),
                GuiConfigHandler.wrapOptions(
                        "chatscreen",
                        "advancedchatcore.tab.chatscreen",
                        ConfigStorage.ChatScreen.OPTIONS.stream().map((saveableConfig) -> (IConfigBase) saveableConfig.config).toList()
                ))
        );

        ProfanityUtil.getInstance().loadConfigs();
        MessageDispatcher.getInstance().registerPreFilter(text -> {
            if (ConfigStorage.General.FILTER_PROFANITY.config.getBooleanValue()) {
                List<StringMatch> profanity =
                        ProfanityUtil.getInstance().getBadWords(text.getString(), (float) ConfigStorage.General.PROFANITY_ABOVE.config.getDoubleValue(), ConfigStorage.General.PROFANITY_ON_WORD_BOUNDARIES.config.getBooleanValue());
                if (profanity.size() == 0) {
                    return Optional.empty();
                }
                Map<StringMatch, StringInsert> insertions =
                        new HashMap<>();
                for (StringMatch bad : profanity) {
                    insertions.put(bad, (current, match) ->
                            Text.literal("*".repeat(bad.end - bad.start)).fillStyle(current.getStyle())
                    );
                }
                text = TextUtil.replaceStrings(text, insertions);
                return Optional.of(text);
            }
            return Optional.empty();
        }, -1);

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

        CommandsHandler.getInstance().setup();
        InputHandler.getInstance().addDisplayName("core_general", "advancedchatcore.config.tab.hotkeysgeneral");
        InputHandler.getInstance().add("core_general", ConfigStorage.Hotkeys.OPEN_CHAT.config, (action, key) -> {
            if (MinecraftClient.getInstance().world == null) {
                return true;
            }
            GuiBase.openGui(new AdvancedChatScreen(""));
            return true;
        });
        InputHandler.getInstance().add("core_general", ConfigStorage.Hotkeys.OPEN_CHAT_WITH_LAST.config, (action, key) -> {
            if (MinecraftClient.getInstance().world == null) {
                return true;
            }
            GuiBase.openGui(new AdvancedChatScreen(0));
            return true;
        });
        InputHandler.getInstance().add("core_general", ConfigStorage.Hotkeys.OPEN_CHAT_FREE_MOVEMENT.config, (action, key) -> {
            if (MinecraftClient.getInstance().world == null) {
                return true;
            }
            // Manually update stuff so that movement keys are continued to be pressed
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.currentScreen != null) {
                client.currentScreen.removed();
            }
            client.currentScreen = new AdvancedChatScreen(true);
            BufferRenderer.unbindAll();
            client.mouse.unlockCursor();
            client.currentScreen.init(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());
            client.skipGameRender = false;;
            return true;
        });
        InputHandler.getInstance().add("core_general", ConfigStorage.Hotkeys.TOGGLE_PERMANENT.config, (action, key) -> {
            AdvancedChatScreen.PERMANENT_FOCUS = !AdvancedChatScreen.PERMANENT_FOCUS;
            InfoUtils.printActionbarMessage("advancedchatcore.message.togglepermanent");
            return true;
        });
        InputHandler.getInstance().add("core_general", ConfigStorage.Hotkeys.OPEN_SETTINGS.config, (action, key) -> {
            GuiBase.openGui(new GuiConfig());
            return true;
        });
        ModuleHandler.getInstance().load();
        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerMouseInputHandler(InputHandler.getInstance());

        List<TabSupplier> children = new ArrayList<>();

        for (Map.Entry<String, List<ConfigHotkey>> hotkeys : InputHandler.getInstance().getHotkeys().entrySet()) {
            List<IConfigBase> configs = hotkeys.getValue().stream().map(hotkey -> (IConfigBase) hotkey).toList();
            children.add(GuiConfigHandler.wrapOptions(hotkeys.getKey(), InputHandler.getInstance().getDisplayName(hotkeys.getKey()), configs));
        }

        GuiConfigHandler.getInstance().addTab(
                GuiConfigHandler.children(
                        "hotkeys",
                        "advancedchat.tab.hotkeys",
                        children.toArray(new TabSupplier[0])
                )
        );
    }
}
