/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.config.gui;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.util.StringUtils;
import io.github.darkkronicle.advancedchatcore.config.SaveableConfig;
import io.github.darkkronicle.advancedchatcore.gui.buttons.ConfigTabsButtonListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public class GuiConfigHandler {

    private static final GuiConfigHandler INSTANCE = new GuiConfigHandler();

    @Deprecated
    public String activeTab = "";

    @Getter private final List<TabSupplier> tabs = new ArrayList<>();

    private GuiConfigHandler() {}

    @Deprecated
    public boolean isTabActive(GuiConfigHandler.Tab button) {
        return button.getName().equals(activeTab);
    }

    public static GuiConfigHandler getInstance() {
        return INSTANCE;
    }

    @Deprecated
    public void addGuiSection(Tab section) {
        if (section instanceof GuiConfigSection gui) {
            addTab(new TabSupplier(section.getName(), StringUtils.translate(section.getName())) {
                @Override
                public List<IConfigBase> getOptions() {
                    return gui.getOptions();
                }
            });
            return;
        }
        tabs.add(new TabSupplier(section.getName(), section.getName()) {
            @Override
            public Screen getScreen(Screen parent) {
                return section.getScreen(getButtons());
            }
        });
    }

    public void addTab(TabSupplier tab) {
        tabs.add(tab);
    }

    public TabSupplier get(String name) {
        for (TabSupplier child : tabs) {
            if (child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    @Deprecated
    public List<TabButton> getButtons() {
        int x = 10;
        int y = 26;
        int rows = 1;
        ArrayList<TabButton> buttons = new ArrayList<>();
        MinecraftClient client = MinecraftClient.getInstance();
        int windowWidth = client.getWindow().getScaledWidth();
        for (TabSupplier tab : tabs) {
            int width = client.textRenderer.getWidth(tab.getName()) + 10;

            if (x >= windowWidth - width - 10) {
                x = 10;
                y += 22;
                rows++;
            }

            ButtonGeneric button = this.createButton(x, y, width, tab);
            x += button.getWidth() + 2;
            buttons.add(new TabButton(tab, button));
        }
        return buttons;
    }

    private ButtonGeneric createButton(int x, int y, int width, TabSupplier tab) {
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getName());
        button.setEnabled(!GuiConfig.TAB.equals(tab));
        return button;
    }

    public Tab getTab(String name) {
        TabSupplier supplier = getTabSupplier(name);
        return new Tab() {
            @Override
            public String getName() {
                return supplier.getDisplayName();
            }

            @Override
            public Screen getScreen(List<TabButton> buttons) {
                return getDefaultScreen();
            }
        };
    }

    public TabSupplier getTabSupplier(String name) {
        for (TabSupplier b : tabs) {
            if (b.getName().equals(name)) {
                return b;
            }
        }
        return null;
    }

    public Screen getDefaultScreen() {
        return new GuiConfig();
    }

    @Deprecated
    public static GuiConfigSection createGuiConfigSection(String name, List<SaveableConfig<? extends IConfigBase>> configs) {
        List<IConfigBase> configBases = new ArrayList<>();
        for (SaveableConfig<? extends IConfigBase> saveable : configs) {
            configBases.add(saveable.config);
        }
        return new GuiConfigSection() {
            @Override
            public List<IConfigBase> getOptions() {
                return configBases;
            }

            @Override
            public String getName() {
                return StringUtils.translate(name);
            }
        };
    }

    @Deprecated
    @AllArgsConstructor
    @Value
    public static class TabButton {
        TabSupplier tabSupplier;
        ButtonGeneric button;

        @Deprecated
        public Tab getTab() {
            return new Tab() {
                @Override
                public String getName() {
                    return tabSupplier.getDisplayName();
                }

                @Override
                public Screen getScreen(List<TabButton> buttons) {
                    return new GuiConfig();
                }
            };
        }

        public ConfigTabsButtonListener createListener() {
            return new ConfigTabsButtonListener(this);
        }
    }

    public static TabSupplier wrapSaveableOptions(String name, String translationKey, Supplier<List<SaveableConfig<? extends IConfigBase>>> supplier) {
        Supplier<List<IConfigBase>> configSupplier = () -> {
            List<IConfigBase> config = new ArrayList<>();
            List<SaveableConfig<? extends IConfigBase>> options = supplier.get();
            for (SaveableConfig<? extends IConfigBase> s : options) {
                config.add(s.config);
            }
            return config;
        };
        return wrapOptions(name, translationKey, configSupplier);
    }

    public static TabSupplier wrapSaveableOptions(String name, String translationKey, List<SaveableConfig<? extends IConfigBase>> options) {
        List<IConfigBase> config = new ArrayList<>();
        for (SaveableConfig<? extends IConfigBase> s : options) {
            config.add(s.config);
        }
        return wrapOptions(name, translationKey, config);
    }

    public static TabSupplier wrapOptions(String name, String translationKey, List<IConfigBase> configs) {
        return wrapOptions(name, translationKey, () -> configs);
    }

    public static TabSupplier wrapOptions(String name, String translationKey, Supplier<List<IConfigBase>> options) {
        return new TabSupplier(name, translationKey) {
            @Override
            public List<IConfigBase> getOptions() {
                return options.get();
            }
        };
    }

    public static TabSupplier wrapScreen(String name, String translationKey, Function<Screen, Screen> screenSupplier) {
        return new TabSupplier(name, translationKey) {
            @Override
            public Screen getScreen(Screen parent) {
                return screenSupplier.apply(parent);
            }
        };
    }

    public static TabSupplier children(String name, String translationKey, TabSupplier... children) {
        TabSupplier tab = new TabSupplier(name, translationKey) {
            @Override
            public String getName() {
                return super.getName();
            }
        };
        for (TabSupplier child : children) {
            tab.addChild(child);
        }
        return tab;
    }

    @Deprecated
    public interface Tab {
        String getName();

        Screen getScreen(List<TabButton> buttons);
    }

    @Deprecated
    public interface GuiConfigSection extends Tab {
        List<IConfigBase> getOptions();

        String getName();

        @Override
        default Screen getScreen(List<TabButton> buttons) {
            GuiConfigHandler.getInstance().activeTab = this.getName();
            return new GuiConfig();
        }
    }
}
