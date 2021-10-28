/*
 * Mozilla Public License v2.0
 *
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.interfaces;

import java.util.function.Supplier;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;

/** An interface to supply a screen. */
public interface IScreenSupplier {
    /**
     * Get's a supplier of a screen based off of a parent.
     *
     * @param parent Parent screen (nullable)
     * @return Supplier of a screen
     */
    Supplier<Screen> getScreen(@Nullable Screen parent);
}
