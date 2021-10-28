/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.config.gui;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import io.github.darkkronicle.advancedchatcore.AdvancedChatCore;
import java.util.List;

// Based off of
// https://github.com/maruohon/minihud/blob/fabric_1.16_snapshots_temp/src/main/java/fi/dy/masa/minihud/gui/GuiConfigs.java
// Released under GNU LGPL
public class GuiConfig extends GuiConfigsBase {

    private List<GuiConfigHandler.TabButton> tabButtons;
    private List<IConfigBase> configs;

    public GuiConfig(List<GuiConfigHandler.TabButton> tabButtons, List<IConfigBase> configs) {
        super(10, 50, AdvancedChatCore.MOD_ID, null, "advancedchat.screen.main");
        this.tabButtons = tabButtons;
        this.configs = configs;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();

        int x = 10;
        int y = 26;
        int rows = 1;

        for (GuiConfigHandler.TabButton tab : tabButtons) {
            int newY = this.createButton(tab, y);
            if (newY != y) {
                rows++;
                y = newY;
            }
        }

        if (rows > 1) {
            int scrollbarPosition = this.getListWidget().getScrollbar().getValue();
            this.setListPosition(this.getListX(), 50 + (rows - 1) * 22);
            this.reCreateListWidget();
            this.getListWidget().getScrollbar().setValue(scrollbarPosition);
            this.getListWidget().refreshEntries();
        }
    }

    private int createButton(GuiConfigHandler.TabButton button, int y) {
        this.addButton(button.getButton(), new ButtonListenerConfigTabs(button));
        return button.getButton().getY();
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        return ConfigOptionWrapper.createFor(configs);
    }

    private static class ButtonListenerConfigTabs implements IButtonActionListener {

        private final GuiConfigHandler.TabButton tabButton;

        public ButtonListenerConfigTabs(GuiConfigHandler.TabButton tabButton) {
            this.tabButton = tabButton;
        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            GuiConfigHandler.getInstance().activeTab = this.tabButton.getTab().getName();
            GuiBase.openGui(
                    this.tabButton.getTab().getScreen(GuiConfigHandler.getInstance().getButtons()));
        }
    }
}
