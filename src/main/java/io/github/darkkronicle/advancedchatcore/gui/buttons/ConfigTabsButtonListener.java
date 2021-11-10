/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.gui.buttons;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import io.github.darkkronicle.advancedchatcore.config.gui.GuiConfigHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ConfigTabsButtonListener implements IButtonActionListener {

    private final GuiConfigHandler.TabButton tabButton;

    public ConfigTabsButtonListener(GuiConfigHandler.TabButton tabButton) {
        this.tabButton = tabButton;
    }

    @Override
    public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
        GuiConfigHandler.getInstance().activeTab = this.tabButton.getTab().getName();
        GuiBase.openGui(
                this.tabButton.getTab().getScreen(GuiConfigHandler.getInstance().getButtons()));
    }
}
