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
import io.github.darkkronicle.advancedchatcore.config.gui.GuiConfig;
import io.github.darkkronicle.advancedchatcore.config.gui.GuiConfigHandler;
import java.util.List;

/** GUI list base to work in a configuration screen */
public abstract class ConfigGuiListBase<
                TYPE,
                WIDGET extends WidgetListEntryBase<TYPE>,
                WIDGETLIST extends WidgetListBase<TYPE, WIDGET>>
        extends CoreGuiListBase<TYPE, WIDGET, WIDGETLIST> {

    public ConfigGuiListBase(List<GuiConfigHandler.TabButton> tabButtons) {
        this(10, 60, tabButtons);
    }

    public ConfigGuiListBase() {
        this(10, 60, null);
    }

    public ConfigGuiListBase(int listX, int listY, List<GuiConfigHandler.TabButton> tabButtons) {
        super(listX, listY);
    }

    @Override
    public void initGui() {
        super.initGui();

        int x = 10;
        int y = 26;

        y += (22 * GuiConfig.addTabButtons(this, 10, y));
        if (GuiConfig.TAB.getChildren() != null && GuiConfig.TAB.getChildren().size() > 0) {
            y += (22 * GuiConfig.addAllChildrenButtons(this, GuiConfig.TAB, 10, y));
        }
        this.setListPosition(this.getListX(), y);
        this.reCreateListWidget();
        this.getListWidget().refreshEntries();

        y += 24;

        initGuiConfig(x, y);
    }

    /** Method when the GUI is initialized. This one takes an X,Y that is away from the buttons */
    public void initGuiConfig(int x, int y) {}
}
