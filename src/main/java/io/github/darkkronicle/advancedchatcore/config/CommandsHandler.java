/*
 * Copyright (C) 2022 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.config;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import fi.dy.masa.malilib.config.ConfigType;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigColor;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.config.options.ConfigString;
import io.github.darkkronicle.advancedchatcore.AdvancedChatCore;
import io.github.darkkronicle.advancedchatcore.util.*;
import io.github.darkkronicle.kommandlib.CommandManager;
import io.github.darkkronicle.kommandlib.command.ClientCommand;
import io.github.darkkronicle.kommandlib.command.CommandInvoker;
import io.github.darkkronicle.kommandlib.invokers.BaseCommandInvoker;
import io.github.darkkronicle.kommandlib.util.CommandUtil;
import io.github.darkkronicle.kommandlib.util.InfoUtil;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Optional;

public class CommandsHandler {

    private static final CommandsHandler INSTANCE = new CommandsHandler();
    private static String TEST_REGEX = "";

    public static CommandsHandler getInstance() {
        return INSTANCE;
    }

    private CommandsHandler() {}

    public CommandNode<ServerCommandSource> getOrCreateSubs(String... keys) {
        // Should always exist
        CommandInvoker<ServerCommandSource> main = CommandManager.getInstance().getInvoker("advancedchat").get();
        CommandNode<ServerCommandSource> node = main;
        for (String k : keys) {
            Optional<CommandNode<ServerCommandSource>> optional = CommandUtil.getChild(node, k);
            if (optional.isPresent()) {
                node = optional.get();
                continue;
            }
            CommandNode<ServerCommandSource> newNode = CommandUtil.literal(k).build();
            node.addChild(newNode);
            node = newNode;
        }
        return node;
    }

    public void addOptions(CommandNode<ServerCommandSource> subCommand, List<SaveableConfig<? extends IConfigBase>> options) {
        for (SaveableConfig<? extends IConfigBase> option : options) {
            addOption(subCommand, option);
        }
    }

    public void addOption(CommandNode<ServerCommandSource> parent, SaveableConfig<? extends IConfigBase> option) {
        CommandNode<ServerCommandSource> wrapped = getOptionCommandWrapper(option.config);
        if (wrapped == null) {
            return;
        }
        parent.addChild(CommandUtil.literal(option.key).executes(wrapped.getCommand()).then(wrapped).build());
    }

    private void sendInvalid(String message) {
        InfoUtil.sendChatMessage(message, Formatting.RED);
    }

    private CommandNode<ServerCommandSource> getOptionCommandWrapper(IConfigBase option) {
        if (option.getType() == ConfigType.BOOLEAN) {
            ConfigBoolean bool = (ConfigBoolean) option;
            return CommandUtil.argument("active", BoolArgumentType.bool()).executes(ClientCommand.of(context -> {
                Optional<Boolean> value = CommandUtil.getArgument(context, "active", Boolean.class);
                if (value.isEmpty()) {
                    sendInvalid("Active needs to be specified!");
                    return;
                }
                bool.setBooleanValue(value.get());
                InfoUtil.sendChatMessage("Set!");
            })).build();
        }
        if (option.getType() == ConfigType.INTEGER) {
            ConfigInteger intOption = (ConfigInteger) option;
            return CommandUtil.argument("number", IntegerArgumentType.integer(intOption.getMinIntegerValue(), intOption.getMaxIntegerValue())).executes(ClientCommand.of(context -> {
                Optional<Integer> value = CommandUtil.getArgument(context, "number", Integer.class);
                if (value.isEmpty()) {
                    sendInvalid("Number needs to be specified!");
                    return;
                }
                intOption.setIntegerValue(value.get());
                InfoUtil.sendChatMessage("Set!");
            })).build();
        }
        if (option.getType() == ConfigType.DOUBLE) {
            ConfigDouble doubleOption = (ConfigDouble) option;
            return CommandUtil.argument("number", DoubleArgumentType.doubleArg(doubleOption.getMinDoubleValue(), doubleOption.getMaxDoubleValue())).executes(ClientCommand.of(context -> {
                Optional<Double> value = CommandUtil.getArgument(context, "number", Double.class);
                if (value.isEmpty()) {
                    sendInvalid("Number needs to be specified!");
                    return;
                }
                doubleOption.setDoubleValue(value.get());
                InfoUtil.sendChatMessage("Set!");
            })).build();
        }
        if (option.getType() == ConfigType.STRING) {
            ConfigString stringOption = (ConfigString) option;
            return CommandUtil.argument("text", StringArgumentType.greedyString()).executes(ClientCommand.of(context -> {
                Optional<String> value = CommandUtil.getArgument(context, "text", String.class);
                stringOption.setValueFromString(value.orElse(""));
                InfoUtil.sendChatMessage("Set!");
            })).build();
        }
        if (option.getType() == ConfigType.COLOR) {
            ConfigColor colorOption = (ConfigColor) option;
            return CommandUtil.argument("color", StringArgumentType.string()).executes(ClientCommand.of(context -> {
                Optional<String> value = CommandUtil.getArgument(context, "color", String.class);
                if (value.isEmpty()) {
                    sendInvalid("Color needs to be specified!");
                    return;
                }
                colorOption.setValueFromString(value.orElse(""));
                InfoUtil.sendChatMessage("Set!");
            })).build();
        }
        return null;
    }

    public void setup() {
        CommandManager.getInstance().unregister(invoker -> invoker.getModId().equals(AdvancedChatCore.MOD_ID));
        CommandInvoker<ServerCommandSource> command = new BaseCommandInvoker(
                AdvancedChatCore.MOD_ID,
                "advancedchat",
                CommandUtil.literal("advancedchat").executes(ClientCommand.of(context -> InfoUtil.sendChatMessage("AdvancedChatCore by DarkKronicle"))).build()
        );
        command.addChild(CommandUtil.literal("setTestRegex").then(
                CommandUtil.argument(
                        "value", StringArgumentType.greedyString()
                ).executes(ClientCommand.of(context -> {
                    Optional<String> value = CommandUtil.getArgument(context, "value", String.class);
                    if (value.isPresent()) {
                        InfoUtil.sendChatMessage("Set!");
                        TEST_REGEX = value.get();
                    } else {
                        InfoUtil.sendChatMessage("Not set!");
                    }
                })).build()).build());
        command.addChild(CommandUtil.literal("testRegex").then(
                CommandUtil.argument(
                        "value", StringArgumentType.greedyString()
                ).executes(ClientCommand.of(context -> {
                    Optional<String> value = CommandUtil.getArgument(context, "value", String.class);
                    if (value.isPresent()) {
                        String val = value.get();
                        val = val.replace('&', '§');
                        Text text = StyleFormatter.formatText(Text.literal(val));
                        Optional<List<StringMatch>> matches = SearchUtils.findMatches(text, TEST_REGEX, FindType.REGEX);
                        InfoUtil.sendChatMessage(text);
                        if (matches.isEmpty()) {
                            InfoUtil.sendChatMessage("None!");
                            return;
                        }
                        InfoUtil.sendChatMessage(String.join(", ", matches.get().stream().map(match -> match.match).toList()));
                    } else {
                        InfoUtil.sendChatMessage("Not there!");
                    }
                })).build()).build());
        command.addChild(CommandUtil.literal("reloadColors").executes(ClientCommand.of((context) -> {
            InfoUtil.sendChatMessage("Reloading colors...");
            Colors.getInstance().load();
            InfoUtil.sendChatMessage("Reloaded!", Formatting.GREEN);
        })).build());
        CommandManager.getInstance().addCommand(command);
        addOptions(getOrCreateSubs("coreconfig", "general"), ConfigStorage.General.OPTIONS);
        addOptions(getOrCreateSubs("coreconfig", "chatScreen"), ConfigStorage.ChatScreen.OPTIONS);
    }

}
