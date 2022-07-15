/*
 * Copyright (C) 2021-2022 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.chat;

import io.github.darkkronicle.advancedchatcore.AdvancedChatCore;
import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import io.github.darkkronicle.advancedchatcore.interfaces.IMessageProcessor;
import io.github.darkkronicle.advancedchatcore.mixin.MixinChatHudInvoker;
import io.github.darkkronicle.advancedchatcore.util.Color;
import io.github.darkkronicle.advancedchatcore.util.SearchUtils;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ChatHistoryProcessor implements IMessageProcessor {

    private static boolean sendToHud(Text text) {
        if (AdvancedChatCore.FORWARD_TO_HUD) {
            ((MixinChatHudInvoker) MinecraftClient.getInstance().inGameHud.getChatHud()).invokeAddMessage(
                    text, 0, MinecraftClient.getInstance().inGameHud.getTicks(), false);
            return true;
        }
        return false;
    }

    @Override
    public boolean process(Text text, @Nullable Text unfiltered) {
        if (unfiltered == null) {
            unfiltered = text;
        }

        // Put the time in
        LocalTime time = LocalTime.now();
        boolean showtime = ConfigStorage.General.SHOW_TIME.config.getBooleanValue();
        // Store original so we can get stuff without the time
        Text original = text.copy();
        if (showtime) {
            DateTimeFormatter format =
                    DateTimeFormatter.ofPattern(
                            ConfigStorage.General.TIME_FORMAT.config.getStringValue());
            String replaceFormat =
                    ConfigStorage.General.TIME_TEXT_FORMAT.config.getStringValue().replaceAll("&", "§");
            Color color = ConfigStorage.General.TIME_COLOR.config.get();
            Style style = Style.EMPTY;
            TextColor textColor = TextColor.fromRgb(color.color());
            style = style.withColor(textColor);
            text.getSiblings().add(0, Text.literal(replaceFormat.replaceAll("%TIME%", time.format(format))).fillStyle(style));
        }

        int width = 0;
        // Find player
        MessageOwner player =
                SearchUtils.getAuthor(
                        MinecraftClient.getInstance().getNetworkHandler(), unfiltered.getString());
        ChatMessage line = ChatMessage.builder()
                .displayText(text)
                .originalText(original)
                .owner(player)
                .id(0)
                .width(width)
                .creationTick(MinecraftClient.getInstance().inGameHud.getTicks())
                .time(time)
                .backgroundColor(new Color(0, 0, 0, 100))
                .build();
        if (ChatHistory.getInstance().add(line)) {
            sendToHud(line.getDisplayText());
        }
        return true;
    }
}
