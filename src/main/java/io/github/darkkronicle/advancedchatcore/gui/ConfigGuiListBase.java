/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.gui;

import fi.dy.masa.malilib.gui.widgets.WidgetListBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import io.github.darkkronicle.advancedchatcore.config.gui.GuiConfigHandler;
import java.util.List;

/** GUI list base to work in a configuration screen */
public abstract class ConfigGuiListBase<
                TYPE,
                WIDGET extends WidgetListEntryBase<TYPE>,
                WIDGETLIST extends WidgetListBase<TYPE, WIDGET>>
        extends CoreGuiListBase<TYPE, WIDGET, WIDGETLIST> {

    private List<GuiConfigHandler.TabButton> tabButtons;

    public ConfigGuiListBase(List<GuiConfigHandler.TabButton> tabButtons) {
        this(10, 60, tabButtons);
    }

    public ConfigGuiListBase(int listX, int listY, List<GuiConfigHandler.TabButton> tabButtons) {
        super(listX, listY);
        this.tabButtons = tabButtons;
    }

    @Override
    public void initGui() {
        super.initGui();

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

        this.setListPosition(this.getListX(), 68 + (rows - 1) * 22);
        this.reCreateListWidget();
        this.getListWidget().refreshEntries();

        y += 24;

        initGuiConfig(x, y);
    }

    private int createButton(GuiConfigHandler.TabButton button, int y) {
        this.addButton(button.getButton(), button.createListener());
        return button.getButton().getY();
    }

    /** Method when the GUI is initialized. This one takes an X,Y that is away from the buttons */
    public void initGuiConfig(int x, int y) {}
}
