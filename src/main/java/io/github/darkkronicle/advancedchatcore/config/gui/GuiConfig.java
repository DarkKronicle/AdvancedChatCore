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
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.Color4f;
import io.github.darkkronicle.advancedchatcore.AdvancedChatCore;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;

// Based off of
// https://github.com/maruohon/minihud/blob/fabric_1.16_snapshots_temp/src/main/java/fi/dy/masa/minihud/gui/GuiConfigs.java
// Released under GNU LGPL
public class GuiConfig extends GuiConfigsBase {

    public static TabSupplier TAB = null;

    @Deprecated
    public GuiConfig(List<GuiConfigHandler.TabButton> tabButtons, List<IConfigBase> configs) {
        this();
    }

    public GuiConfig() {
        super(10, 50, AdvancedChatCore.MOD_ID, null, "advancedchat.screen.main");
    }

    @Override
    public void initGui() {
        if (TAB == null) {
            // Should be general
            for (int i = 0; i < GuiConfigHandler.getInstance().getTabs().size(); i++) {
                TabSupplier tab = GuiConfigHandler.getInstance().getTabs().get(i);
                if (tab.isSelectable()) {
                    TAB = tab;
                    break;
                }
            }
            if (TAB == null) {
                // sucks to suck lol
                TAB = GuiConfigHandler.getInstance().getTabs().get(0);
            }
        }
        boolean children = TAB.getChildren() != null && TAB.getChildren().size() != 0;

        Screen child = getFullyNestedSupplier(TAB).getScreen(this);
        if (child != null) {
            GuiBase.openGui(child);
            return;
        }

        clearElements();
        int x = 10;
        int y = 26;
        int rows = addTabButtons(this, x, y);
        y += rows * 22;
        if (children) {
            y += (addAllChildrenButtons(this, TAB, x, y) * 22);
        }
        setListPosition(getListX(), y + 10);
        if (this.getListWidget() != null) {
            this.getListWidget().setSize(this.getBrowserWidth(), this.getBrowserHeight());
            this.getListWidget().initGui();
        }

    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        return ConfigOptionWrapper.createFor(getFullyNestedSupplier(TAB).getOptions());
    }

    public static TabSupplier getFullyNestedSupplier(TabSupplier supplier) {
        if (supplier.getChildren() == null || supplier.getChildren().size() == 0) {
            return supplier;
        }
        return getFullyNestedSupplier(supplier.getNestedSelection());
    }

    public static int addAllChildrenButtons(GuiBase screen, TabSupplier supplier, int x, int y) {
        int rows = 0;
        if (supplier.getChildren() != null && supplier.getChildren().size() != 0) {
            x += 2;
            screen.addLabel(x, y, 10, 22, new Color4f(1, 1, 1, 1).intValue, ">");
            x += 8;
            addNestedTabButtons(screen, supplier, x, y);
            y += 22;
            rows++;
            if (supplier.getNestedSelection() != null) {
                rows += addAllChildrenButtons(screen, supplier.getNestedSelection(), x, y);
            }
        }
        return rows;
    }

    /**
     * Adds the category buttons to the selected screen
     * @param screen Screen to apply to
     * @return Amount of rows it created
     */
    public static int addTabButtons(GuiBase screen, int x, int y) {
        int rows = 1;
        for (TabSupplier tab : GuiConfigHandler.getInstance().getTabs()) {
            int width = screen.getStringWidth(tab.getDisplayName()) + 10;

            if (x >= screen.width - width - 10)
            {
                x = 10;
                y += 22;
                ++rows;
            }

            x += createTabButton(screen, x, y, width, tab);
        }
        return rows;
    }

    public static int addNestedTabButtons(GuiBase screen, TabSupplier supplier, int x, int y) {
        int rows = 1;
        for (TabSupplier tab : supplier.getChildren()) {
            int width = screen.getStringWidth(tab.getDisplayName()) + 10;

            if (x >= screen.width - width - 10)
            {
                x = 10;
                y += 22;
                ++rows;
            }

            x += createTabButton(screen, x, y, width, tab, supplier);
        }
        return rows;
    }

    private static int createTabButton(GuiBase screen, int x, int y, int width, TabSupplier tab) {
        return createTabButton(screen, x, y, width, tab, null);
    }

    private static int createTabButton(GuiBase screen, int x, int y, int width, TabSupplier tab, TabSupplier parent) {
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getDisplayName());
        if (parent == null) {
            button.setEnabled(GuiConfig.TAB != tab);
        } else {
            button.setEnabled(parent.getNestedSelection() != tab);
        }
        screen.addButton(button, new ButtonListenerTab(tab, parent));

        return button.getWidth() + 2;
    }

    public static class ButtonListenerTab implements IButtonActionListener {
        private final TabSupplier tab;
        private final TabSupplier parent;

        public ButtonListenerTab(TabSupplier tab, TabSupplier parent) {
            this.tab = tab;
            this.parent = parent;
        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            if (parent == null) {
                GuiConfig.TAB = this.tab;
            } else {
                parent.setNestedSelection(this.tab);
            }
            GuiBase.openGui(new GuiConfig());
        }
    }
}
