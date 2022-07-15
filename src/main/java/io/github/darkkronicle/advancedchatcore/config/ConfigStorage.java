/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.config;

import com.google.common.collect.ImmutableList;
import io.github.darkkronicle.advancedchatcore.AdvancedChatCore;
import java.io.File;

import java.util.List;

import io.github.darkkronicle.darkkore.config.ModConfig;
import io.github.darkkronicle.darkkore.config.options.*;
import io.github.darkkronicle.darkkore.hotkeys.HotkeySettings;
import io.github.darkkronicle.darkkore.hotkeys.HotkeySettingsOption;
import io.github.darkkronicle.darkkore.intialization.profiles.PlayerContextCheck;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.FileUtil;
import org.lwjgl.glfw.GLFW;

public class ConfigStorage extends ModConfig {

    public static final String CONFIG_FILE_NAME = AdvancedChatCore.MOD_ID + ".json";

    public static class General {

        public static final String NAME = "general";

        public static String translate(String key) {
            return "advancedchat.config.general." + key;
        }

        public static final StringOption TIME_FORMAT =
                new StringOption("timeFormat", translate("timeformat"), translate("info.timeformat"), "hh:mm");

        public static final StringOption TIME_TEXT_FORMAT =
                new StringOption("timeTextFormat", translate("timetextformat"), translate("info.timetextformat"), "[%TIME%] ");

        public static final ColorOption TIME_COLOR =
                new ColorOption("time_color", translate("timecolor"), translate("info.timecolor"), new Color(255, 255, 255, 255));

        public static final BooleanOption SHOW_TIME =
                new BooleanOption("show_time", translate("showtime"), translate("info.showtime"), false);

        public static final BooleanOption CLEAR_ON_DISCONNECT =
                new BooleanOption("clearOnDisconnect", translate("clearondisconnect"), translate("info.clearondisconnect"), true);

        public static final IntegerOption CHAT_STACK =
                new IntegerOption("chatStack", translate("chatstack"), translate("info.chatstack"), 0, 0, 20);

        public static final BooleanOption CHAT_STACK_UPDATE =
                new BooleanOption("chatStackUpdate", translate("chatstackupdate"), translate("info.chatstackupdate"), false);

        public static final StringOption MESSAGE_OWNER_REGEX =
                new StringOption("messageOwnerRegex", translate("messageownerregex"), translate("info.messageownerregex"), "(?<!\\[)\\b[A-Za-z0-9_§]{3,16}\\b(?!\\])");

        public static final BooleanOption FILTER_PROFANITY =
                new BooleanOption("filterProfanity", translate("filterprofanity"), translate("info.filterprofanity"), false);

        public static final BooleanOption PROFANITY_ON_WORD_BOUNDARIES =
                new BooleanOption("profanityWordBoundaries", translate("profanitywordboundaries"), translate("info.profanitywordboundaries"), false);

        public static final DoubleOption PROFANITY_ABOVE =
                new DoubleOption(
                        "profanityAbove", translate("profanityabove"), translate("info.profanityabove"), 0, 0, 3);

        public static final ImmutableList<Option<?>> OPTIONS =
                ImmutableList.of(
                        TIME_FORMAT,
                        TIME_TEXT_FORMAT,
                        TIME_COLOR,
                        SHOW_TIME,
                        CLEAR_ON_DISCONNECT,
                        CHAT_STACK,
                        CHAT_STACK_UPDATE,
                        MESSAGE_OWNER_REGEX,
                        FILTER_PROFANITY,
                        PROFANITY_ABOVE,
                        PROFANITY_ON_WORD_BOUNDARIES
                );
    }

    public static class ChatScreen {

        public static final String NAME = "chatscreen";

        public static String translate(String key) {
            return "advancedchat.config.chatscreen." + key;
        }

        public static final BooleanOption PERSISTENT_TEXT =
                new BooleanOption("persistentText", translate("persistenttext"), translate("info.persistenttext"), false);

        public static final ColorOption COLOR =
                new ColorOption("color", translate("color"), translate("info.color"), new Color(0, 0, 0, 100));

        public static final BooleanOption MORE_TEXT =
                new BooleanOption("moreText", translate("moretext"), translate("info.moretext"), false);

        public static final ImmutableList<Option<?>> OPTIONS =
                ImmutableList.of(PERSISTENT_TEXT, COLOR, MORE_TEXT);
    }

    public static class Hotkeys {
        public static final String NAME = "hotkeys";

        public static String translate(String key) {
            return "advancedchat.config.hotkeys." + key;
        }

        public static final HotkeySettingsOption OPEN_CHAT = new HotkeySettingsOption(
                "openChat", translate("openchat"), translate("info.openchat"),
                new HotkeySettings(false, false, false, List.of(), PlayerContextCheck.getDefault()));

        public static final HotkeySettingsOption TOGGLE_PERMANENT = new HotkeySettingsOption(
                "togglePermanent", translate("togglepermanentfocus"), translate("info.togglepermanentfocus"),
                new HotkeySettings(false, false, false, List.of(), PlayerContextCheck.getDefault()));

        public static final HotkeySettingsOption OPEN_CHAT_WITH_LAST = new HotkeySettingsOption(
                "openChatWithLast", translate("openchatwithlast"), translate("info.openchatwithlast"),
                new HotkeySettings(false, false, false, List.of(GLFW.GLFW_KEY_UP), PlayerContextCheck.getDefault()));

        public static final HotkeySettingsOption OPEN_CHAT_FREE_MOVEMENT = new HotkeySettingsOption(
                "openChatFreeMovement", translate("openchatfreemovement"), translate("info.openchatfreemovement"),
                new HotkeySettings(false, false, false, List.of(), PlayerContextCheck.getDefault()));

        public static final HotkeySettingsOption OPEN_SETTINGS = new HotkeySettingsOption(
                "openSettings", translate("opensettings"), translate("info.opensettings"),
                new HotkeySettings(false, false, false, List.of(), PlayerContextCheck.getDefault()));

        public static final ImmutableList<Option<?>> OPTIONS =
                ImmutableList.of(OPEN_SETTINGS, OPEN_CHAT, OPEN_CHAT_FREE_MOVEMENT, TOGGLE_PERMANENT);
    }

    @Override
    public List<Option<?>> getOptions() {
        return null;
    }

    @Override
    public File getFile() {
        return new File(new File(FileUtil.getConfigDirectory(), "advancedchat"), CONFIG_FILE_NAME);
    }

}
